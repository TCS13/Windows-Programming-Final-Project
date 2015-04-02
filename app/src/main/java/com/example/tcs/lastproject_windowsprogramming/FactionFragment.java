package com.example.tcs.lastproject_windowsprogramming;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

public class FactionFragment extends Fragment {

    public static final String EXTRA_FACTION_ID = "com.example.tcs.lastproject_windowsprogramming.faction_id";
    public static final String EXTRA_FACTION = "com.example.tcs.lastproject_windowsprogramming.faction";
    private Faction mFaction;
    private TextView mFactionName;
    private ImageView mFactionLogo;
    private TextView mFactionCatAndType;
    private TextView mFactionCreationDate;
    private TextView mFactionLeader;
    private TextView mFaction2iC;
    private static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;

    private static final String TAG = "SWC API";

    public static FactionFragment newInstance(String param1, String param2) {
        FactionFragment fragment = new FactionFragment();
        return fragment;
    }

    public FactionFragment() {
        // Required empty public constructor
    }

    public static FactionFragment newInstance(UUID factionId, Faction faction) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FACTION_ID, factionId);
        args.putSerializable(EXTRA_FACTION, faction);

        FactionFragment fragment = new FactionFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID factionId = (UUID)getArguments().getSerializable(EXTRA_FACTION_ID);
        //Log.d(TAG, factionId.toString());
        mFaction = (Faction)getArguments().getSerializable(EXTRA_FACTION);
        mFaction.update();
        //Log.d(TAG, "Faction retrieved: "+mFaction.toString());
        //mFaction = FactionGeneration.get(getActivity()).getFaction(factionId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_faction,parent,false);

        mFactionLogo = (ImageView)v.findViewById(R.id.faction_logo);
//        mFactionLogo.setMinimumHeight(70);
//        mFactionLogo.setMaxHeight(70);
//        mFactionLogo.setMinimumWidth(70);
//        mFactionLogo.setMaxWidth(70);
        //mFactionLogo.setImageURI(mFaction.getLogo().toURI());
        new loadImage().execute(mFaction.getLogo());
        mFactionLogo.setImageResource(R.drawable.tba);
        //mFactionLogo.setImageBitmap(mFaction.getLogoDrawable());

        mFactionName = (TextView)v.findViewById(R.id.faction_name);
        mFactionName.setText(mFaction.getName());

        mFactionCatAndType = (TextView)v.findViewById(R.id.faction_cat_and_type);
        mFactionCatAndType.setText(mFaction.getCatAndType());

        mFactionCreationDate = (TextView)v.findViewById(R.id.faction_founding_date);
        mFactionCreationDate.setText(mFaction.getFoundationDate());

        mFactionLeader = (TextView)v.findViewById(R.id.faction_leader);
        mFactionLeader.setText(mFaction.getLeader());

        mFaction2iC = (TextView)v.findViewById(R.id.faction_2iC);
        mFaction2iC.setText(mFaction.getM2iC());


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            //Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            //mCrime.setDate(date);
            //updateDate();
        }
    }

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
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //((Bitmap) img).compress(Bitmap.CompressFormat.PNG, 100, stream);
                mFactionLogo.setImageBitmap((Bitmap)img);
                //Log.d(TAG, "Logo");
            }
        }
    }
}
