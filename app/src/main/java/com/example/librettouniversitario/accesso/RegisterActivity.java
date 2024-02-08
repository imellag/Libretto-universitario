package com.example.librettouniversitario.accesso;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.librettouniversitario.DatePickerFragment;
import com.example.librettouniversitario.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    protected Intent intent;

    protected EditText etName, etSurname, calendarBirthdate, etPassword;
    protected Button registerButton;

    protected TextView backToAccess;

    protected Calendar calendar;
    protected SimpleDateFormat simpleDateFormat;

    protected Intent back, confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.name);
        etSurname = findViewById(R.id.surname);
        calendarBirthdate = findViewById(R.id.birthdate);
        etPassword = findViewById(R.id.password);

        registerButton = findViewById(R.id.registerButton);
        backToAccess = findViewById(R.id.back_to_access);

        calendarBirthdate.setKeyListener(null);

        intent = getIntent();

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);

        registerButton.setOnClickListener(v -> {
            if (checkInput()) {
                confirm = new Intent(this, LoginActivity.class);
                saveUserCredentials();
                Toast.makeText(this, "Hai creato un nuovo account, ora puoi accedere!", Toast.LENGTH_SHORT).show();
                startActivity(confirm);
                finish();
            }
        });

        backToAccess.setOnClickListener(v -> {
            back = new Intent(this, LoginActivity.class);
            startActivity(back);
            finish();
        });

        calendarBirthdate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showDialog();
        });

        calendarBirthdate.setOnClickListener(v -> showDialog());

    }

    private void saveUserCredentials() {
        String nome = etName.getText().toString();
        String cognome = etSurname.getText().toString();
        String dataNascita = calendarBirthdate.getText().toString();
        String password = etPassword.getText().toString();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("NomeUtente", nome);
        editor.putString("CognomeUtente", cognome);
        editor.putString("DataNascita", dataNascita);
        editor.putString("Password", password);

        editor.apply();
    }

    protected void showDialog() {
        DialogFragment dialogFragment = DatePickerFragment.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick(Calendar calendar) {
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < calendar.get(Calendar.MONTH)) {
            age--;
        } else if (today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && today.get(Calendar.DAY_OF_MONTH) < calendar
                .get(Calendar.DAY_OF_MONTH)) {
            age--;
        }

        if (age >= 19) {
            calendarBirthdate.setText(simpleDateFormat.format(calendar.getTime()));
            calendarBirthdate.setError(null);
        } else {
            calendarBirthdate.setText("");
            calendarBirthdate.setError("Devi avere maggiorenne per poter creare un account!");
        }
    }

    public void doNegativeClick() {
    }

    protected boolean checkInput() {
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String birthdate = calendarBirthdate.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Inserisci un nome!");
            return false;
        } else {
            etName.setError(null);
        }

        if (surname.isEmpty()) {
            etSurname.setError("Inserisci un cognome!");
            return false;
        } else {
            etSurname.setError(null);
        }

        if (birthdate.isEmpty()) {
            calendarBirthdate.setError("Inserisci una data!");
            return false;
        } else {
            calendarBirthdate.setError(null);
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
}
