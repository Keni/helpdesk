package keni.paritet.ActionsTask.Date;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import keni.paritet.R;

/**
 * Created by Keni on 13.12.2016.
 */

public class SetTime
{
    private final Calendar myCalendar = Calendar.getInstance();

    public void setTime(Activity activity, final TextView inputTime)
    {
        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = myCalendar.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                inputTime.setText(String.format(Locale.ENGLISH, "%02d:%02d", selectedHour, selectedMinute));
            }
        }, hour, minute, true);
        mTimePicker.setTitle(R.string.selectTime);
        mTimePicker.show();
    }
}
