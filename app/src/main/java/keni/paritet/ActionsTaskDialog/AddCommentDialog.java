package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import keni.paritet.ActionsTask.Add.TaskCommentAdd;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 12.12.2016.
 */

public class AddCommentDialog extends DialogFragment
{
    public void addCommentDialog(final Activity activity, final String id, final String performer_id)
    {
        final AlertDialog.Builder dialogComment = new AlertDialog.Builder(activity);
        final EditText inputComment = new EditText(activity);
        inputComment.setHint(R.string.commentHint);
        inputComment.setFocusable(true);
        dialogComment.setView(inputComment, 50, 0, 50, 0);
        dialogComment.setIcon(R.drawable.ic_comment_black_24dp);
        dialogComment.setTitle(R.string.commentTitle);

        dialogComment.setPositiveButton(R.string.buttonAdd, null);

        dialogComment.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogComment.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!inputComment.getText().toString().trim().isEmpty())
                {
                    SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

                    String comment = inputComment.getText().toString();
                    TaskCommentAdd tca = new TaskCommentAdd();
                    tca.commentAddTask(activity, auth_user_id, id, performer_id, comment);
                    activity.recreate();
                    dialog.dismiss();
                }
                else
                {
                    inputComment.setError(activity.getText(R.string.errorEmptyField));
                }
            }
        });
    }
}
