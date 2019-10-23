package com.wazinsure.qsure.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.request.RequestOptions;
import com.wazinsure.qsure.R;

import com.wazinsure.qsure.UI.PaCoverDetailActiviy;
import com.wazinsure.qsure.models.PaCoverModel;
import android.widget.Filter;

import org.parceler.Parcels;

import java.util.ArrayList;

public class PaCoverAdapter  extends RecyclerView.Adapter<PaCoverAdapter.MyViewHolder>  implements Filterable {
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
        holder.container.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_scale_animation));





    }

    @Override
    public int getItemCount() {
        return mDataFiltrered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter(){

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if(Key.isEmpty()){
                    mDataFiltrered = mPaCovers;
                }else{
                    ArrayList<PaCoverModel> lstFiltered = new ArrayList<>();
                    for (PaCoverModel row: mPaCovers){
                        if (row.getCover_name().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    mDataFiltrered = lstFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltrered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                mDataFiltrered = (ArrayList<PaCoverModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class  MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{


        TextView cover_name;
        ImageView imageView;
        TextView cover_description;
        TextView  product;
        TextView currency;
        TextView annual_premium;
        RelativeLayout container;
        CardView innerContainer;




        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cover_name = itemView.findViewById(R.id.coverNameItem);
            product = itemView.findViewById(R.id.productItem);
            currency = itemView.findViewById(R.id.currencyItem);
            annual_premium = itemView.findViewById(R.id.annual_premiumItem);
            container = itemView.findViewById(R.id.container);
            innerContainer = itemView.findViewById(R.id.innerContainer);
            imageView = itemView.findViewById(R.id.image1);
            cover_description = itemView.findViewById(R.id.pa_cover_description);

            mContext = itemView.getContext();
            itemView.setOnClickListener(this);


            if (isDark) {
                setDarkTheme();
            }
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);

        }


        private void setDarkTheme (){
            container.setBackgroundResource(R.color.black);
            innerContainer.setBackgroundResource(R.drawable.card_bg_dark);


        }

        @Override
        public void onClick(View view) {
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, PaCoverDetailActiviy.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("paCoverModel", Parcels.wrap(mPaCovers));
            mContext.startActivity(intent);
        }



        public void bindCustomer(PaCoverModel paCoverModel) {
            cover_name.setText(paCoverModel.getCover_name());
            product.setText(paCoverModel.getProduct());
            annual_premium.setText(paCoverModel.getAnnual_premium());
            currency.setText(paCoverModel.getCurrency());


        }
    }
}
