package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import keni.paritet.ActionsTask.Date.SetDate;
import keni.paritet.ActionsTask.Date.SetTime;
import keni.paritet.ActionsTask.Update.TaskRenew;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 26.12.2016.
 */

public class RenewTaskDialog
{
    public void renewTaskDialog(final Activity activity, final String app_id, final String performer_id)
    {
        final AlertDialog.Builder dialogTransfer = new AlertDialog.Builder(activity);

        LinearLayout view = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_renew, null);

        final EditText inputComment = (EditText) view.findViewById(R.id.editTextComment);

        final TextView inputDate = (TextView) view.findViewById(R.id.textViewRenewDeadLineDate);
        final TextView inputTime = (TextView) view.findViewById(R.id.textViewRenewDeadLineTime);

        inputDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetDate sd = new SetDate();
                sd.setDate(activity, inputDate);
            }
        });

        inputTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SetTime st = new SetTime();
                st.setTime(activity, inputTime);
            }
        });


        dialogTransfer.setView(view);
        dialogTransfer.setIcon(R.drawable.ic_replay_black_24dp);
        dialogTransfer.setTitle(R.string.renewTitle);

        dialogTransfer.setPositiveButton(R.string.buttonAccept, null);

        dialogTransfer.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogTransfer.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!inputComment.getText().toString().trim().isEmpty() && !inputDate.getText().toString().trim().isEmpty() && !inputTime.getText().toString().trim().isEmpty())
                {
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

                    String comment = inputComment.getText().toString();
                    String deadline = inputDate.getText().toString() + " " + inputTime.getText().toString();

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
                        TaskRenew tr = new TaskRenew();
                        tr.pauseTask(activity, auth_user_id, performer_id, app_id, comment, deadline);

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
                    if (inputComment.getText().toString().isEmpty())
                        inputComment.setError(activity.getText(R.string.errorEmptyField));

                    if (inputDate.getText().toString().isEmpty())
                        inputDate.setError(activity.getText(R.string.errorEmptyField));

                    if (inputTime.getText().toString().isEmpty())
                        inputTime.setError(activity.getText(R.string.errorEmptyField));

                    Toast toast = Toast.makeText(activity, R.string.errorEmptyFieldToast, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
        });
    }
}
