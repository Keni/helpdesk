package keni.paritet.ActionsTask.Add;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.R;

/**
 * Created by Keni on 07.12.2016.
 */

public class TaskReportAdd
{
    public void reportAddTask(final Activity activity, final String id, final String performer_id, final String comment, final String dt0, final String dt1, final String solution, final String act)
    {
        class ReportAddTask extends AsyncTask<Void, Void, String>
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
                SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.TASK_APP_ID, id);
                hashMap.put(Config.TAG_USER_ID, auth_user_id);
                hashMap.put(Config.REPORT_USER, performer_id);
                hashMap.put(Config.REPORT_COMMENT, comment);
                hashMap.put(Config.REPORT_DT0, dt0);
                hashMap.put(Config.REPORT_DT1, dt1);
                hashMap.put(Config.REPORT_SOLUTION, solution);
                hashMap.put(Config.REPORT_ACT, act);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.REPORT_ADD_URL, hashMap);

                return s;
            }
        }

        ReportAddTask rat = new ReportAddTask();
        rat.execute();
    }
}
