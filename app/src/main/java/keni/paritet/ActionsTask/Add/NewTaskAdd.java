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
 * Created by Keni on 23.12.2016.
 */

public class NewTaskAdd
{
    public void taskNewAdd(final Activity activity, final String auth_user_id, final String task_object_id, final String task_reason, final String task_priority_id, final String task_deadline, final String performer_id)
    {
        class TaskNewAdd extends AsyncTask<Void, Void, String>
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
                hashMap.put(Config.TASK_CREATOR_ID, auth_user_id);
                hashMap.put(Config.TASK_OBJ, task_object_id);
                hashMap.put(Config.TASK_REASON, task_reason);
                hashMap.put(Config.TASK_PRIORITY, task_priority_id);
                hashMap.put(Config.TASK_EXPIRE_DATE, task_deadline);
                hashMap.put(Config.TASK_PERFORMER_ID, performer_id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.TASK_ADD_URL, hashMap);

                return s;
            }
        }

        TaskNewAdd tna = new TaskNewAdd();
        tna.execute();
    }
}
