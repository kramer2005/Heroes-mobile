package br.ufpr.heroes.models;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ufpr.heroes.R;
import br.ufpr.heroes.api.RequestClient;

public class registration extends AppCompatActivity {

    private int cont = 1;
    private EditText parent;
    private ConstraintLayout layout;
    private ImageView Image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        this.parent = (EditText) findViewById(R.id.power);
        this.layout = (ConstraintLayout) findViewById(R.id.CLayout);
        this.Image = (ImageView) findViewById(R.id.image);

    }

    public void addField(View view){
        cont++;

        EditText et = new EditText(this);
        et.setId(cont);
        et.setHint("Poder");
        et.setText("");

        ConstraintLayout.LayoutParams param = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_CONSTRAINT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        et.setLayoutParams(param);
        this.layout.addView(et);

        ConstraintSet cs = new ConstraintSet();
        cs.clone(this.layout);
        cs.connect((cont), ConstraintSet.TOP, parent.getId(), ConstraintSet.BOTTOM, 8);
        cs.connect((cont), ConstraintSet.LEFT, parent.getId(), ConstraintSet.LEFT, 0);
        cs.connect((cont), ConstraintSet.RIGHT, parent.getId(), ConstraintSet.RIGHT, 0);
        cs.applyTo(this.layout);

        this.parent = et;
    }

    public void removeField(View view){
        if (cont == 1){
            Toast.makeText(this, "Deve ter ao menos um poder para ser um mutante!", Toast.LENGTH_LONG).show();
        } else{
            this.layout.removeView((EditText) findViewById(cont));
            if(cont==2){
                this.parent =(EditText) findViewById(R.id.power);
            } else {
                this.parent = (EditText) findViewById(cont - 1);
            }
            cont--;
        }
    }

    public void image(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    public void add (View view){
        Hero hero = new Hero();
        Boolean empty = false;
        TextView nome = (EditText) findViewById(R.id.name);
        hero.setNome(nome.getText().toString());
        if(hero.getNome().isEmpty()){
            empty = true;
        }

        //imagem
        Bitmap bm= ((BitmapDrawable)Image.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] b = outputStream.toByteArray();
        final String img=  Base64.encodeToString(b, Base64.DEFAULT);

        List<String> powers = new ArrayList();
        TextView power = (TextView) findViewById(R.id.power);
        if(power.getText().toString().isEmpty()){
            empty = true;
        }
        powers.add(power.getText().toString());

        for(int i = 2; i <= cont; i++){
            power = (TextView) findViewById(i);
            if(power.getText().toString().isEmpty()){
                empty = true;
            }
            powers.add(power.getText().toString());
        }
        hero.setPowers(powers);

        if(empty){
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_LONG).show();
        }else {

            RequestQueue requestQueue = RequestClient.getInstance(this).getRequestQueue();
            final Context contexto = this;

            final String mutNome = hero.getNome();
            String hab ="";
            for(String h: hero.getPowers()){
                hab += h+',';
            }
            final String habilidades = hab;
            //final String usr= RequestClient.usuario;

            StringRequest request = new StringRequest(StringRequest.Method.POST, RequestClient.API_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("1")) {
                        Toast.makeText(contexto, "Adicionado com sucesso", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(contexto, "Mutante já cadastrado", Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }

            }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("operacao", "adicionar");
                    params.put("nome", mutNome);
                    params.put("habilidades", habilidades);
                    params.put("foto", img);
                    //params.put("usuario", usr);
                    return params;
                }
            };
            requestQueue.add(request);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageView temp = this.Image;
                this.Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}