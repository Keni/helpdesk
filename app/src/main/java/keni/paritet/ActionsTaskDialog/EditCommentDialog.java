package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.EditText;

import keni.paritet.ActionsTask.Update.TaskCommentEdit;
import keni.paritet.R;

/**
 * Created by Keni on 12.12.2016.
 */

public class EditCommentDialog
{
    public void editCommentDialog(final Activity activity, final String report_id, String comment)
    {
        final AlertDialog.Builder dialogEditComment = new AlertDialog.Builder(activity);
        final EditText inputComment = new EditText(activity);
        inputComment.setText(comment);
        inputComment.setSelection(inputComment.getText().length());
        dialogEditComment.setView(inputComment, 50, 0, 50, 0);
        dialogEditComment.setIcon(R.drawable.ic_comment_black_24dp);
        dialogEditComment.setTitle(R.string.commentTitle);

        dialogEditComment.setPositiveButton(R.string.buttonAccept, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                String comment = inputComment.getText().toString();
                TaskCommentEdit tce = new TaskCommentEdit();
                tce.commentEditTask(activity, report_id, comment);
                activity.recreate();
            }
        });

        dialogEditComment.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        final AlertDialog dialog = dialogEditComment.create();

        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
}
