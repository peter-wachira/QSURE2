package com.wazinsure.qsure.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.UI.PaCoverDetailActivity;
import com.wazinsure.qsure.models.PaCoverModel;

import java.util.ArrayList;

public class PaCoverAdapter  extends RecyclerView.Adapter<PaCoverAdapter.MyViewHolder> {
    private ArrayList<PaCoverModel> mPaCovers = new ArrayList<>();
    ArrayList<PaCoverModel> mDataFiltrered;

    boolean  isDark = false;

    RequestOptions option;

    Context mContext;


    public  PaCoverAdapter(Context mContext,ArrayList<PaCoverModel> mPaCovers,boolean isDark){
        this.mContext = mContext;
        this.mPaCovers = mPaCovers;
        this.isDark = isDark;
        this.mDataFiltrered = mPaCovers;

    }


    public  PaCoverAdapter(Context mContext,ArrayList<PaCoverModel> mPaCovers){
        this.mContext =mContext;
        this.mPaCovers = mPaCovers;
        this.isDark = isDark;
        this.mDataFiltrered = mPaCovers;
    }
    @NonNull
    @Override
    public PaCoverAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.pa_item, parent, false);

        return  new MyViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull PaCoverAdapter.MyViewHolder holder, int position) {

        holder.bindCustomer(mPaCovers.get(position));
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_animation));

    }

    @Override
    public int getItemCount() {
        return mDataFiltrered.size();
    }




    public class  MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{


        TextView cover_name;
        TextView cover_desc;
        TextView  product;
        TextView currency;
        TextView annual_premium;
        RelativeLayout container;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cover_name = itemView.findViewById(R.id.coverNameItem);
            product = itemView.findViewById(R.id.productItem);
            currency = itemView.findViewById(R.id.currencyItem);
            annual_premium = itemView.findViewById(R.id.annual_premiumItem);


            mContext = itemView.getContext();
            itemView.setOnClickListener(this);


            if (isDark) {
                setDarkTheme();
            }
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }


        private void setDarkTheme (){
            container.setBackgroundResource(R.drawable.card_bg_dark);
        }

        @Override
        public void onClick(View view) {
//            int itemPosition = getLayoutPosition();
//            Intent intent = new Intent(mContext, PaCoverDetailActivity.class);
//            intent.putExtra("position", itemPosition);
//            intent.putExtra("paCoverModel", Parcels.wrap(paCoverModel));
//            mContext.startActivity(intent);
        }



        public void bindCustomer(PaCoverModel paCoverModel) {
            cover_name.setText(paCoverModel.getCover_name());
            product.setText(paCoverModel.getProduct());
            annual_premium.setText(paCoverModel.getAnnual_premium());
            currency.setText(paCoverModel.getCurrency());


        }
    }
}
