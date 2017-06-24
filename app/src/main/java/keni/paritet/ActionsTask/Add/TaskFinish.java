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

public class TaskFinish
{
    public void finishTask(final Activity activity, final String id, final String auth_user_id)
    {
        class FinishTask extends AsyncTask<Void, Void, String>
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
                activity.recreate();
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.TASK_APP_ID, id);
                hashMap.put(Config.TAG_USER_ID, auth_user_id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.FINISH_TASK_URL, hashMap);

                return s;
            }
        }

        FinishTask ft = new FinishTask();
        ft.execute();
    }
}
