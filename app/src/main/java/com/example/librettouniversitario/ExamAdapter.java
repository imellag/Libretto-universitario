package com.example.librettouniversitario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamViewHolder> {

    private final List<Exam> examsList;
    private final OnExamListener onExamListener;

    public ExamAdapter(List<Exam> examsList, OnExamListener onExamListener) {
        this.examsList = examsList;
        this.onExamListener = onExamListener;
    }

    @NonNull
    @Override
    public ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item, parent, false);
        return new ExamViewHolder(itemView, onExamListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamViewHolder holder, int position) {
        Exam exam = examsList.get(position);
        holder.bind(exam);
    }

    @Override
    public int getItemCount() {
        return examsList.size();
    }

    public static class ExamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView examNameTextView, creditsTextView, gradeTextView;
        private final OnExamListener listener;

        public ExamViewHolder(View view, OnExamListener listener) {
            super(view);
            this.listener = listener;
            examNameTextView = view.findViewById(R.id.examName);
            creditsTextView = view.findViewById(R.id.examCfu);
            gradeTextView = view.findViewById(R.id.examGrade);
            Button deleteButton = view.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(this);
        }

        public void bind(Exam exam) {
            examNameTextView.setText(exam.getName());
            creditsTextView.setText("CFU: " + exam.getCfu());
            gradeTextView.setText("Voto: " + exam.getGrade());
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onExamDelete(position);
                }
            }
        }
    }

    public interface OnExamListener {
        void onExamDelete(int position);
    }
}
