package keni.paritet.ActionsTask.Add;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.R;

/**
 * Created by Keni on 04.12.2016.
 */

public class TaskCommentAdd
{
    public void commentAddTask(final Activity activity, final String auth_user_id, final String id, final String performer_id, final String comment)
    {
        class CommentAddTask extends AsyncTask<Void, Void, String>
        {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(activity, activity.getText(R.string.loading), activity.getText(R.string.wait), false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.TASK_APP_ID, id);
                hashMap.put(Config.TAG_USER_ID, auth_user_id);
                hashMap.put(Config.TASK_PERFORMER_ID, performer_id);
                hashMap.put(Config.REPORT_COMMENT, comment);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.COMMENT_ADD_URL, hashMap);

                return s;
            }
        }

        CommentAddTask cat = new CommentAddTask();
        cat.execute();
    }
}
