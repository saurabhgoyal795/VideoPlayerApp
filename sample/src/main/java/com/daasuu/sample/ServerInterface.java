package com.daasuu.sample;

/**
 * Created by saurabhgoyal on 13/03/18.
 */


import android.util.Log;

import com.daasuu.sample.widget.CommonUtility;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;


public class ServerInterface {


    public static String doSyncPost(String server, String query) throws IOException {
        if(CommonUtility.isDebugModeOn) {
            Log.i(CommonUtility.TAG, "request: " + server + "?" + query);
        }

        URL url = new URL(server);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        OutputStream os = connection.getOutputStream();
        try {
            os.write(query.getBytes());
            os.flush();
        } finally {
            os.close();
        }

        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } finally {
            reader.close();
            is.close();
        }
        connection.disconnect();

        String serverResponse = buffer.toString();

        if(CommonUtility.isDebugModeOn) {
            Log.i(CommonUtility.TAG, "response: " + serverResponse);
        }

        return serverResponse;
    }

    public static String doSync(String urlString) throws IOException {
        if(CommonUtility.isDebugModeOn) {
            Log.i(CommonUtility.TAG, "request: " + urlString);
        }
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.connect();
            connection.setReadTimeout(60000);

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder buffer = new StringBuilder();
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            } finally {
                reader.close();
                is.close();
            }
            connection.disconnect();

            String serverResponse = buffer.toString();

            if (CommonUtility.isDebugModeOn) {
                Log.i(CommonUtility.TAG, "response: " + serverResponse);
            }
            return serverResponse;
        }catch (Exception e){
            if(CommonUtility.isDebugModeOn){
                e.printStackTrace();
            }
        }
        return "error";
    }
    public static String doSyncPost(String urlString, ArrayList<ServerParameter>
            params) throws IOException {
        if(CommonUtility.isDebugModeOn) {
            Log.i(CommonUtility.TAG, "request: " + urlString + "?" + params);
        }

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();

        OutputStream os = connection.getOutputStream();
        try {
            os.write(buildQuery(params).getBytes());
            os.flush();
        } finally {
            os.close();
        }

        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder buffer = new StringBuilder();
        try {
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } finally {
            reader.close();
            is.close();
        }
        connection.disconnect();

        String serverResponse = buffer.toString();

        if(CommonUtility.isDebugModeOn) {
            Log.i(CommonUtility.TAG, "response: " + serverResponse);
        }

        return serverResponse;
    }
    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    public static String buildQuery(ArrayList<ServerParameter> params){
        String query = "";

        if(params == null || params.size() == 0)
            return query;
        int counter = 0;
        for(ServerParameter param: params) {
            String key = param.getKey();
            String value = param.getValue();
            try {
                if((key == null || key.length() == 0) || (value == null || value.length() == 0)) {
                    ++counter;
                    continue;
                }
            } catch (NullPointerException e) {
                ++counter;

                continue;
            }
            try {
                query += key + "=" + URLEncoder.encode(value, "UTF-8");
            } catch(UnsupportedEncodingException e) {
                if(CommonUtility.isDebugModeOn) {
                    CommonUtility.printStackTrace(e);
                }

            }
            if(++counter < params.size())
                query += "&";
        }
        return query;


    }




}