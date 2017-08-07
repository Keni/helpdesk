package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import keni.paritet.ActionsTask.Date.SetDate;
import keni.paritet.ActionsTask.Date.SetTime;
import keni.paritet.ActionsTask.Update.TaskReportEdit;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 13.12.2016.
 */

public class EditReportDialog
{
    public void editReportDialog(final Activity activity, final String app_id, final String report_id, final String user_id, final String comment, final String dt0Date, final String dt1Date, final String dt0Time, final String dt1Time, final String solution_id, final String act)
    {
        final AlertDialog.Builder dialogEditReport = new AlertDialog.Builder(activity);

        // Инициализируем разметку диалога
        ScrollView view = (ScrollView) activity.getLayoutInflater().inflate(R.layout.dialog_add_report, null);
        RelativeLayout viewTitle = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dialog_custom_title, null);

        // Инициализируем текст с коментарием
        final EditText editTextReport = (EditText) view.findViewById(R.id.editTextReport);
        editTextReport.setText(comment);
        editTextReport.setSelection(editTextReport.getText().length());

        // Инициализируем дату
        final TextView inputDate0 = (TextView) view.findViewById(R.id.textViewEditDT0);
        inputDate0.setText(dt0Date);
        final TextView inputDate1 = (TextView) view.findViewById(R.id.textViewEditDT1);
        inputDate1.setText(dt1Date);

        // Инициализируем время
        final TextView inputTime0 = (TextView) view.findViewById(R.id.textViewEditTimeDT0);
        inputTime0.setText(dt0Time);
        final TextView inputTime1 = (TextView) view.findViewById(R.id.textViewEditTimeDT1);
        inputTime1.setText(dt1Time);

        // Инициализируем выполняется ли репорт по акту
        final CheckBox checkBoxAct = (CheckBox) view.findViewById(R.id.checkboxAct);
        if (act.equals("1"))
            checkBoxAct.setChecked(true);

        // Получаем лист и заполняем его исполнителями
        ArrayList<String> performers = new ArrayList<>();
        for (int i = 0; i < Config.users.size(); i++)
            performers.add(Config.users.get(i).get(Config.TAG_USER));

        // Инициализируем адаптер и сам спиннер для списка исполнителей
        ArrayAdapter<String> adapterUsers = new ArrayAdapter<>(activity, R.layout.spinner_items, performers);
        adapterUsers.setDropDownViewResource(R.layout.spinner_dropdown_items);
        final Spinner spinnerPerformers = (Spinner) view.findViewById(R.id.spinnerPerformer);
        spinnerPerformers.setAdapter(adapterUsers);

        // Выставляем текущего исполнителя
        for (int i = 0; i < Config.users.size(); i++)
            if (Config.users.get(i).get(Config.TAG_USER_ID).equals(user_id))
                spinnerPerformers.setSelection(i);

        // Получаем лист и заполняем его типами работ
        ArrayList<String> solution = new ArrayList<>();
        for (int i = 0; i < Config.solution.size(); i++)
            solution.add(Config.solution.get(i).get(Config.TAG_SOLUTION_NAME));

        // Инициализируем адаптер и сам спиннер для списка типа работ
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(activity, R.layout.spinner_items, solution);
        adapterSpinner.setDropDownViewResource(R.layout.spinner_dropdown_items);
        final Spinner spinnerSolution = (Spinner) view.findViewById(R.id.spinnerSolution);
        spinnerSolution.setAdapter(adapterSpinner);

        // Выставляем текущий вид работы
        for (int i = 0; i < Config.solution.size(); i++)
            if (Config.solution.get(i).get(Config.TAG_SOLUTION_ID).equals(solution_id))
                spinnerSolution.setSelection(i);

        // Выбор даты начала наряда
        inputDate0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(activity, inputDate0);
            }
        });

        // Выбор даты окончания наряда
        inputDate1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(activity, inputDate1);
            }
        });

        // Выбор времени начала наряда
        inputTime0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SetTime st = new SetTime();
                st.setTime(activity, inputTime0);
            }
        });

        // Выбор времени окончания наряда
        inputTime1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetTime st = new SetTime();
                st.setTime(activity, inputTime1);
            }
        });

        // Установка кастомного вью элемента для диалога, установка иконки и заголовка
        dialogEditReport.setView(view);
        dialogEditReport.setCustomTitle(viewTitle);

        final TextView textViewTitle = (TextView) viewTitle.findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.reportEdit);

        // Действия при кнопке применить
        dialogEditReport.setPositiveButton(R.string.buttonAccept, null);

        // Действие при отмене
        dialogEditReport.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogEditReport.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveReport(activity, dialog, checkBoxAct, spinnerPerformers, spinnerSolution, app_id, report_id, editTextReport.getText().toString(),
                        inputDate0, inputTime0, inputDate1, inputTime1);
            }
        });

        final ImageButton imageButtonSave = (ImageButton) viewTitle.findViewById(R.id.imageButtonSave);
        imageButtonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveReport(activity, dialog, checkBoxAct, spinnerPerformers, spinnerSolution, app_id, report_id, editTextReport.getText().toString(),
                        inputDate0, inputTime0, inputDate1, inputTime1);
            }
        });
    }

    private void saveReport(Activity activity, AlertDialog dialog, CheckBox checkBoxAct, Spinner spinnerPerformers, Spinner spinnerSolution, String app_id,
                            String report_id, String comment, TextView inputDate0, TextView inputTime0, TextView inputDate1, TextView inputTime1)
    {
        String startDate = inputDate0.getText().toString() + " " + inputTime0.getText().toString();
        String endDate = inputDate1.getText().toString() + " " + inputTime1.getText().toString();

        Date startDT = new Date();
        Date endDT = new Date();
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
        try
        {
            startDT = format.parse(startDate);
            if (!endDate.equals(" "))
                endDT = format.parse(endDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        if (startDT.before(endDT) || endDate.equals(" "))
        {
            String performer = null;
            String solution = null;
            String act = "null";

            if (checkBoxAct.isChecked())
                act = "1";

            for (int j = 0; j < Config.users.size(); j++)
                if (Config.users.get(j).get(Config.TAG_USER).equals(spinnerPerformers.getSelectedItem().toString()))
                    performer = Config.users.get(j).get(Config.TAG_USER_ID);

            for (int j = 0; j < Config.solution.size(); j++)
                if (Config.solution.get(j).get(Config.TAG_SOLUTION_NAME).equals(spinnerSolution.getSelectedItem().toString()))
                    solution = Config.solution.get(j).get(Config.TAG_SOLUTION_ID);

            TaskReportEdit tre = new TaskReportEdit();
            tre.reportEditTask(activity, app_id, report_id, performer, comment, startDate, endDate, solution, act);
            activity.recreate();
            dialog.dismiss();
        }
        else
        {
            Toast toast = Toast.makeText(activity, R.string.errorTimeReport, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}
