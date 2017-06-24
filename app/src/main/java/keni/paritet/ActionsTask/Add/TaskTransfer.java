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
 * Created by Keni on 05.12.2016.
 */

public class TaskTransfer
{
    public void transferTask(final Activity activity, final String id, final String comment, final String user_id, final String to_user_id)
    {
        class TransferTask extends AsyncTask<Void, Void, String>
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
            protected String doInBackground(Void... Params)
            {
                SharedPreferences sharedPreferences = activity.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.TASK_APP_ID, id);
                hashMap.put(Config.REPORT_COMMENT, comment);
                hashMap.put(Config.auth_user_id, auth_user_id);
                hashMap.put(Config.TAG_USER_ID, user_id);
                hashMap.put(Config.REPORT_TO_USER_ID, to_user_id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.TRANSFER_TASK_URL, hashMap);

                return s;
            }
        }

        TransferTask tt = new TransferTask();
        tt.execute();
    }
}
