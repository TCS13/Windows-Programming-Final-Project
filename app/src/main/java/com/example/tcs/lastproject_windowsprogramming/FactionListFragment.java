package com.example.tcs.lastproject_windowsprogramming;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by TCS on 3/17/15.
 */
public class FactionListFragment extends ListFragment {
    private ArrayList<Faction> mFactions;
    private static final String TAG = "SWC API";
    public static final String FACTION_ARRAY_LIST = "com.example.tcs.lastproject_windowsprogramming.faction_array_list";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.factions_title);
        Log.d(TAG, "About to load factions");
        mFactions = (ArrayList<Faction>)getActivity().getIntent().getSerializableExtra(FACTION_ARRAY_LIST);
        //mFactions = FactionGeneration.get(getActivity()).getFactions();
        //Log.d(TAG, "Entering while loop");
        //while (mFactions == null)
        //{
        //    FactionGeneration.get(getActivity()).getFactions();
        //}
        //Log.d(TAG, "Exited while loop");
        FactionAdapter adapter = new FactionAdapter(mFactions);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Faction f = ((FactionAdapter)getListAdapter()).getItem(position);
        //Log.d(TAG, c.getTitle() + " was clicked.");
        Intent i = new Intent(getActivity(), FactionPagerActivity.class);
        i.putExtra(FACTION_ARRAY_LIST, mFactions);
        i.putExtra(FactionFragment.EXTRA_FACTION, f);
        Log.d(TAG, "Faction sent: "+f.toString());
        i.putExtra(FactionFragment.EXTRA_FACTION_ID, f.getID());
        startActivity(i);
    }

    private class FactionAdapter extends ArrayAdapter<Faction> {
        public FactionAdapter(ArrayList<Faction> factions) {
            super(getActivity(), 0, factions);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //If we weren't given a view, inflate one
            if(convertView == null)
            {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_faction, null);
            }

            //Configure the view for this Crime
            Faction f = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.faction_list_item_titleTextView);
            titleTextView.setText(f.getName());
            TextView leaderTextView = (TextView)convertView.findViewById(R.id.faction_list_item_leaderTextView);
            leaderTextView.setText(f.getLeader());
            TextView secondTextView = (TextView)convertView.findViewById(R.id.faction_list_item_2iCTextView);
            secondTextView.setText(f.getM2iC());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FactionAdapter)getListAdapter()).notifyDataSetChanged();
    }

}
