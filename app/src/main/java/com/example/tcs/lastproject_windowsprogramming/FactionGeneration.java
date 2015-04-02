package com.example.tcs.lastproject_windowsprogramming;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TCS on 1/27/15.
 */
public class FactionGeneration {
    private static ArrayList<Faction> mFactions;

    private static FactionGeneration sFactionGeneration;
    private Context mAppContext;

    private static final String TAG = "SWC API";

    private FactionGeneration(Context appContext) {
        mAppContext = appContext;
        mFactions = new ArrayList<Faction>();
        mFactions.add(new Faction("Loading", "Loading", "Loading", "Loading", null));
        Log.d(TAG, "Added dummy faction for initial loading");
        new GetFactionList().execute();
    }

    public static FactionGeneration get(Context c) {
        if (sFactionGeneration == null) {
            sFactionGeneration = new FactionGeneration(c.getApplicationContext());
            Log.d(TAG, "Entering while loop");
            while(mFactions == null || mFactions.size()==0)
            {
                int a = 1+1;
            }
            Log.d(TAG, "Exited while loop");
        }
        return sFactionGeneration;
    }

    public ArrayList<Faction> getFactions() {
        return mFactions;
    }

    public Faction getFaction(UUID id) {
        for (Faction f : mFactions) {
            if (f.getID().equals(id))
                return f;
        }
        return null;
    }

    private class GetFactionList extends AsyncTask {
        private final int MAX_FACTIONS_PER_QUERY = 50;
        protected ArrayList<Faction> doInBackground(Object[] link) {
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
            Log.d(TAG, "Total number of factions: "+el.getAttribute("total"));
            int numFactions = Integer.parseInt(el.getAttribute("total"));
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
                    //Log.d(TAG, "Number: " + numFactionsRemaining);
                    Log.d(TAG, "Size of Factions: "+factions.size());
                    String uid = fac.getAttribute("uid");
                    String name = fac.getAttribute("name");
                    String leader = getTextValue(fac, "leader");
                    String sIc = getTextValue(fac, "second-in-command");
                    URL facURL = new URL(fac.getAttribute("href"));
                    Faction factionItself = new Faction(uid, name, sIc, leader, facURL);
                    factions.add(factionItself);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    Log.d(TAG, "An error occurred getting a faction");
                }
                Log.d(TAG, factions.get(i).toString());
                numFactionsRemaining++;
                if(i==49)
                {
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
            Log.d(TAG, "Size of mFactions: "+ mFactions.size());
        }
    }

}
