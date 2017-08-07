package keni.paritet.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.R;
import keni.paritet.Activitys.TaskInfoActivity;

/**
 * Created by Keni on 14.11.2016.
 */

public class AcceptTasksFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener
{
    private ListView list_accept_tasks;

    private String JSON_STRING;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.listview_for_tasks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(R.string.confirmationTasks);

        list_accept_tasks = (ListView) getActivity().findViewById(R.id.listview);
        list_accept_tasks.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                swipeRefreshLayout.setRefreshing(true);
                getJSON();
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getJSON();
    }

    @Override
    public void onRefresh()
    {
        getJSON();
    }

    private void showInfoTask()
    {
        JSONObject jsonObject;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i =0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);
                String app_id = jo.getString(Config.TASK_APP_ID);
                String obj = jo.getString(Config.TASK_OBJ);
                String create_dt = jo.getString(Config.TASK_CREATE_DATE);
                String reason = jo.getString(Config.TASK_REASON);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Date create_dt_date = format.parse(create_dt);
                String create_dt_parse_dt =  new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru")).format(create_dt_date);

                HashMap<String, String> tasks = new HashMap<>();
                tasks.put(Config.TASK_APP_ID, app_id);
                tasks.put(Config.TASK_OBJ, obj);
                tasks.put(Config.TASK_CREATE_DATE, create_dt_parse_dt);
                tasks.put(Config.TASK_REASON, reason);
                list.add(tasks);
            }
        }
        catch (JSONException | ParseException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.items_on_listview_tasks,
                new String[]{Config.TASK_OBJ, Config.TASK_CREATE_DATE, Config.TASK_REASON},
                new int[]{R.id.textViewObj, R.id.textViewDate, R.id.textViewReason});

        list_accept_tasks.setAdapter(adapter);

        swipeRefreshLayout.setRefreshing(false);
    }

    private void getJSON()
    {
        swipeRefreshLayout.setRefreshing(true);

        class GetJSON extends AsyncTask<Void, Void, String>
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
                showInfoTask();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetDoubleRequestParam(Config.GET_TASKS_URL, "", Config.ACCEPT_STATUS);
                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getActivity(), TaskInfoActivity.class);
        HashMap map = (HashMap)parent.getItemAtPosition(position);
        String addId = map.get(Config.TASK_APP_ID).toString();

        intent.putExtra(Config.TASK_APP_ID, addId);
        startActivity(intent);
    }
}

