package keni.paritet.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

import keni.paritet.ActionsTask.Add.TaskFinish;
import keni.paritet.ActionsTask.Add.TaskStart;
import keni.paritet.ActionsTaskDialog.AddCommentDialog;
import keni.paritet.ActionsTaskDialog.AddReportDialog;
import keni.paritet.ActionsTaskDialog.DeleteReportDialog;
import keni.paritet.ActionsTaskDialog.EditCommentDialog;
import keni.paritet.ActionsTaskDialog.EditReportDialog;
import keni.paritet.ActionsTaskDialog.RenewTaskDialog;
import keni.paritet.ActionsTaskDialog.TransferDialog;
import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.R;
import keni.paritet.Tools.TextViewResizable;

/**
 * Created by Keni on 15.11.2016.
 */

public class TaskInfoActivity extends AppCompatActivity
{
    private ListView list_of_reports;

    private String JSON_STRING;
    private String app_id;
    private String performer_id;
    private String status;
    private int solution_img = android.R.color.transparent;

    private TextView textViewObj;
    private TextView textViewReason;
    private TextView textViewCreateDate;
    private TextView textViewDeadLine;
    private TextView textViewPriority;
    private TextView textViewStatus;
    private TextView textViewPerformer;

    private ImageButton imageButtonRenewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_for_info_of_task);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String auth_user_id = sharedPreferences.getString(Config.auth_user_id, "Недоступен");

        Intent intent = getIntent();
        app_id = intent.getStringExtra(Config.TASK_APP_ID);

        initToolBars();

        list_of_reports = (ListView) findViewById(R.id.listview);

        textViewObj = (TextView) findViewById(R.id.textViewObj);
        textViewReason = (TextView) findViewById(R.id.textViewReason);
        textViewCreateDate = (TextView) findViewById(R.id.textViewCreateDate);
        textViewDeadLine = (TextView) findViewById(R.id.textViewDeadLine);
        textViewPriority = (TextView) findViewById(R.id.textViewPriority);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewPerformer = (TextView) findViewById(R.id.textViewPerformer);

        ImageButton imageButtonStartTask = (ImageButton) findViewById(R.id.imageButtonStartTask);
        imageButtonStartTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TaskStart ts = new TaskStart();
                ts.startTask(TaskInfoActivity.this, app_id, performer_id);
            }
        });

        ImageButton imageButtonFinishTask = (ImageButton) findViewById(R.id.imageButtonFinishTask);
        imageButtonFinishTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TaskFinish tf = new TaskFinish();
                tf.finishTask(TaskInfoActivity.this, app_id, auth_user_id);
            }
        });

        imageButtonRenewTask = (ImageButton) findViewById(R.id.imageButtonRenew);
        imageButtonRenewTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                RenewTaskDialog rtd = new RenewTaskDialog();
                rtd.renewTaskDialog(TaskInfoActivity.this, app_id, performer_id);
            }
        });

        ImageButton imageButtonAddReport = (ImageButton) findViewById(R.id.imageButtonAddReport);
        imageButtonAddReport.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddReportDialog ard = new AddReportDialog();
                ard.addReportDialog(TaskInfoActivity.this, app_id);
            }
        });

        final ImageButton imageButtonAddComment = (ImageButton) findViewById(R.id.imageButtonAddComment);
        imageButtonAddComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AddCommentDialog acd = new AddCommentDialog();
                acd.addCommentDialog(TaskInfoActivity.this, app_id, performer_id);
            }
        });

        ImageButton imageButtonTransfer = (ImageButton) findViewById(R.id.imageButtonTransfer);
        imageButtonTransfer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TransferDialog td = new TransferDialog();
                td.transferDialog(TaskInfoActivity.this, app_id, performer_id, status);
            }
        });

        getJSON();
    }

    private void showInfoReport()
    {
        JSONObject jsonObject;
        final ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        try
        {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);

            JSONObject jo_app = result.getJSONObject(0);
            String object = jo_app.getString(Config.TASK_OBJ);
            performer_id = jo_app.getString(Config.TASK_PERFORMER_ID);
            String performer = jo_app.getString(Config.TASK_PERFORMER);
            String priority = jo_app.getString(Config.TASK_PRIORITY);
            String create_dt = jo_app.getString(Config.TASK_CREATE_DATE);
            String expire_dt = jo_app.getString(Config.TASK_EXPIRE_DATE);
            String reason = jo_app.getString(Config.TASK_REASON);
            status = jo_app.getString(Config.TASK_STATUS);

            if (status.equalsIgnoreCase(getResources().getString(R.string.finish)) || status.equalsIgnoreCase(getResources().getString(R.string.accept)))
                imageButtonRenewTask.setVisibility(View.VISIBLE);
            else
                imageButtonRenewTask.setVisibility(View.GONE);

            if (priority.equalsIgnoreCase(getText(R.string.high).toString()))
            {
                textViewPriority.setTypeface(null, Typeface.BOLD);
                textViewPriority.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            Date expire = format.parse(expire_dt);
            String expire_dt_parse = new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru")).format(expire);
            Date create = format.parse(create_dt);
            String create_dt_parse =  new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru")).format(create);
            Date dateNow = new Date();

            if (dateNow.after(expire))
                textViewDeadLine.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            else if (expire.getTime() - (12 * 60 * 60 * 1000) < System.currentTimeMillis())
                textViewDeadLine.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));

            for (int i = 1; i < result.length(); i++)
            {
                JSONObject jo = result.getJSONObject(i);
                String report_id = jo.getString(Config.REPORT_ID);
                String type = jo.getString(Config.REPORT_TYPE);
                String user = jo.getString(Config.REPORT_USER);
                String comment = jo.getString(Config.REPORT_COMMENT);
                String report_create_dt = jo.getString(Config.REPORT_CREATE_DT);
                String report_status = jo.getString(Config.REPORT_STATUS);
                String dt0 = jo.getString(Config.REPORT_DT0);
                String dt1 = jo.getString(Config.REPORT_DT1);
                String dt0Date = jo.getString(Config.REPORT_DT0_DATE);
                String dt1Date = jo.getString(Config.REPORT_DT1_DATE);
                String dt0Time = jo.getString(Config.REPORT_DT0_TIME);
                String dt1Time = jo.getString(Config.REPORT_DT1_TIME);
                String solution_id = jo.getString(Config.REPORT_SOLUTION);
                String act = jo.getString(Config.REPORT_ACT);
                String avatar = jo.getString(Config.REPORT_AVATAR);

                Date report_create_dt_date = format.parse(report_create_dt);
                String report_create_dt_parse =  new SimpleDateFormat("dd MMMM, HH:mm", new Locale("ru")).format(report_create_dt_date);

                String dt0_parse = "";
                String dt1_parse = "";
                if (!dt0.isEmpty())
                {
                    Date dt0_date = format.parse(dt0);
                    dt0_parse = new SimpleDateFormat("dd MMMM, HH:mm - ", new Locale("ru")).format(dt0_date);

                    if (!dt1.isEmpty())
                    {
                        Date dt1_date = format.parse(dt1);
                        dt1_parse = new SimpleDateFormat("HH:mm", new Locale("ru")).format(dt1_date);
                    }
                }

                if (solution_id.equals("0"))
                    solution_img = android.R.color.transparent;
                if (solution_id.equals("1"))
                    solution_img = R.drawable.ic_directions_car_black_24dp;
                if (solution_id.equals("2"))
                    solution_img = R.drawable.ic_computer_black_24dp;
                if (solution_id.equals("3"))
                    solution_img = R.drawable.ic_phone_black_24dp;
                if (solution_id.equals("4"))
                    solution_img = R.drawable.ic_assignment_ind_black_24dp;
                if (solution_id.equals("5"))
                    solution_img = R.drawable.ic_autorenew_black_24dp;
                if (solution_id.equals("6"))
                    solution_img = R.drawable.source_code;
                if (solution_id.equals("7"))
                    solution_img = R.drawable.bill;

                HashMap<String, Object> tasks = new HashMap<>();
                tasks.put(Config.REPORT_ID, report_id);
                tasks.put(Config.REPORT_TYPE, type);
                tasks.put(Config.REPORT_USER, user);
                tasks.put(Config.REPORT_STATUS, report_status);
                tasks.put(Config.REPORT_COMMENT, comment);
                tasks.put(Config.REPORT_CREATE_DT, report_create_dt_parse);
                tasks.put(Config.REPORT_DT0, dt0_parse);
                tasks.put(Config.REPORT_DT1, dt1_parse);
                tasks.put(Config.REPORT_DT0_DATE, dt0Date);
                tasks.put(Config.REPORT_DT1_DATE, dt1Date);
                tasks.put(Config.REPORT_DT0_TIME, dt0Time);
                tasks.put(Config.REPORT_DT1_TIME, dt1Time);
                tasks.put(Config.REPORT_SOLUTION_ID, solution_id);
                tasks.put(Config.REPORT_SOLUTION_IMG, solution_img);
                tasks.put(Config.REPORT_ACT, act);
                tasks.put(Config.REPORT_AVATAR, avatar);

                list.add(tasks);

            }

            textViewReason.setText(reason);
            if (reason.length() > 150)
                TextViewResizable.makeTextViewResizable(textViewReason, 3, getText(R.string.showText).toString(), true);

            textViewObj.setText(object);
            textViewCreateDate.setText(create_dt_parse);
            textViewDeadLine.setText(expire_dt_parse);
            textViewPriority.setText(priority);
            textViewPerformer.setText(performer);
            textViewStatus.setText(status);

        }
        catch (JSONException | ParseException e)
        {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(this, list, R.layout.items_on_listview_for_info_of_task,
                new String[]{Config.REPORT_TYPE, Config.REPORT_USER, Config.REPORT_COMMENT, Config.REPORT_CREATE_DT, Config.REPORT_DT0, Config.REPORT_DT1, Config.REPORT_SOLUTION_IMG},
                new int[]{R.id.textViewType, R.id.textViewUser, R.id.textViewComment, R.id.textViewCreateDate, R.id.textViewDT0, R.id.textViewDT1, R.id.imageViewSolution})
        {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent)
            {
                View view = super.getView(position, convertView, parent);

                ImageView act = (ImageView) view.findViewById(R.id.imageViewAct);

                if (list.get(position).get(Config.REPORT_ACT).equals("1"))
                    act.setVisibility(View.VISIBLE);
                else
                    act.setVisibility(View.GONE);

                ImageView avatar = (ImageView) view.findViewById(R.id.imageViewUser);
                if (list.get(position).get(Config.REPORT_AVATAR).equals("null"))
                {
                    avatar.setImageResource(R.drawable.user);
                }
                else
                {
                    Picasso.with(TaskInfoActivity.this).load(list.get(position).get(Config.REPORT_AVATAR).toString()).into(avatar);
                }

                final TextView textViewType = (TextView) view.findViewById(R.id.textViewType);
                final TextView textViewPerformer = (TextView) view.findViewById(R.id.textViewUser);
                final TextView textViewComment = (TextView) view.findViewById(R.id.textViewComment);
                final TextView textViewDT0 = (TextView) view.findViewById(R.id.textViewDT0);
                final TextView textViewDT1 = (TextView) view.findViewById(R.id.textViewDT1);

                if (!list.get(position).get(Config.REPORT_DT0).toString().isEmpty())
                {
                    textViewDT0.setVisibility(View.VISIBLE);
                    textViewDT1.setVisibility(View.VISIBLE);
                }
                else
                {
                    textViewDT0.setVisibility(View.GONE);
                    textViewDT1.setVisibility(View.GONE);
                }

                final ImageButton imageButtonStopReport = (ImageButton) view.findViewById(R.id.imageButtonStopReport);

                if (list.get(position).get(Config.REPORT_STATUS).equals("3")) {
                    imageButtonStopReport.setVisibility(View.VISIBLE);

                    imageButtonStopReport.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            String dt1DateNow = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
                            String dt1TimeNow = new SimpleDateFormat("HH:mm").format(new Date());

                            String user_id = null;
                            String report_id = list.get(position).get(Config.REPORT_ID).toString();
                            String comment = textViewComment.getText().toString();
                            String dt0Date = list.get(position).get(Config.REPORT_DT0_DATE).toString();
                            String dt0Time = list.get(position).get(Config.REPORT_DT0_TIME).toString();
                            String solution_id = list.get(position).get(Config.REPORT_SOLUTION_ID).toString();
                            String act = list.get(position).get(Config.REPORT_ACT).toString();

                            for (int i = 0; i < Config.users.size(); i++)
                                if (Config.users.get(i).get(Config.TAG_USER_FULL_NAME).equals(textViewPerformer.getText().toString()))
                                    user_id = Config.users.get(i).get(Config.TAG_USER_ID);

                            EditReportDialog erd = new EditReportDialog();
                            erd.editReportDialog(TaskInfoActivity.this, app_id, report_id, user_id, comment, dt0Date, dt1DateNow, dt0Time, dt1TimeNow, solution_id, act);

                        }
                    });
                }
                else
                    imageButtonStopReport.setVisibility(View.GONE);

                final ImageButton imageButtonMenu = (ImageButton) view.findViewById(R.id.imageButtonMenu);

                if (textViewType.getText().equals(getText(R.string.justComment)) || textViewType.getText().equals(getText(R.string.report)))
                {
                    imageButtonMenu.setVisibility(View.VISIBLE);

                    final PopupMenu popupMenu = new PopupMenu(TaskInfoActivity.this, view.findViewById(R.id.imageButtonMenu));

                    popupMenu.getMenuInflater().inflate(R.menu.itemtaskmenu, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                    {
                        @Override
                        public boolean onMenuItemClick(MenuItem item)
                        {
                            String report_id = list.get(position).get(Config.REPORT_ID).toString();

                            switch (item.getItemId())
                            {
                                case R.id.edit:
                                    String comment = textViewComment.getText().toString();

                                    if (textViewType.getText().equals(getText(R.string.justComment)))
                                    {
                                        EditCommentDialog ecd = new EditCommentDialog();
                                        ecd.editCommentDialog(TaskInfoActivity.this, report_id, comment);
                                    }
                                    if (textViewType.getText().equals(getText(R.string.report))) {
                                        String user_id = null;
                                        String dt0Date = list.get(position).get(Config.REPORT_DT0_DATE).toString();
                                        String dt1Date = list.get(position).get(Config.REPORT_DT1_DATE).toString();
                                        String dt0Time = list.get(position).get(Config.REPORT_DT0_TIME).toString();
                                        String dt1Time = list.get(position).get(Config.REPORT_DT1_TIME).toString();
                                        String solution_id = list.get(position).get(Config.REPORT_SOLUTION_ID).toString();
                                        String act = list.get(position).get(Config.REPORT_ACT).toString();

                                        for (int i = 0; i < Config.users.size(); i++)
                                            if (Config.users.get(i).get(Config.TAG_USER_FULL_NAME).equals(textViewPerformer.getText().toString()))
                                                user_id = Config.users.get(i).get(Config.TAG_USER_ID);

                                        EditReportDialog erd = new EditReportDialog();
                                        erd.editReportDialog(TaskInfoActivity.this, app_id, report_id, user_id, comment, dt0Date, dt1Date, dt0Time, dt1Time, solution_id, act);
                                    }
                                    break;

                                case R.id.delete:
                                    DeleteReportDialog drd = new DeleteReportDialog();
                                    drd.deleteReportDialog(TaskInfoActivity.this, report_id);
                                    break;
                            }

                            return true;
                        }
                    });

                    imageButtonMenu.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            popupMenu.show();
                        }
                    });

                }
                else
                {
                    imageButtonMenu.setVisibility(View.INVISIBLE);
                }

                return view;
            }
        };

        list_of_reports.setAdapter(adapter);


    }


    public void getJSON()
    {
        class GetJSON extends AsyncTask<Void, Void, String>
        {
            private ProgressDialog loading;
            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                loading = ProgressDialog.show(TaskInfoActivity.this, getText(R.string.loading), getText(R.string.wait), false, false);
            }

            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showInfoReport();
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.GET_TASK_INFO_URL, app_id);
                return s;
            }
        }

        GetJSON gj = new GetJSON();
        gj.execute();
    }

    private void initToolBars()
    {
        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarTop);
        setTitle(getText(R.string.taskID) + app_id);

        toolbarTop.setNavigationIcon(R.drawable.ic_back);
        toolbarTop.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
