package keni.paritet.ActionsTaskDialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import keni.paritet.ActionsTask.Update.TaskReportDelete;
import keni.paritet.R;

/**
 * Created by Keni on 14.12.2016.
 */

public class DeleteReportDialog
{
    public void deleteReportDialog(final Activity activity, final String report_id)
    {
        final AlertDialog.Builder dialogEditComment = new AlertDialog.Builder(activity);

        final TextView textView = new TextView(activity);
        textView.setTextSize(20);
        textView.setText(R.string.deleteMessage);

        dialogEditComment.setView(textView, 50, 25, 50, 0);
        dialogEditComment.setIcon(android.R.drawable.ic_delete);
        dialogEditComment.setTitle(R.string.delete);

        dialogEditComment.setPositiveButton(R.string.deleted, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                TaskReportDelete trd = new TaskReportDelete();
                trd.reportDeleteTask(activity, report_id);
                activity.recreate();
            }
        });

        dialogEditComment.setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        dialogEditComment.show();
    }
}
