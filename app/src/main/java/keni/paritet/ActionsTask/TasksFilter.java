package keni.paritet.ActionsTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.Fragments.AllTasksFragment;
import keni.paritet.R;

/**
 * Created by Keni on 20.12.2016.
 */

public class TasksFilter
{
    private String JSON_STRING;

    public void filterTasks(final Fragment fragment, final String performer_id, final String object_id, final String priority, final String status, final String dt0, final String dt1, final String sort)
    {

        class FilterTasks extends AsyncTask<Void, Void, String>
        {
            private ProgressDialog loading;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(fragment.getActivity(), fragment.getText(R.string.loading), fragment.getText(R.string.wait), false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                JSON_STRING = s;
                loading.dismiss();

                JSONObject jsonObject;
                final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                try
                {
                    jsonObject = new JSONObject(JSON_STRING);
                    JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

                    for (int i = 0; i < result.length(); i++)
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

                    ListAdapter adapter = new SimpleAdapter(fragment.getActivity(), list, R.layout.items_on_listview_tasks,
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
                                imageViewTime.setVisibility(View.VISIBLE);
                            else
                                imageViewTime.setVisibility(View.GONE);

                            if (list.get(position).get(Config.TASK_STATUS).equals("1"))
                                textViewStatus.setVisibility(View.VISIBLE);
                            else
                                textViewStatus.setVisibility(View.GONE);

                            return view;
                        }
                    };

                    AllTasksFragment.list_all_tasks.setAdapter(adapter);

                }
                catch (JSONException | ParseException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetFilterRequestParam(Config.GET_TASKS_FILTERED, performer_id, object_id, priority, status, dt0, dt1, sort);
                return s;
            }
        }
        FilterTasks ft = new FilterTasks();
        ft.execute();
    }
}
