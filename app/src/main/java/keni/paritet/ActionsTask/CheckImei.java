package keni.paritet.ActionsTask;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.HashMap;

import keni.paritet.Activitys.LoginActivity;
import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;

/**
 * Created by Keni on 21.06.2017.
 */

public class CheckImei
{
    private static final String KEY_IMEI = "IMEI";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_VERSION = "version";

    public void imeiCheck(final Activity activity, final String imei, final String token, final String version)
    {
        class ImeiCheck extends AsyncTask<Void, Void, String>
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
                if (s.trim().equals(Config.IMEI_SUCCESS))
                {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
                else
                {
                    Toast.makeText(activity.getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    activity.finish();
                }
            }

            @Override
            protected String doInBackground(Void... params)
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(KEY_IMEI, imei);
                hashMap.put(KEY_TOKEN, token);
                hashMap.put(KEY_VERSION, String.valueOf(version));

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(Config.CHECK_IMEI_URL, hashMap);

                return s;
            }
        }

        ImeiCheck ic = new ImeiCheck();
        ic.execute();
    }
}
