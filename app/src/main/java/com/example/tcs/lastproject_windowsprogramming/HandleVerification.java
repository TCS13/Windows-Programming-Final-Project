package com.example.tcs.lastproject_windowsprogramming;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by TCS on 2/26/15.
 */
public class HandleVerification extends ActionBarActivity {

    private Button mMainMenu;
    private Button mSubmit;
    private final String TAG = "SWC API";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_verification);
        mMainMenu = (Button)findViewById(R.id.back_to_overview);
        mMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HandleVerification.this, Overview.class);
                startActivity(i);
            }
        });
        mSubmit = (Button)findViewById(R.id.submit_handle_to_check);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reservedForGettingHandle = (EditText)findViewById(R.id.handle_to_verify);
                String handle = reservedForGettingHandle.getText().toString();
                try {
                    handle = URLEncoder.encode(handle, "utf-8");
                    handle = URLEncoder.encode(handle, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                //Log.d(TAG, "Handle: " + handle);
                new VerifyHandle().execute("http://www.swcombine.com:8081/ws/v1.0/handle/"+handle, handle);
            }
        });
    }

    private class VerifyHandle extends AsyncTask {
        private static final String DIALOG_STRING = "string";
        String handle;
        @Override
        protected String[] doInBackground(Object[] link) {
            handle = (String)link[1];
            //Log.d(TAG, "Background started");
            URL url = null;
            try {
                url = new URL((String) link[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Background continuing 1");
            URLConnection conn = null;
            try {
                conn = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d(TAG,"Try this: "+conn.getHeaderField(conn.getHeaderFields().size()-2));
//            for (int i = 0;; i++) {
//                String headerName = conn.getHeaderFieldKey(i);
//                String headerValue = conn.getHeaderField(i);
//                Log.d(TAG,"Header Name: "+headerName);
//                Log.d(TAG,"Header Value: "+headerValue);
//
//                if (headerName == null && headerValue == null) {
//                    System.out.println("No more headers");
//                    break;
//                }
//            }
            //String response = new HttpGet().getFirstHeader((String)link[0]).getValue();
            //Log.d(TAG, "Background continuing 2");
            //Log.d(TAG, response);
            String[] results = {conn.getHeaderField(conn.getHeaderFields().size()-2),handle};
            return results;
        }

        @Override
        protected void onPostExecute(Object str) {
            String[] strings = (String[])str;
            String han;
            try {
                han = URLDecoder.decode(strings[1],"utf-8");
                han = URLDecoder.decode(han,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                han = "ERROR OCCURRED";
            }
            if((strings[0]).equals("NETWORK 200"))
            {
                String title = "Handle Verification";
                String message = han+" exists!";
                FragmentManager fm = getSupportFragmentManager();
                DisplayDialog dialog = DisplayDialog.newInstance(title, message);
                dialog.show(fm, DIALOG_STRING);
            }
            else
            {
                String title = "Handle Verification";
                String message = han + " does not exist!";
                FragmentManager fm = getSupportFragmentManager();
                DisplayDialog dialog = DisplayDialog.newInstance(title, message);
                dialog.show(fm, DIALOG_STRING);
            }
        }
    }
}
