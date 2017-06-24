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

public class PriorityGet
{
    private String JSON_STRING;
    public void getPriority()
    {

        class GetPriority extends AsyncTask<Void, Void, String>
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
                loadPriority();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_PRIORITY_URL);
                return s;
            }
        }
        GetPriority gp = new GetPriority();
        gp.execute();
    }

    private void loadPriority()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String priority_id = jo.getString(Config.TAG_PRIORITY_ID);
                String priority_name = jo.getString(Config.TAG_PRIORITY_NAME);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_PRIORITY_ID, priority_id);
                apps.put(Config.TAG_PRIORITY_NAME, priority_name);

                Config.priorityList.add(apps);
            }

            for (int i = 0; i < Config.priorityList.size(); i++)
                Config.priority.add(Config.priorityList.get(i).get(Config.TAG_PRIORITY_NAME));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
