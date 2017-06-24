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

import keni.paritet.ActionsTask.Add.TaskReportAdd;
import keni.paritet.ActionsTask.Date.SetDate;
import keni.paritet.ActionsTask.Date.SetTime;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 15.12.2016.
 */

public class AddReportDialog
{
    public void addReportDialog(final Activity activity, final String app_id)
    {
        final AlertDialog.Builder dialogAddReport = new AlertDialog.Builder(activity);

        ScrollView view = (ScrollView) activity.getLayoutInflater().inflate(R.layout.dialog_add_report, null);
        RelativeLayout viewTitle = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dialog_custom_title, null);

        final EditText editTextReport = (EditText) view.findViewById(R.id.editTextReport);
        final CheckBox checkBoxAct = (CheckBox) view.findViewById(R.id.checkboxAct);

        ArrayList<String> performers = new ArrayList<>();
        for (int i = 0; i < Config.users.size(); i++)
            performers.add(Config.users.get(i).get(Config.TAG_USER));

        ArrayAdapter<String> adapterUsers = new ArrayAdapter<String>(activity, R.layout.spinner_items, performers);
        adapterUsers.setDropDownViewResource(R.layout.spinner_dropdown_items);
        final Spinner spinnerPerformers = (Spinner) view.findViewById(R.id.spinnerPerformer);
        spinnerPerformers.setAdapter(adapterUsers);

        ArrayList<String> solution = new ArrayList<>();
        for (int i = 0; i < Config.solution.size(); i++)
            solution.add(Config.solution.get(i).get(Config.TAG_SOLUTION_NAME));

        ArrayAdapter<String> adapterSolution = new ArrayAdapter<String>(activity, R.layout.spinner_items, solution);
        adapterSolution.setDropDownViewResource(R.layout.spinner_dropdown_items);
        final Spinner spinnerSolution = (Spinner) view.findViewById(R.id.spinnerSolution);
        spinnerSolution.setAdapter(adapterSolution);

        final TextView inputTime0 = (TextView) view.findViewById(R.id.textViewEditTimeDT0);
        final TextView inputTime1 = (TextView) view.findViewById(R.id.textViewEditTimeDT1);

        final TextView inputDate0 = (TextView) view.findViewById(R.id.textViewEditDT0);
        final TextView inputDate1 = (TextView) view.findViewById(R.id.textViewEditDT1);

        inputDate0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(activity, inputDate0);
            }
        });

        inputDate1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(activity, inputDate1);
            }
        });

        inputTime0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SetTime st = new SetTime();
                st.setTime(activity, inputTime0);
            }
        });

        inputTime1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetTime st = new SetTime();
                st.setTime(activity, inputTime1);
            }
        });

        dialogAddReport.setView(view);
        dialogAddReport.setCustomTitle(viewTitle);

        final TextView textViewTitle = (TextView) viewTitle.findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.reportAdd);

        dialogAddReport.setPositiveButton(R.string.buttonAdd, null);

        dialogAddReport.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogAddReport.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addReport(activity, dialog, inputDate0, inputTime0, inputDate1, inputTime1, app_id,
                        editTextReport.getText().toString(), checkBoxAct, spinnerPerformers, spinnerSolution);
            }
        });

        final ImageButton imageButtonAddReport = (ImageButton) viewTitle.findViewById(R.id.imageButtonSave);
        imageButtonAddReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addReport(activity, dialog, inputDate0, inputTime0, inputDate1, inputTime1, app_id,
                        editTextReport.getText().toString(), checkBoxAct, spinnerPerformers, spinnerSolution);
            }
        });
    }

    private void addReport(Activity activity, AlertDialog dialog, TextView inputDate0, TextView inputTime0, TextView inputDate1, TextView inputTime1, String app_id, String comment, CheckBox checkBoxAct, Spinner spinnerPerformers, Spinner spinnerSolution)
    {
        if (!inputDate0.getText().toString().trim().isEmpty() && !inputTime0.getText().toString().trim().isEmpty())
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

            if (startDT.before(endDT))
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

                TaskReportAdd tra = new TaskReportAdd();
                tra.reportAddTask(activity, app_id, performer, comment, startDate, endDate, solution, act);
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
        else
        {
            if (inputDate0.getText().toString().trim().isEmpty())
                inputDate0.setError(activity.getText(R.string.errorEmptyField));
            if (inputTime0.getText().toString().trim().isEmpty())
                inputTime0.setError(activity.getText(R.string.errorEmptyField));

            Toast toast = Toast.makeText(activity, R.string.errorEmptyFieldToast, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }
}
