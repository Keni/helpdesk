package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import keni.paritet.ActionsTask.Add.TaskTransfer;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 15.12.2016.
 */

public class TransferDialog
{
    public void transferDialog(final Activity activity, final String app_id, final String performer_id)
    {
        final AlertDialog.Builder dialogTransfer = new AlertDialog.Builder(activity);
        LinearLayout view = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_transfer_layout, null);

        final EditText inputComment = (EditText) view.findViewById(R.id.editTextTransferReason);

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < Config.users.size(); i++)
        {
            data.add(Config.users.get(i).get(Config.TAG_USER_FULL_NAME));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_items, data);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_items);
        final Spinner spinnerUsers = (Spinner) view.findViewById(R.id.spinnerTransfer);
        spinnerUsers.setAdapter(adapter);

        dialogTransfer.setView(view);
        dialogTransfer.setIcon(R.drawable.transfer_user);
        dialogTransfer.setTitle(R.string.transferTitle);

        dialogTransfer.setPositiveButton(R.string.buttonAppoint, null);

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
                if (!inputComment.getText().toString().trim().isEmpty())
                {
                    String comment = inputComment.getText().toString();
                    String to_user_id = performer_id;
                    for (int j = 0; j < Config.users.size(); j++)
                        if (Config.users.get(j).get(Config.TAG_USER_FULL_NAME).equals(spinnerUsers.getSelectedItem().toString()))
                            to_user_id = Config.users.get(j).get(Config.TAG_USER_ID);

                    if (!performer_id.equals(to_user_id))
                    {
                        TaskTransfer tt = new TaskTransfer();
                        tt.transferTask(activity, app_id, comment, performer_id, to_user_id);
                        activity.recreate();
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast toast = Toast.makeText(activity, R.string.errorTransferOnMe, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                }
                else
                {
                    inputComment.setError(activity.getText(R.string.errorEmptyField));
                }
            }
        });
    }
}
