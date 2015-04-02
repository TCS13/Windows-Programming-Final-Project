package com.example.tcs.lastproject_windowsprogramming;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Overview extends ActionBarActivity {
    private TextView mSWC_TimeStamp;
    private ArrayList<Faction> mFactions;
    private Button mCheckHandleValidity;
    private Button mLookUpFaction;
    private Button mBrowseGalaxy;
    private Button mBrowseEntities;
    private final String TAG = "SWC API";
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        if(mFactions == null)
        {
            mFactions = new ArrayList<Faction>();
            mFactions.add(new Faction("Loading", "Loading", "Loading", "Loading", null));
        }
        new GetFactionList().execute(mFactions);

        mSWC_TimeStamp = (TextView)findViewById(R.id.swc_timestamp);
        new loadTimeStamp().execute("http://www.swcombine.com:8081/ws/v1.0/api/time/cgt/");

        mCheckHandleValidity = (Button)findViewById(R.id.check_user_name);
        mCheckHandleValidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Overview.this, HandleVerification.class);
                startActivity(i);
            }
        });
        mLookUpFaction = (Button)findViewById(R.id.factions);
        mLookUpFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Overview.this, FactionListActivity.class);
                i.putExtra(FactionListFragment.FACTION_ARRAY_LIST, mFactions);
                //i.putArrayListExtra(FactionListFragment.FACTION_ARRAY_LIST,mFactions);
                startActivity(i);
            }
        });
        mBrowseEntities = (Button)findViewById(R.id.entities);
        mBrowseGalaxy = (Button)findViewById(R.id.galaxy);


        //setContentView(R.layout.activity_handle_verification);
    }


    private class loadTimeStamp extends AsyncTask {

        @Override
        protected String doInBackground(Object[] link) {
            //Log.d(TAG, "Background started");
            URL url = null;
            try {
                url = new URL((String) link[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Background continuing 1");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Background continuing 2");
            Document document = null;
            try {
                document = builder.parse(url.openStream());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Background continuing 3");
            NodeList a = document.getElementsByTagName("timecgt");
            Element el = (Element)a.item(0);
            //Log.d(TAG, "About to start string");
            String hours = getTextValue(el, "hours");
            if(hours.length() == 1)
            {
                hours = "0" + hours;
            }
            String mins = getTextValue(el, "mins");
            if(mins.length() == 1)
            {
                mins = "0" + mins;
            }
            String secs = getTextValue(el, "secs");
            if(secs.length() == 1)
            {
                secs = "0" + secs;
            }
            String stamp = "Year " + getTextValue(el, "years") + " Day " + getTextValue(el, "days") + " " + hours + ":" + mins + ":" + secs;
            //Log.d(TAG, stamp);
            return stamp;
        }

        private String getTextValue(Element ele, String tagName) {
            String textVal = null;
            NodeList nl = ele.getElementsByTagName(tagName);
            if(nl != null && nl.getLength() > 0) {
                Element el = (Element)nl.item(0);
                textVal = el.getFirstChild().getNodeValue();
            }

            return textVal;
        }

        @Override
        protected void onPostExecute(Object str) {
            //Log.d(TAG, "Got to onPostExecute");
            //Log.d(TAG, (String)str);
            mSWC_TimeStamp.setText((String)str);
            mCheckHandleValidity.setText("Verify Handle");
            mCheckHandleValidity.setEnabled(true);
            //mLookUpFaction.setEnabled(true);
            //mGalaxy.setEnabled(true);
            //mEntity.setEnabled(true);
            //Log.d(TAG, "Background finished");
        }
    }


    private class GetFactionList extends AsyncTask {
        private final int MAX_FACTIONS_PER_QUERY = 50;
        protected ArrayList<Faction> doInBackground(Object[] link) {
            //ArrayList<Faction> factions = (ArrayList<Faction>)link[0];
            ArrayList<Faction> factions = new ArrayList<Faction>();
            URL url = null;
            try {
                url = new URL("http://www.swcombine.com:8081/ws/v1.0/factions/");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Background continuing 1");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            //Log.d(TAG, "Background continuing 2");
            Document document = null;
            try {
                document = builder.parse(url.openStream());
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            NodeList a = document.getElementsByTagName("factions");
            Element el = (Element)a.item(0);
            Log.d(TAG, "Total number of factions: " + el.getAttribute("total"));
            int numFactions = Integer.parseInt(el.getAttribute("total"));
            int numFactionsTotal = numFactions;
            int numCalls = 0;
            int numFactionsRemaining = 1;
            for(int i = 0; i < numFactions; i=i+MAX_FACTIONS_PER_QUERY)
            {
                numCalls++;
            }
            Log.d(TAG, "Total number of factions: "+numFactions);
            Log.d(TAG, "Total number of calls required: "+numCalls);
            int count = 0;
            for(int i = 0; i < numFactions && i < MAX_FACTIONS_PER_QUERY; i++)
            {
                NodeList b = el.getElementsByTagName("faction");
                Element fac = (Element)b.item(i);
//                NodeList facLead = fac.getElementsByTagName("leader");
//                Element lead = (Element)facLead.item(0);
//                NodeList fac2iC = fac.getElementsByTagName("second-in-command");
//                Element second = (Element)fac2iC.item(0);
                try {
                    //Log.d(TAG, "UID: "+fac.getAttribute("uid"));
                    Log.d(TAG, "Name: "+fac.getAttribute("name"));
                    //Log.d(TAG, "Link: "+fac.getAttribute("href"));
                    //Log.d(TAG, "Leader: "+getTextValue(fac, "leader"));
                    //Log.d(TAG, "2iC: "+getTextValue(fac, "second-in-command"));
                    Log.d(TAG, "Number: " + numFactionsRemaining);
                    Log.d(TAG, "Size of Factions: "+factions.size());
                    String uid = fac.getAttribute("uid");
                    String name = fac.getAttribute("name");
                    String leader = getTextValue(fac, "leader");
                    String sIc = getTextValue(fac, "second-in-command");
                    URL facURL = new URL(fac.getAttribute("href"));
                    Faction factionItself = new Faction(uid, name, sIc, leader, facURL);
                    factionItself.update();
                    factions.add(factionItself);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d(TAG, "An error occurred getting a faction");
                }
                //mLookUpFaction.setText("Loading... (" +numFactionsRemaining+"/"+numFactionsTotal+")");
                Log.d(TAG, factions.get(i).toString());
                numFactionsRemaining++;
                if(i==49)
                {
                    //mLookUpFaction.setText("Loading... (" +numFactionsRemaining/numFactionsTotal+"%)");
                    //updateButton(mLookUpFaction, "Loading... (" +numFactionsRemaining/numFactionsTotal+"%)");
                    numFactions -= 50;
                    try {
                        url = new URL("http://www.swcombine.com:8081/ws/v1.0/factions/?start_index="+ (numFactionsRemaining));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, "Background continuing 1");
                    factory = DocumentBuilderFactory.newInstance();
                    builder = null;
                    try {
                        builder = factory.newDocumentBuilder();
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
                    //Log.d(TAG, "Background continuing 2");
                    document = null;
                    try {
                        document = builder.parse(url.openStream());
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    a = document.getElementsByTagName("factions");
                    el = (Element)a.item(0);
                    i=-1;
                }
            }
            Collections.sort(factions);
            return factions;
        }

        private String getTextValue(Element ele, String tagName) {
            String textVal = null;
            NodeList nl = ele.getElementsByTagName(tagName);
            if(nl != null && nl.getLength() > 0) {
                Element el = (Element)nl.item(0);
                try {
                    textVal = el.getFirstChild().getNodeValue();
                }catch (Exception exc)
                {
                    exc.printStackTrace();
                    if(tagName != "href")
                    {
                        textVal = "None";
                    }
                    else {
                        textVal = "http://www.swcombine.com";
                    }
                }
            }

            return textVal;
        }

        @Override
        protected void onPostExecute(Object str) {
            Log.d(TAG, "Gets to onPostExecute");
            mFactions = (ArrayList<Faction>)str;
            mLookUpFaction.setText("Look Up Faction");
            mLookUpFaction.setEnabled(true);
            Log.d(TAG, "Size of mFactions: "+ mFactions.size());
        }
    }


//    public void updateButton(Button button, String text)
//    {
//        button.setText(text);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
