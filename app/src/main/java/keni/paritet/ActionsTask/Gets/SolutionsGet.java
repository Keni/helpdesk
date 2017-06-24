package keni.paritet.ActionsTask.Gets;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;

/**
 * Created by Keni on 07.12.2016.
 */

public class SolutionsGet
{
    private String JSON_STRING;
    public void getSolutions()
    {

        class GetTypeOfWork extends AsyncTask<Void, Void, String>
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
                showSolutions();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.GET_SOLUTIONS_URL);
                return s;
            }
        }
        GetTypeOfWork gtow = new GetTypeOfWork();
        gtow.execute();
    }

    private void showSolutions()
    {
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i = 0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);

                String sid = jo.getString(Config.TAG_SOLUTION_ID);
                String solution = jo.getString(Config.TAG_SOLUTION_NAME);

                HashMap<String, String> apps = new HashMap<>();
                apps.put(Config.TAG_SOLUTION_ID, sid);
                apps.put(Config.TAG_SOLUTION_NAME, solution);
                Config.solution.add(apps);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }
}
