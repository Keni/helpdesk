package keni.paritet.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

import keni.paritet.ActionsTask.TasksFilter;
import keni.paritet.ActionsTaskDialog.FilterDialog;
import keni.paritet.Activitys.TaskInfoActivity;
import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.R;

/**
 * Created by Keni on 10.11.2016.
 */

public class AllTasksFragment extends android.support.v4.app.Fragment implements ListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener
{
    public static ListView list_all_tasks;

    private String JSON_STRING;

    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.listview_for_tasks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.allTasks);

        list_all_tasks = (ListView) getActivity().findViewById(R.id.listview);
        list_all_tasks.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                checkFilter();
            }
        });
    }

    @Override
    public void onRefresh()
    {
        checkFilter();
    }

    private void showInfoTask()
    {
        JSONObject jsonObject;
        final ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            for (int i =0; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);
                String app = jo.getString(Config.TASK_APP_ID);
                String obj = jo.getString(Config.TASK_OBJ);
                String create_dt = jo.getString(Config.TASK_CREATE_DATE);
                String reason = jo.getString(Config.TASK_REASON);
                String priority = jo.getString(Config.TASK_PRIORITY);
                String status = jo.getString(Config.TASK_STATUS);
                String expire_dt = jo.getString(Config.TASK_EXPIRE_DATE);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Date create_dt_date = format.parse(create_dt);
                String create_dt_parse_dt =  new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru")).format(create_dt_date);

                HashMap<String, String> tasks = new HashMap<>();
                tasks.put(Config.TASK_APP_ID, app);
                tasks.put(Config.TASK_OBJ, obj);
                tasks.put(Config.TASK_CREATE_DATE, create_dt_parse_dt);
                tasks.put(Config.TASK_REASON, reason);
                tasks.put(Config.TASK_PRIORITY, priority);
                tasks.put(Config.TASK_STATUS, status);
                tasks.put(Config.TASK_EXPIRE_DATE, expire_dt);
                list.add(tasks);
            }
        }
        catch (JSONException | ParseException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.items_on_listview_tasks,
                new String[]{Config.TASK_OBJ, Config.TASK_CREATE_DATE, Config.TASK_REASON},
                new int[]{R.id.textViewObj, R.id.textViewDate, R.id.textViewReason})
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                final TextView textViewStatus = (TextView) view.findViewById(R.id.textViewStatus);
                final ImageView imageViewTime = (ImageView) view.findViewById(R.id.imageViewTime);
                final ImageView imageViewPriority = (ImageView) view.findViewById(R.id.imageViewPriority);

                if (list.get(position).get(Config.TASK_PRIORITY).equals("3"))
                    imageViewPriority.setVisibility(View.VISIBLE);
                else
                    imageViewPriority.setVisibility(View.GONE);

                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                Date expire = null;
                try
                {
                    expire = format.parse(list.get(position).get(Config.TASK_EXPIRE_DATE));
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }

                Date dateNow = new Date();

                if (dateNow.after(expire))
                {
                    imageViewTime.setVisibility(View.VISIBLE);
                    imageViewTime.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.holo_red_light));
                }
                else if (expire.getTime() - (12 * 60 * 60 * 1000) < System.currentTimeMillis())
                {
                    imageViewTime.setVisibility(View.VISIBLE);
                    imageViewTime.setColorFilter(ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark));
                }
                else
                {
                    imageViewTime.setVisibility(View.GONE);
                }

                if (list.get(position).get(Config.TASK_STATUS).equals("1"))
                    textViewStatus.setVisibility(View.VISIBLE);
                else if (list.get(position).get(Config.TASK_STATUS).equals("3"))
                {
                    textViewStatus.setVisibility(View.VISIBLE);
                    textViewStatus.setText(R.string.statusInWork);
                    textViewStatus.setBackgroundColor(getResources().getColor(R.color.colorStatusInWork));
                }
                else
                    textViewStatus.setVisibility(View.GONE);

                return view;
            }
        };

        list_all_tasks.setAdapter(adapter);

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
                String s = rh.sendGetDoubleRequestParam(Config.GET_TASKS_URL, "", "");
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

    private void checkFilter()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Config.SHARED_PREF_FILTER, Context.MODE_PRIVATE);

        boolean filtered = sharedPreferences.getBoolean(Config.FILTERED, false);

        if (filtered)
        {
            String filtered_performer = sharedPreferences.getString(Config.FILTERED_PERFORMER, "");
            String filtered_object = sharedPreferences.getString(Config.FILTERED_OBJECT, "");
            String filtered_priority = sharedPreferences.getString(Config.FILTERED_PRIORITY, "");
            String filtered_status = sharedPreferences.getString(Config.FILTERED_STATUS, "");
            String filtered_dt0 = sharedPreferences.getString(Config.FILTERED_DT0, "");
            String filtered_dt1 = sharedPreferences.getString(Config.FILTERED_DT1, "");
            String filtered_sort = sharedPreferences.getString(Config.FILTERED_SORT, "");

            swipeRefreshLayout.setRefreshing(true);
            TasksFilter tf = new TasksFilter();
            tf.filterTasks(this, filtered_performer, filtered_object, filtered_priority, filtered_status, filtered_dt0, filtered_dt1, filtered_sort);
        }
        else
        {
            swipeRefreshLayout.setRefreshing(true);
            getJSON();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.filter:
                FilterDialog fd = new FilterDialog();
                fd.filterDialog(AllTasksFragment.this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
