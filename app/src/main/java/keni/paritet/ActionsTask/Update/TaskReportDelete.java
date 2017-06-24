package keni.paritet.ActionsTask.Update;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;

/**
 * Created by Keni on 14.12.2016.
 */

public class TaskReportDelete
{
    public void reportDeleteTask(final Activity activity, final String report_id)
    {
        class ReportDeleteTask extends AsyncTask<Void, Void, String>
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
                Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.REPORT_ID, report_id);
                hashMap.put(Config.TAG_USER_ID, auth_user_id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.REPORT_DELETE_URL, hashMap);

                return s;
            }
        }

        ReportDeleteTask rdt = new ReportDeleteTask();
        rdt.execute();
    }
}
