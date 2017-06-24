package keni.paritet.ActionsTask.Update;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;

/**
 * Created by Keni on 26.12.2016.
 */

public class TaskRenew
{
    public void pauseTask(final Activity activity, final String auth_user_id, final String performer_id, final String task_id, final String task_renew_comment, final String task_renew_dt)
    {
        class PauseTask extends AsyncTask<Void, Void, String>
        {
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                activity.recreate();
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.TASK_APP_ID, task_id);
                hashMap.put(Config.TAG_USER_ID, auth_user_id);
                hashMap.put(Config.TASK_PERFORMER_ID, performer_id);
                hashMap.put(Config.REPORT_COMMENT, task_renew_comment);
                hashMap.put(Config.TASK_EXPIRE_DATE, task_renew_dt);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.RENEW_TASK_URL, hashMap);

                return s;
            }
        }

        PauseTask pt = new PauseTask();
        pt.execute();
    }
}
