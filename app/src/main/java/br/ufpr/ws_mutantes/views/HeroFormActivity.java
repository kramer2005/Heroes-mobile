package br.ufpr.ws_mutantes.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.ufpr.ws_mutantes.BuildConfig;
import br.ufpr.ws_mutantes.R;
import br.ufpr.ws_mutantes.adapters.SpinnerAdapter;
import br.ufpr.ws_mutantes.api.AbilitiesApi;
import br.ufpr.ws_mutantes.api.HeroesApi;
import br.ufpr.ws_mutantes.api.RequestClient;
import br.ufpr.ws_mutantes.models.Hero;

public class HeroFormActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int mutantId;
    EditText mutantName, mutantHistory, mutantMovie;
    ImageView mutantImage, goBack;
    FlowLayout mutantAbilities;
    public List<String> abilities;
    public List<String> selectedAbilities;
    boolean imageChanged = false;
    AutoCompleteTextView abilityAutocomplete;
    Bitmap b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_form);

        Intent intent = getIntent();
        mutantId = intent.getIntExtra("mutantId", -1);
        mutantName = findViewById(R.id.mutantName);
        mutantHistory = findViewById(R.id.mutantHistory);
        mutantImage = findViewById(R.id.mutantImage);
        mutantMovie = findViewById(R.id.mutantMovie);
        mutantAbilities = findViewById(R.id.mutant_abilities);
        goBack = findViewById(R.id.goBack);
        abilityAutocomplete = findViewById(R.id.abilitiesAutocomplete);
        selectedAbilities = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            TextView abilityTextView = (TextView) mutantAbilities.getChildAt(i);
            int finalI = i;
            abilityTextView.setOnClickListener((View v) -> {
                abilityAutocomplete.setEnabled(true);
                selectedAbilities.remove(finalI);
                updateAbilities();
            });
        }

        if (mutantId >= 0) {
            setupMutant();
        }

        AbilitiesApi.getAbilities((int status, List<String> list) -> {
            abilities = list;
            ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list, list);
            mArrayAdapter.setDropDownViewResource(R.layout.spinner_list);
            abilityAutocomplete.setAdapter(mArrayAdapter);
            abilityAutocomplete.setOnItemClickListener((parent, arg1, pos, id) -> {
                String selected = mArrayAdapter.getItem(pos);
                if (!selectedAbilities.contains(selected)) {
                    selectedAbilities.add(selected);
                    updateAbilities();
                    abilityAutocomplete.setText("");
                }
                if (selectedAbilities.size() >= 3) {
                    abilityAutocomplete.setEnabled(false);
                }
            });
            abilityAutocomplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                        String selected = abilityAutocomplete.getText().toString();
                        if (!selectedAbilities.contains(selected)) {
                            selectedAbilities.add(selected);
                            updateAbilities();
                            abilityAutocomplete.setText("");
                        }
                        if (selectedAbilities.size() >= 3) {
                            abilityAutocomplete.setEnabled(false);
                        }
                    }
                    return false;
                }
            });
        }, getApplicationContext());
    }

    private void updateAbilities() {
        for (int i = 0; i < selectedAbilities.size(); i++) {
            TextView abilityTextView = (TextView) mutantAbilities.getChildAt(i);
            abilityTextView.setVisibility(View.VISIBLE);
            abilityTextView.setText(selectedAbilities.get(i));
        }
        for (int i = selectedAbilities.size(); i < 3; i++) {
            TextView abilityTextView = (TextView) mutantAbilities.getChildAt(i);
            abilityTextView.setVisibility(View.GONE);
        }
    }

    private void setupMutant() {
        HeroesApi.getHero((int status, Hero hero) -> {
            mutantName.setText(hero.name);
            mutantHistory.setText(hero.description);
            mutantMovie.setText(hero.movie);
            Glide.with(getApplicationContext())
                    .load(RequestClient.API_URL + "/images/" + hero.image)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(mutantImage);
            for (int i = 0; i < hero.abilities.size(); i++) {
                TextView abilityTextView = (TextView) mutantAbilities.getChildAt(i);
                abilityTextView.setVisibility(View.VISIBLE);
                abilityTextView.setText(hero.abilities.get(i));
            }
            selectedAbilities.addAll(hero.abilities);
            if (hero.abilities.size() >= 3) {
                abilityAutocomplete.setEnabled(false);
            }
        }, this.getApplicationContext(), mutantId);
    }

    String currentPhotoPath;
    Uri currentPathUri;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                        BuildConfig.APPLICATION_ID + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void dispatchOpenGalleryIntent(View v) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 0);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    currentPathUri = data.getData();
                    try {
                        b = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageChanged = true;
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    currentPathUri = Uri.parse(currentPhotoPath);
                    b = BitmapFactory.decodeFile(currentPathUri.toString());
                    imageChanged = true;
                }

                break;
        }
        mutantImage.setImageURI(currentPathUri);
    }

    public void saveMutant(View v) {
        ArrayList<String> newAbilities = new ArrayList<>(selectedAbilities);
        Hero hero = new Hero(
                mutantId,
                mutantName.getText().toString(),
                mutantHistory.getText().toString(),
                mutantMovie.getText().toString(),
                newAbilities
        );

        if (imageChanged) {
            hero.image = currentPathUri.toString();
        } else {
            if (mutantId < 0) {
                Toast.makeText(this, "O herói precisa ter uma imagem", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (selectedAbilities.size() < 1) {
            Toast.makeText(this, "O mutante precisa ter uma habilidade", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (mutantId >= 0) {
                HeroesApi.putHero((int status, Hero response) -> {
                    finish();
                }, getApplicationContext(), hero, b);
            } else {
                HeroesApi.postHero((int status, Hero response) -> {
                    if (status == 409) {
                        Toast.makeText(this, "Mutante já cadastrado", Toast.LENGTH_SHORT).show();
                    } else {
                        finish();
                    }
                }, getApplicationContext(), hero, b);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}