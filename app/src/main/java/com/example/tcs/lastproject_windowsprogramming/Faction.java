package com.example.tcs.lastproject_windowsprogramming;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by TCS on 3/17/15.
 */
public class Faction implements Serializable, Comparable{
    private UUID mID;
    private String mSWC_uid;
    private String mName;
    private URL mLogo;
    private String m2iC;
    private String mLeader;
    private String mCatAndType;
    private String mFoundationDate;
    private URL mUpdateURL;
    private byte[] mLogoDrawable;

    private static final String TAG = "SWC API";

    public Faction(String factionUID, String factionName, String faction2iC, String factionLeader, URL factionURL) {
        //Generate a unique identifier
        mID = UUID.randomUUID();
        mSWC_uid = factionUID;
        mName = factionName;
        m2iC = faction2iC;
        mLeader = factionLeader;
        mUpdateURL = factionURL;
        mLogoDrawable = null;
        try {
            mLogo = new URL("http://id.swc-tradefederation.org/tba.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public UUID getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public URL getLogo() {
        return mLogo;
    }

    public void setLogo(URL logo) {
        mLogo = logo;
    }

    public String getM2iC() {
        return "Second-in-command: "+m2iC;
    }

    public void setM2iC(String m2iC) {
        this.m2iC = m2iC;
    }

    public String getLeader() {
        return "Leader: "+mLeader;
    }

    public void setLeader(String leader) {
        mLeader = leader;
    }

    public String getCatAndType() {
        return mCatAndType;
    }

    public void setCatAndType(String catAndType) {
        mCatAndType = catAndType;
    }

    public String getFoundationDate() {
        return "Founded: "+mFoundationDate;
    }

    public void setFoundationDate(String foundationDate) {
        mFoundationDate = foundationDate;
    }

    public URL getUpdateURL() {
        return mUpdateURL;
    }

    public Bitmap getLogoDrawable() {
        return BitmapFactory.decodeByteArray(mLogoDrawable, 0, mLogoDrawable.length);
    }

    public void update() {
        new updateFaction().execute(getUpdateURL());
    }

    @Override
    public String toString() { return mName; }

    private class loadImage extends AsyncTask {
        protected Bitmap doInBackground(Object[] link) {
            //ArrayList<Faction> factions = (ArrayList<Faction>)link[0];
            ArrayList<Faction> factions = new ArrayList<Faction>();
            URL url = (URL)link[0];
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Object img) {
            if((Bitmap)img != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ((Bitmap) img).compress(Bitmap.CompressFormat.PNG, 100, stream);
                mLogoDrawable = stream.toByteArray();
                //Log.d(TAG, "Logo");
            }
        }
    }

    private class updateFaction extends AsyncTask {
        private final int MAX_FACTIONS_PER_QUERY = 50;
        protected String[] doInBackground(Object[] uid) {
            String[] data = new String[6];
            URL url = (URL)uid[0];
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
            NodeList a = document.getElementsByTagName("faction");
            Element fac = (Element)a.item(0);
            String name = getTextValue(fac, "name");
            String leader = getTextValue(fac, "leader");
            String sIc = getTextValue(fac, "second-in-command");
            String catAndType = getTextValue(fac, "category") + ": " + getTextValue(fac, "type");
            NodeList b = document.getElementsByTagName("foundation-date");
            Element date = (Element)b.item(0);
            String creationDate = getTextValue(date, "timestamp-cgt");
            NodeList c = document.getElementsByTagName("images");
            Element images = (Element)c.item(0);
            String logo = getTextValue(images, "logo");
            data[0] = name;
            data[1] = leader;
            data[2] = sIc;
            data[3] = catAndType;
            data[4] = creationDate;
            data[5] = logo;
            return data;
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
            Log.d(TAG, "Updating "+getName());
            String[] results = (String[])str;
            setName(results[0]);
            setLeader(results[1]);
            setM2iC(results[2]);
            setCatAndType(results[3]);
            setFoundationDate(results[4]);
            try {
                setLogo(new URL(results[5]));
                //new loadImage().execute(getLogo());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int compareTo(Object f) {
        return (((Faction)f).getName()).compareTo(this.mName) * -1;
    }
}
