package org.skv.dailyenglish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by keviny.seo on 2017. 4. 29..
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    TimePickerDialog mTimePickerDialog;
    private MainActivity mainActivity;
    boolean needTimeSet = false;

    public void setActivity(MainActivity mainAct) {
        mainActivity = mainAct;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.i("TimePickerFragment", "OnTimeSet Call");
        if (needTimeSet == true) {
            mainActivity.createAlarm(hourOfDay, minute);
            Toast.makeText(mainActivity, "Set Alarm Normally", Toast.LENGTH_SHORT).show();
            needTimeSet = false;
        } else {
            Toast.makeText(mainActivity, "Cancel Alarm Setting", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i("TimePickerFragment", "onCreateDialog");
        needTimeSet = false;
        Calendar mCalendar = Calendar.getInstance();
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int min = mCalendar.get(Calendar.MINUTE);
        mTimePickerDialog = new TimePickerDialog(
                getContext(), AlertDialog.THEME_HOLO_DARK, this, hour, min, DateFormat.is24HourFormat(getContext())
        );
        mTimePickerDialog.setCancelable(true);
        mTimePickerDialog.setCanceledOnTouchOutside(true);

        mTimePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("TimePickerFragment", "Ok Click !!");
                needTimeSet = true;
            }
        });

        return mTimePickerDialog;
    }


}
