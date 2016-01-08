package com.lochbridge.cellphoneplan.android;

import android.os.Bundle;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabAdapter extends FragmentStatePagerAdapter {
  Bundle data;

    public TabAdapter(FragmentManager fm, Bundle data) {
        super(fm);
        this.data=data;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position) {


            case 0:
              if(data.getSerializable("logAggregationObj")!=null) {
                    FragmentPieChart tab3 = new FragmentPieChart();
                    tab3.setArguments(data);
                    return tab3;
                }

            case 1:
                if(data.getSerializable("logAggregationObj")!=null) {
                    FragmentMesgInternet fragmentMesgInternet = new FragmentMesgInternet();
                    fragmentMesgInternet.setArguments(data);
                    return fragmentMesgInternet;
                }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
