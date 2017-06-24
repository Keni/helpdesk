package keni.paritet.ActionsTask.Gets;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;

/**
 * Created by Keni on 04.12.2016.
 */

public class UsersGet
{
    private String JSON_STRING;
    public void getUsers()
    {

        class GetUsers extends AsyncTask<Void, Void, String>
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
                showUsers();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_USERS_URL);
                return s;
            }
        }
        GetUsers gu = new GetUsers();
        gu.execute();
    }

    private void showUsers()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String user_id = jo.getString(Config.TAG_USER_ID);
                String user = jo.getString(Config.TAG_USER);
                String full_name = jo.getString(Config.TAG_USER_FULL_NAME);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_USER_ID, user_id);
                apps.put(Config.TAG_USER, user);
                apps.put(Config.TAG_USER_FULL_NAME, full_name);
                Config.users.add(apps);
            }

            for (int i = 0; i < Config.users.size(); i++)
                Config.performers.add(Config.users.get(i).get(Config.TAG_USER));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
