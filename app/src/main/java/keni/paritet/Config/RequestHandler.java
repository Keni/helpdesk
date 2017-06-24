package keni.paritet.Config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Keni on 09.11.2016.
 */

public class RequestHandler
{
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams)
    {
        URL url;

        StringBuilder sb = new StringBuilder();
        try
        {
            url = new URL(requestURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String response;

                while ((response = br.readLine()) != null)
                {
                    sb.append(response);
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public String sendGetRequest(String requestURL)
    {
        StringBuilder sb = new StringBuilder();

        try
        {
            URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null)
            {
                sb.append(s + "\n");
            }
        }
        catch (Exception e)
        {
        }

        return sb.toString();
    }

    public String sendGetRequestParam(String requestURL, String id)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            URL url = new URL(requestURL + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null)
            {
                sb.append(s + "\n");
            }
        }
        catch (Exception e)
        {
        }
        return sb.toString();
    }

    public String sendGetDoubleRequestParam(String requestURL, String id, String params)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            URL url = new URL(requestURL + id + "&status=" + params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null)
            {
                sb.append(s + "\n");
            }
        }
        catch (Exception e)
        {
        }
        return sb.toString();
    }

    public String sendGetFilterRequestParam(String requestURL, String performer_id, String object_id, String priority, String status, String dt0, String dt1, String sort)
    {
        StringBuilder sb = new StringBuilder();

        try
        {
            URL url = new URL(requestURL + "?performer_id=" + performer_id + "&object_id=" + object_id + "&priority=" + priority + "&status=" + status + "&dt0=" + dt0 + "&dt1=" + dt1 + "&sort=" + sort);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String s;
            while ((s = bufferedReader.readLine()) != null)
            {
                sb.append(s + "\n");
            }
        }
        catch (Exception e)
        {
        }
        return sb.toString();
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
