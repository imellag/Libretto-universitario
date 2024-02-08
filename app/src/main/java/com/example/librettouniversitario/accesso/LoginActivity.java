package com.example.librettouniversitario.accesso;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.librettouniversitario.MainActivity;
import com.example.librettouniversitario.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etName, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etName = findViewById(R.id.name);
        etPassword = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);
        TextView newAccount = findViewById(R.id.new_account);

        loginButton.setOnClickListener(v -> {
            if (validateInput()) {
                login();
            }
        });

        newAccount.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private boolean validateInput() {
        String username = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty()) {
            etName.setError("Inserisci un nome!");
            return false;
        } else {
            etName.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Inserisci una password!");
            return false;
        } else if (password.length() < 6) {
            etPassword.setError("La password deve avere almeno 6 caratteri!");
            return false;
        } else {
            etPassword.setError(null);
        }

        return true;
    }

    private boolean validateCredentials(String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("NomeUtente", null);
        String savedPassword = sharedPreferences.getString("Password", null);

        return username.equals(savedUsername) && password.equals(savedPassword);
    }

    private void login() {
        String username = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateCredentials(username, password)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Login fallito. Per favore, controlla le tue credenziali o crea un account se non lo hai ancora fatto.", Toast.LENGTH_LONG).show();
        }
    }
}
