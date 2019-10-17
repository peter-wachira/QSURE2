package com.wazinsure.qsure.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wazinsure.qsure.UI.PaCoverDetailFragment;
import com.wazinsure.qsure.models.PaCoverModel;

import java.util.ArrayList;

public class PaCoverPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<PaCoverModel> paCoverModelArrayList;


    public PaCoverPagerAdapter(FragmentManager fm,ArrayList<PaCoverModel> paCoverModels)  {

        super(fm);
        paCoverModelArrayList = paCoverModels;
    }

    @Override
    public Fragment getItem(int position) {
        return    PaCoverDetailFragment.newInstance(paCoverModelArrayList.get(position));

    }

    @Override
    public int getCount() {
        return  paCoverModelArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return paCoverModelArrayList.get(position).getProduct();
    }

}
