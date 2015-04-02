package com.example.tcs.lastproject_windowsprogramming;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by TCS on 3/17/15.
 */
public class FactionPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Faction> mFactions;

    private static final String TAG = "SWC API";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        //mFactions = FactionGeneration.get(this).getFactions();
        mFactions = (ArrayList<Faction>)getIntent().getSerializableExtra(FactionListFragment.FACTION_ARRAY_LIST);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Faction f = mFactions.get(position);
                Log.d(TAG, "Adapter set: "+f.toString());
                return FactionFragment.newInstance(f.getID(), f);
            }

            @Override
            public int getCount() {
                return mFactions.size();
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) { }

            public void onPageScrolled(int pos, float posOffset, int postOffsetPixels) { }

            public void onPageSelected(int pos) {
                Faction f = mFactions.get(pos);
                if(f.getName() != null) {
                    setTitle(f.getName());
                }
            }
        });

        UUID factionId = (UUID)getIntent().getSerializableExtra(FactionFragment.EXTRA_FACTION_ID);
        for (int i = 0; i < mFactions.size(); i++)
        {
            if(mFactions.get(i).getID().equals(factionId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
