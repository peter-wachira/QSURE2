package com.wazinsure.qsure.UI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wazinsure.qsure.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class User_FirsttimeFragment extends Fragment  implements View.OnClickListener{
    @BindView(R.id.dailyCover)
    CardView dailyCoverCard;
    @BindView(R.id.monthlyCover)
    CardView monthlyCoverCard;
    @BindView(R.id.quarterlyCover)
    CardView quarterlyCoverCard;
    @BindView(R.id.faqs)
    CardView faqsCard;
    Context mContext;
    private OnFragmentInteractionListener mListener;

    public User_FirsttimeFragment() {
        // Required empty public constructor
    }


    public static User_FirsttimeFragment newInstance(String param1, String param2) {
        User_FirsttimeFragment fragment = new User_FirsttimeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_user__firsttime, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,view);

        //Add click listener to the cards
        dailyCoverCard.setOnClickListener(this);
        monthlyCoverCard.setOnClickListener(this);
        quarterlyCoverCard.setOnClickListener(this);
        faqsCard.setOnClickListener(this);
        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {


        Intent i;

        switch (view.getId()) {

            case R.id.dailyCover :i = new Intent(getContext(),DisplayDailyCoverActivity.class);
                startActivity(i);break;
            case R.id.monthlyCover :i= new Intent(getContext(), DisplayMonthlyCoverActivity.class);
                startActivity(i);break;
            case R.id.quarterlyCover: i= new Intent(getContext(), DisplayAnnualCoverActivity.class);
                startActivity(i);break;
            case R.id.faqs: i= new Intent(getContext(), FAQsActivity.class);
                startActivity(i);break;
            default:break;

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
