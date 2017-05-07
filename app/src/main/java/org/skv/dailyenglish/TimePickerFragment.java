package org.skv.dailyenglish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

/**
 * Created by keviny.seo on 2017. 4. 29..
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private MainActivity mainActivity;

    public TimePickerFragment(MainActivity mainAct) {
        mainActivity = mainAct;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("TimePickerFragment", "OnTimeSet Call");
        mainActivity.createAlarm(hourOfDay, minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                getContext(), AlertDialog.THEME_HOLO_DARK, this, hour, min, DateFormat.is24HourFormat(getContext())
        );
        return mTimePickerDialog;
    }
}
