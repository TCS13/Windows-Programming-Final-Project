package com.example.tcs.lastproject_windowsprogramming;

import android.support.v4.app.Fragment;

/**
 * Created by TCS on 3/17/15.
 */
public class FactionListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new FactionListFragment();
    }
}
