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
 * Created by Keni on 13.12.2016.
 */

public class TaskReportEdit
{
    public void reportEditTask(final Activity activity, final String app_id, final String report_id, final String user_id, final String comment, final String dt0, final String dt1, final String solution_id, final String act)
    {
        class ReportEditTask extends AsyncTask<Void, Void, String>
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
                hashMap.put(Config.TASK_APP_ID, app_id);
                hashMap.put(Config.REPORT_ID, report_id);
                hashMap.put(Config.auth_user_id, auth_user_id);
                hashMap.put(Config.REPORT_USER, user_id);
                hashMap.put(Config.REPORT_COMMENT, comment);
                hashMap.put(Config.REPORT_DT0, dt0);
                hashMap.put(Config.REPORT_DT1, dt1);
                hashMap.put(Config.REPORT_SOLUTION_ID, solution_id);
                hashMap.put(Config.REPORT_ACT, act);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.REPORT_UPDATE_URL, hashMap);

                return s;
            }
        }

        ReportEditTask ret = new ReportEditTask();
        ret.execute();
    }
}
