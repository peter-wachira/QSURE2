package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wazinsure.qsure.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FAQsActivity extends AppCompatActivity {

    @BindView(R.id.container1)
    LinearLayout container1;
    @BindView(R.id.linearitemscontainer)
    LinearLayout linearitemscontainer;
    @BindView(R.id.linearitemscontainer2)
    LinearLayout linearitemscontainer2;
    @BindView(R.id.linearitemscontainer3)
    LinearLayout linearitemscontainer3;
    @BindView(R.id.linearitemscontainer4)
    LinearLayout linearitemscontainer4;
    @BindView(R.id.linearitemscontainer5)
    LinearLayout linearitemscontainer5;

    @BindView(R.id.imageIQ)
    ImageView imageIQ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);
        ButterKnife.bind(this);
        createAnimations();

    }

    private void createAnimations() {


        imageIQ.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation));

        linearitemscontainer.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));

        linearitemscontainer3.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));

        linearitemscontainer4.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));

        linearitemscontainer5.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));


    }


}
