package keni.paritet.Activitys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import keni.paritet.ActionsTask.Gets.ObjectsGet;
import keni.paritet.ActionsTask.Gets.PriorityGet;
import keni.paritet.ActionsTask.Gets.SolutionsGet;
import keni.paritet.ActionsTask.Gets.StatusGet;
import keni.paritet.ActionsTask.Gets.UsersGet;
import keni.paritet.ActionsTaskDialog.AddNewTaskDialog;
import keni.paritet.Config.Config;
import keni.paritet.Fragments.AcceptTasksFragment;
import keni.paritet.Fragments.AllTasksFragment;
import keni.paritet.Fragments.InWorkTasksFragment;
import keni.paritet.Fragments.MyTasksFragment;
import keni.paritet.R;
import keni.paritet.Tools.NotificationUtils;

public class MainActivity extends AppCompatActivity
{
    private DrawerLayout drawerLayout;

    private static final String TAG = MainActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String auth_full_name = sharedPreferences.getString(Config.auth_full_name, "Не доступен");
        String auth_user_avatar = sharedPreferences.getString(Config.auth_user_avatar, "Не доступен");


            if (Config.priorityList.isEmpty() && Config.objectsList.isEmpty() && Config.statusList.isEmpty() && Config.solution.isEmpty())
            {
                PriorityGet pg = new PriorityGet();
                pg.getPriority();

                ObjectsGet og = new ObjectsGet();
                og.getObjects();

                StatusGet sg = new StatusGet();
                sg.getStatus();

                SolutionsGet solutionsGet = new SolutionsGet();
                solutionsGet.getSolutions();
            }

            if (savedInstanceState == null)
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContent, new MyTasksFragment()).commit();
            }

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AddNewTaskDialog antd = new AddNewTaskDialog();
                    antd.addNewTaskDialog(MainActivity.this);
                }
            });

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setTitle(R.string.myTasks);
            setSupportActionBar(toolbar);

            if (Config.users.isEmpty())
            {
                UsersGet ug = new UsersGet();
                ug.getUsers();
            }

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
            drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
            setupDrawerContent(navigationView);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawerLayout.setDrawerListener(toggle);
            toggle.syncState();

            View header = navigationView.getHeaderView(0);
            TextView textViewFullName = (TextView) header.findViewById(R.id.textViewFullName);
            textViewFullName.setText(auth_full_name);

            CircleImageView imageViewAvatar = (CircleImageView) header.findViewById(R.id.imageViewAvatar);

            if (!auth_user_avatar.isEmpty())
                Picasso.with(MainActivity.this).load(auth_user_avatar).into(imageViewAvatar);
            else
                imageViewAvatar.setImageResource(R.drawable.user);

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE))
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                }
            }
        };
    }

    private void setupDrawerContent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    public void selectDrawerItem(MenuItem item)
    {
        Fragment fragment = null;
        Class fragmentClass;

        switch (item.getItemId())
        {
            case R.id.list_my_tasks:
                fragmentClass = MyTasksFragment.class;
                break;

            case R.id.list_all_tasks:
                fragmentClass = AllTasksFragment.class;
                break;

            case R.id.list_in_work_tasks:
                fragmentClass = InWorkTasksFragment.class;
                break;

            case R.id.list_confirmation_tasks:
                fragmentClass = AcceptTasksFragment.class;
                break;

            case R.id.exit:
                logout();

            default:
                fragmentClass = MyTasksFragment.class;
        }

        try
        {
            fragment = (Fragment) fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();

        item.setChecked(true);

        if (item.getItemId() != R.id.exit)
            setTitle(item.getTitle());

        drawerLayout.closeDrawers();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    private void logout()
    {
        AlertDialog.Builder exitDialogBuilder = new AlertDialog.Builder(this);
        exitDialogBuilder.setMessage(getResources().getString(R.string.exitMessage));
        exitDialogBuilder.setPositiveButton(R.string.buttonYes, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean(Config.LOGGED_SHARED_PREF, false);

                editor.putString(Config.auth_user_id, "");
                editor.putString(Config.auth_full_name, "");
                editor.putString(Config.auth_user_avatar, "");

                editor.apply();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        exitDialogBuilder.setNegativeButton(R.string.buttonNo, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
            }
        });

        AlertDialog exitDialog = exitDialogBuilder.create();
        exitDialog.show();
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
