package keni.paritet.ActionsTask.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Keni on 13.12.2016.
 */

public class SetDate
{
    private final Calendar myCalendar = Calendar.getInstance();

    public void setDate(Activity activity, final TextView inputDate)
    {
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);
        int month = myCalendar.get(Calendar.MONTH);
        int year = myCalendar.get(Calendar.YEAR);
        DatePickerDialog mDatePicker;
        mDatePicker = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDayOfMonth)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth);

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
                String strDate = format.format(calendar.getTime());

                inputDate.setText(strDate);
            }
        }, year, month, day);
        mDatePicker.show();
    }
}
