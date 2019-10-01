package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.wazinsure.qsure.R;
import com.wazinsure.qsure.adapters.PaCoverPagerAdapter;
import com.wazinsure.qsure.models.PaCoverModel;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaCoverDetailActiviy extends AppCompatActivity {
    ArrayList<PaCoverModel> paCoverModelArrayList = new ArrayList<>();
    private PaCoverPagerAdapter adapterViewPager;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pa_cover_detail_activiy);

        ButterKnife.bind(this);

        paCoverModelArrayList = Parcels.unwrap(getIntent().getParcelableExtra("paCoverModel"));

        int startingPosition = getIntent().getIntExtra("position", 0);

        adapterViewPager = new PaCoverPagerAdapter(getSupportFragmentManager(), paCoverModelArrayList);

        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);


    }

}
