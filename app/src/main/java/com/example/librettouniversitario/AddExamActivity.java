package com.example.librettouniversitario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddExamActivity extends AppCompatActivity {

    private EditText etExamName, etCredits, etGrade;
    private int voto = 0, cfu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        etExamName = findViewById(R.id.etExamName);
        etCredits = findViewById(R.id.etCredits);
        etGrade = findViewById(R.id.etGrade);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveExam());
    }

    private void saveExam() {
        if (!checkExamInput()) return;

        String examName = etExamName.getText().toString();
        Exam newExam = new Exam(examName, cfu, voto);

        MainActivity.examsList.add(newExam);

        SharedPreferences prefs = getSharedPreferences("LibrettoUniversitario", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("EsameAggiunto", true);
        editor.apply();

        finish();
    }

    protected boolean checkExamInput() {
        boolean isValid = true;

        String examName = etExamName.getText().toString();
        String credits = etCredits.getText().toString();
        String grade = etGrade.getText().toString();

        if (examName.isEmpty()) {
            etExamName.setError("Inserisci un nome!");
            isValid = false;
        } else {
            etExamName.setError(null);
        }

        try {
            cfu = Integer.parseInt(credits);
            if (cfu <= 0) {
                etCredits.setError("L'esame non può valere 0 crediti!");
                isValid = false;
            } else {
                etCredits.setError(null);
            }
        } catch (NumberFormatException e) {
            etCredits.setError("Inserisci un numero valido di CFU!");
            isValid = false;
        }

        try {
            voto = Integer.parseInt(grade);
            if (voto < 18 || voto > 31) {
                etGrade.setError("Il voto deve essere compreso tra 18 e 31!");
                isValid = false;
            } else {
                etGrade.setError(null);
            }
        } catch (NumberFormatException e) {
            etGrade.setError("Inserisci un voto valido!");
            isValid = false;
        }

        return isValid;
    }

    public void goBack(View view) {
        finish();
    }
}
