package keni.paritet.ActionsTask.Gets;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;

/**
 * Created by Keni on 17.12.2016.
 */

public class StatusGet
{
    private String JSON_STRING;

    public void getStatus()
    {

        class GetStatus extends AsyncTask<Void, Void, String>
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
                JSON_STRING = s;
                loadStatus();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_STATUS_URL);
                return s;
            }
        }
        GetStatus gs = new GetStatus();
        gs.execute();
    }

    private void loadStatus()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String status_id = jo.getString(Config.TAG_STATUS_ID);
                String status_name = jo.getString(Config.TAG_STATUS_NAME);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_STATUS_ID, status_id);
                apps.put(Config.TAG_STATUS_NAME, status_name);

                Config.statusList.add(apps);
            }

            for (int i = 0; i < Config.statusList.size(); i++)
                Config.status.add(Config.statusList.get(i).get(Config.TAG_STATUS_NAME));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
