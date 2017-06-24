package keni.paritet.Activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import keni.paritet.BuildConfig;
import keni.paritet.Config.Config;
import keni.paritet.R;

/**
 * Created by Keni on 15.02.2017.
 */

public class SplashActivity extends AppCompatActivity
{
    private static final String KEY_IMEI = "IMEI";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_VERSION = "version";
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private TelephonyManager mngr;
    private int versionCode;
    //private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        int permissionCheckIMEI = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        versionCode = BuildConfig.VERSION_CODE;

        //SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        //token = pref.getString("regId", null);

        if (permissionCheckIMEI != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        }
        else
        {
            //SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
            //String token = pref.getString("regId", null);

            checkIMEI();
            //CheckImei checkImei = new CheckImei();
            //checkImei.imeiCheck(SplashActivity.this, mngr.getDeviceId(), token, String.valueOf(versionCode));
        }
    }

    private void checkIMEI()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CHECK_IMEI_URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if (response.trim().equals(Config.IMEI_SUCCESS))
                        {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), R.string.errorNetwork, Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_IMEI, mngr.getDeviceId());
                //map.put(KEY_TOKEN, token);
                map.put(KEY_VERSION, String.valueOf(versionCode));

                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    checkIMEI();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Ну без этого никак :(", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            default:
                break;
        }
    }

}
