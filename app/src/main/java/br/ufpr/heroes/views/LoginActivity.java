package br.ufpr.heroes.views;

import static br.ufpr.heroes.api.RequestClient.API_URL;
import static br.ufpr.heroes.api.RequestClient.COOKIE_NAME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.net.HttpCookie;
import java.net.URI;

import br.ufpr.heroes.R;
import br.ufpr.heroes.api.CredentialsManager;
import br.ufpr.heroes.api.Public;
import br.ufpr.heroes.api.RequestClient;
import br.ufpr.heroes.models.User;

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
        if(statusCode == 404) {
            Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show();
        } else if(statusCode == 409) {
            Toast.makeText(this, "Usuário já cadastrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro" + String.valueOf(statusCode), Toast.LENGTH_SHORT).show();
        }
    }
}