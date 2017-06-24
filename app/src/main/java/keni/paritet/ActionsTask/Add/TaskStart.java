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

public class TaskStart
{

    public void startTask(final Activity activity, final String id, final String performer_id)
    {
        class StartTask extends AsyncTask<Void, Void, String>
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
            protected String doInBackground(Void... Params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(Config.TASK_APP_ID, id);
                hashMap.put(Config.TASK_PERFORMER_ID, performer_id);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.START_TASK_URL, hashMap);

                return s;
            }
        }

        StartTask st = new StartTask();
        st.execute();
    }

}
