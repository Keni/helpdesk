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

public class ObjectsGet
{
    private String JSON_STRING;

    public void getObjects()
    {

        class GetObjects extends AsyncTask<Void, Void, String>
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
                loadObjects();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_OBJECTS_URL);
                return s;
            }
        }
        GetObjects go = new GetObjects();
        go.execute();
    }

    private void loadObjects()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String object_id = jo.getString(Config.TAG_OBJECT_ID);
                String object_name = jo.getString(Config.TAG_OBJECT_NAME);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_OBJECT_ID, object_id);
                apps.put(Config.TAG_OBJECT_NAME, object_name);

                Config.objectsList.add(apps);
            }

            for (int i = 0; i < Config.objectsList.size(); i++)
                Config.objects.add(Config.objectsList.get(i).get(Config.TAG_OBJECT_NAME));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
