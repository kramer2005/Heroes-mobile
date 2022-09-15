package br.ufpr.ws_mutantes.views;

import static br.ufpr.ws_mutantes.api.RequestClient.API_URL;
import static br.ufpr.ws_mutantes.api.RequestClient.COOKIE_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.net.HttpCookie;
import java.net.URI;

import br.ufpr.ws_mutantes.R;
import br.ufpr.ws_mutantes.api.CredentialsManager;
import br.ufpr.ws_mutantes.api.Public;
import br.ufpr.ws_mutantes.api.RequestClient;
import br.ufpr.ws_mutantes.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(CredentialsManager.isLoggedIn(this)){
            successFullLogin();
            if(RequestClient.getCookieStore() == null) {
                RequestClient.setCookieStore();
            }
            HttpCookie cookie = new HttpCookie(COOKIE_NAME, CredentialsManager.getUserToken(this));
            RequestClient.getCookieStore().add(URI.create(API_URL), cookie);
        }

        this.emailInput = findViewById(R.id.emailInput);
        this.passwordInput = findViewById(R.id.passwordInput);
    }

    private boolean isValidFields() {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.getText().toString()).matches()){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();

            return false;
        }

        if(passwordInput.getText().toString().length() <= 0) {
            Toast.makeText(this, "Insira sua senha", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    public void register(View view) throws JSONException {
        if(isValidFields()){
            User user = new User(emailInput.getText().toString(), passwordInput.getText().toString());
            Public.register(user, this);
        }
    }

    public void login(View view) throws JSONException {
        if(isValidFields()){
            User user = new User(emailInput.getText().toString(), passwordInput.getText().toString());
            Public.login(user, this);
        }
    }

    public void successFullLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void loginFailure(int statusCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(statusCode == 404) {
            builder.setMessage(R.string.failed_login_message);
        } else if(statusCode == 409) {
            builder.setMessage(R.string.failed_register_message);
        } else {
            Toast.makeText(this, "Erro" + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
        }

        builder.create().show();
    }
}