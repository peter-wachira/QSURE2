package com.wazinsure.qsure.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wazinsure.qsure.R;
import com.wazinsure.qsure.models.PaCoverModel;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;


public class PaCoverDetailFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.coverName)
    TextView coverName;
    @BindView(R.id.currency) TextView currency;
    @BindView(R.id.annual_premium) TextView annualPremium;
    @BindView(R.id.productName)
    TextView product;
    @BindView(R.id.btn_purchase)
    Button btn_Purchase;
    @BindView(R.id.pa_cover_description) TextView pa_cover_description;
    Context mContext;
    PaCoverModel paCoverModel;
    private OnFragmentInteractionListener mListener;

    public PaCoverDetailFragment() {
        // Required empty public constructor
    }

    public static PaCoverDetailFragment newInstance (PaCoverModel paCoverModel) {
        PaCoverDetailFragment paCoverDetailFragment = new PaCoverDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("paCoverModel",Parcels.wrap(paCoverModel));
        paCoverDetailFragment.setArguments(args);

        return paCoverDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.onCreate(savedInstanceState);
        paCoverModel = Parcels.unwrap(getArguments().getParcelable("paCoverModel"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pa_cover_detail, container, false);
        ButterKnife.bind(this, view);


        btn_Purchase.setOnClickListener(this);

        annualPremium.setText(paCoverModel.getAnnual_premium());

        coverName.setText(paCoverModel.getCover_name());

        currency.setText(paCoverModel.getCurrency());

        product.setText(paCoverModel.getProduct());


        pa_cover_description.setText(paCoverModel.getCover_desc());


        return view;


    }

    @Override
    public void onClick(View view) {
        if (view == btn_Purchase){

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                Intent intent = new Intent(getContext(),PurchaseCoverActivity2.class);
                    saveProductDetails();
                    startActivity(intent);
                }
            });

        }

    }


    private void saveProductDetails() {
        SharedPreferences sharedPref = this.getActivity().getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("product_name",paCoverModel.getProduct());
        editor.putString("cover_id", paCoverModel.getPa_cover_id());
        editor.putString("cover_name",paCoverModel.getCover_name());
        editor.putString("cover_details",paCoverModel.getCover_desc());
        editor.putString("product_premium",paCoverModel.getAnnual_premium());
        editor.putString("pa_cover_description",paCoverModel.getCover_desc());
        editor.putString("currency",paCoverModel.getCurrency());
        editor.commit();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
