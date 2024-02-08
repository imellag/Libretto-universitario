package com.example.librettouniversitario;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.librettouniversitario.accesso.RegisterActivity;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private Calendar calendar;
    private DatePicker datePicker;

    public static DatePickerFragment newInstance() {
        return new DatePickerFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        datePicker = new DatePicker(requireActivity());
        calendar = Calendar.getInstance();

        return new AlertDialog.Builder(requireActivity())
                .setView(datePicker)
                .setPositiveButton(R.string.birthdate_confirm, this)
                .setNegativeButton(R.string.birthdate_revert, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                ((RegisterActivity) requireActivity()).doPositiveClick(calendar);
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                ((RegisterActivity) requireActivity()).doNegativeClick();
                break;
        }
    }
}
