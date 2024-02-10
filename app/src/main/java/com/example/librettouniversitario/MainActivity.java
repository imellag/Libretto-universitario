package com.example.librettouniversitario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.librettouniversitario.accesso.LoginActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ExamAdapter.OnExamListener {
    public static List<Exam> examsList;
    private ExamAdapter adapter;
    private TextView tvVotoLaurea, mediaP, mediaA;
    private ProgressBar horizontalProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvVotoLaurea = findViewById(R.id.voto_laurea);
        mediaP = findViewById(R.id.tvMediaPonderata);
        mediaA = findViewById(R.id.tvMediaAritmetica);
        horizontalProgressBar = findViewById(R.id.horizontalProgressBar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        examsList = new ArrayList<>();
        adapter = new ExamAdapter(examsList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateUI() {
        calcolaMediaPonderata();
        calcolaMediaAritmetica();
        aggiornaProgressBar();
        adapter.notifyDataSetChanged();
    }

    private void aggiornaProgressBar() {
        double mediaPonderata = calcolaMediaPonderata();
        double votoLaurea = (mediaPonderata * 110) / 30;
        int progressValue = (int) ((votoLaurea * 100) / 110);
        horizontalProgressBar.setProgress(progressValue);
        tvVotoLaurea.setText(getString(R.string.voto_di_laurea_previsto, votoLaurea));
    }

    private double calcolaMediaPonderata() {
        double sommaPonderata = 0;
        int sommaCrediti = 0;

        for (Exam exam : examsList) {
            sommaPonderata += exam.getGrade() * exam.getCfu();
            sommaCrediti += exam.getCfu();
        }

        if (sommaCrediti > 0) {
            double mediaPonderata = sommaPonderata / sommaCrediti;
            mediaP.setText(String.format(Locale.ENGLISH, "%.2f", mediaPonderata));
            return mediaPonderata;
        } else {
            mediaP.setText("0.00");
            return 0;
        }
    }

    private void calcolaMediaAritmetica() {
        double sommaVoti = 0;

        for (Exam exam : examsList) {
            sommaVoti += exam.getGrade();
        }

        double mediaAritmetica = examsList.isEmpty() ? 0 : sommaVoti / examsList.size();
        mediaA.setText(String.format(Locale.ENGLISH, "%.2f", mediaAritmetica));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("LibrettoUniversitario", MODE_PRIVATE);
        boolean esameAggiunto = prefs.getBoolean("EsameAggiunto", false);
        if (esameAggiunto) {
            updateUI();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("EsameAggiunto", false);
            editor.apply();
        }
    }

    public void addExam(View view) {
        Intent intent = new Intent(this, AddExamActivity.class);
        startActivity(intent);
    }

    @Override
    public void onExamDelete(int position) {
        examsList.remove(position);
        updateUI();
    }

    public void logOut(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
