package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
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

import keni.paritet.Tools.SearchableSpinner;
import keni.paritet.ActionsTask.Add.NewTaskAdd;
import keni.paritet.ActionsTask.Date.SetDate;
import keni.paritet.ActionsTask.Date.SetTime;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 23.12.2016.
 */

public class AddNewTaskDialog
{
    public void addNewTaskDialog(final Activity activity)
    {
        final AlertDialog.Builder dialogAddNewTask = new AlertDialog.Builder(activity);

        ScrollView view = (ScrollView) activity.getLayoutInflater().inflate(R.layout.dialog_add_new_task, null);
        RelativeLayout viewTitle = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.dialog_custom_title, null);

        final SearchableSpinner spinnerObjects = (SearchableSpinner) view.findViewById(R.id.spinnerObj);
        spinnerObjects.setAdapter(arrayAdapter(activity, Config.objects));
        spinnerObjects.setTitle(String.valueOf(activity.getText(R.string.selectObj)));
        spinnerObjects.setPositiveButton(String.valueOf(activity.getText(R.string.buttonAccept)));

        final EditText editTextReason = (EditText) view.findViewById(R.id.editTextReason);

        final Spinner spinnerPriority = (Spinner) view.findViewById(R.id.spinnerPriority);
        spinnerPriority.setAdapter(arrayAdapter(activity, Config.priority));
        spinnerPriority.setSelection(1);

        final TextView inputExpireDate = (TextView) view.findViewById(R.id.textViewDeadLineDate);
        final TextView inputExpireTime = (TextView) view.findViewById(R.id.textViewDeadLineTime);

        inputExpireDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(activity, inputExpireDate);
            }
        });

        inputExpireTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetTime st = new SetTime();
                st.setTime(activity, inputExpireTime);
            }
        });

        ArrayList<String> performers = new ArrayList<>();
        for (int i = 0; i < Config.users.size(); i++)
            performers.add(Config.users.get(i).get(Config.TAG_USER));

        if (!performers.get(0).equals(activity.getText(R.string.notSelected)))
            performers.add(0, String.valueOf(activity.getText(R.string.notSelected)));

        ArrayAdapter<String> adapterUsers = new ArrayAdapter<>(activity, R.layout.spinner_items, performers);
        adapterUsers.setDropDownViewResource(R.layout.spinner_dropdown_items);
        final Spinner spinnerPerformers = (Spinner) view.findViewById(R.id.spinnerPerformer);
        spinnerPerformers.setAdapter(adapterUsers);

        dialogAddNewTask.setView(view);
        dialogAddNewTask.setCustomTitle(viewTitle);

        final TextView textViewTitle = (TextView) viewTitle.findViewById(R.id.textViewTitle);
        textViewTitle.setText(R.string.addTask);

        dialogAddNewTask.setPositiveButton(R.string.buttonAdd, null);

        dialogAddNewTask.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogAddNewTask.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkFormAndAdd(activity, dialog, editTextReason, inputExpireDate, inputExpireTime, spinnerPriority, spinnerPerformers, spinnerObjects);
            }
        });

        final ImageButton imageButtonSave = (ImageButton) viewTitle.findViewById(R.id.imageButtonSave);
        imageButtonSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkFormAndAdd(activity, dialog, editTextReason, inputExpireDate, inputExpireTime, spinnerPriority, spinnerPerformers, spinnerObjects);
            }
        });

    }

    private void checkFormAndAdd(Activity activity, Dialog dialog, EditText editTextReason,
                                 TextView inputExpireDate, TextView inputExpireTime, Spinner spinnerPriority, Spinner spinnerPerformers, Spinner spinnerObjects)
    {
        if (!editTextReason.getText().toString().isEmpty() && !inputExpireDate.getText().toString().isEmpty() && !inputExpireTime.getText().toString().isEmpty())
        {
            String deadline = inputExpireDate.getText().toString() + " " + inputExpireTime.getText().toString();

            Date dateNow = new Date();
            Date deadlineDT = new Date();
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
            try
            {
                deadlineDT = format.parse(deadline);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            if (!dateNow.after(deadlineDT))
            {
                addTask(activity, editTextReason.getText().toString(), spinnerPriority.getSelectedItemPosition() + 1,
                        deadline, spinnerPerformers, spinnerObjects);

                activity.recreate();
                dialog.dismiss();
            }
            else
            {
                Toast toast = Toast.makeText(activity, R.string.errorTime, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }
        else
        {
            if (editTextReason.getText().toString().trim().isEmpty())
                editTextReason.setError(activity.getText(R.string.errorEmptyField));
            if (inputExpireDate.getText().toString().trim().isEmpty())
                inputExpireDate.setError(activity.getText(R.string.errorEmptyField));
            if (inputExpireTime.getText().toString().trim().isEmpty())
                inputExpireTime.setError(activity.getText(R.string.errorEmptyField));
        }

    }

    private ArrayAdapter arrayAdapter(Activity activity, ArrayList<String> list)
    {
        if (list.get(0).equals(activity.getText(R.string.all)))
            list.remove(0);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spinner_items, list);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_items);

        return adapter;
    }

    private void addTask(Activity activity, String reason, int priority, String deadline, Spinner spinnerPerformers, Spinner spinnerObjects)
    {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

        String object_id = null;
        String performer_id = "0";

        for (int j = 0; j < Config.objectsList.size(); j++)
            if (Config.objectsList.get(j).get(Config.TAG_OBJECT_NAME).equals(spinnerObjects.getSelectedItem()))
                object_id = Config.objectsList.get(j).get(Config.TAG_OBJECT_ID);

        if (spinnerPerformers.getSelectedItemPosition() == 0)
            performer_id = "0";
        else
            for (int j = 0; j < Config.users.size(); j++)
                if (Config.users.get(j).get(Config.TAG_USER).equals(spinnerPerformers.getSelectedItem().toString()))
                    performer_id = Config.users.get(j).get(Config.TAG_USER_ID);

        NewTaskAdd nta = new NewTaskAdd();
        nta.taskNewAdd(activity, auth_user_id, object_id, reason, String.valueOf(priority), deadline, performer_id);
    }
}
