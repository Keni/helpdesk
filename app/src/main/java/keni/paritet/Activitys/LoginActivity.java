package keni.paritet.Activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import keni.paritet.Config.Config;
import keni.paritet.Config.RequestHandler;
import keni.paritet.R;

public class LoginActivity extends AppCompatActivity
{
    private AutoCompleteTextView editTextLogin;
    private EditText editTextPassword;

    private String login;

    private String auth_user_id, auth_full_name, auth_user_avatar;

    private ProgressDialog loading;

    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IMEI = "IMEI";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.sibparitet));
        setSupportActionBar(toolbar);

        editTextLogin = (AutoCompleteTextView) findViewById(R.id.login);
        editTextPassword = (EditText) findViewById(R.id.password);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                  userAuth();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        boolean logged = sharedPreferences.getBoolean(Config.LOGGED_SHARED_PREF, false);

        if (logged)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void userAuth()
    {
        login = editTextLogin.getText().toString();
        final String password = editTextPassword.getText().toString();
        final TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        if (!login.isEmpty() && !password.isEmpty())
        {
            loading = ProgressDialog.show(LoginActivity.this, getText(R.string.loading), getText(R.string.wait), false, false);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response)
                        {
                            if(response.trim().equals(Config.LOGIN_SUCCESS))
                            {
                                getProfile();
                            }
                            else
                            {
                                loading.dismiss();
                                Toast toast = Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                                toast.show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            loading.dismiss();
                            Toast toast = Toast.makeText(LoginActivity.this, getText(R.string.errorMySQL), Toast.LENGTH_SHORT );
                            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    Map<String,String> map = new HashMap<String,String>();
                    map.put(KEY_LOGIN, login);
                    map.put(KEY_PASSWORD, password);
                    map.put(KEY_IMEI, mngr.getDeviceId());
                    return map;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
        else
        {
            if (login.isEmpty())
                editTextLogin.setError(getText(R.string.errorEmptyField));
            if (password.isEmpty())
                editTextPassword.setError(getText(R.string.errorEmptyField));
        }
    }

    private void getProfile()
    {
        class GetProfile extends AsyncTask<Void, Void, String>
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
                loadInfo(s);
            }

            @Override
            protected String doInBackground(Void... params)
            {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(Config.GET_PARAMS_URL, login);
                return s;
            }
        }
        GetProfile gp = new GetProfile();
        gp.execute();
    }

    private void loadInfo(String json)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            auth_user_id = c.getString(Config.TAG_USER_ID);
            auth_full_name = c.getString(Config.TAG_USER_FULL_NAME);
            auth_user_avatar = c.getString(Config.TAG_USER_AVATAR);

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Config.LOGGED_SHARED_PREF, true);
        editor.putString(Config.auth_user_id, auth_user_id);
        editor.putString(Config.auth_full_name, auth_full_name);
        editor.putString(Config.auth_user_avatar, auth_user_avatar);

        editor.apply();

        openProfile();

    }

    private void openProfile()
    {
        loading.dismiss();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
