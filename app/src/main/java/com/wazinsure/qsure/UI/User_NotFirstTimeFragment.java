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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link User_NotFirstTimeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link User_NotFirstTimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_NotFirstTimeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.buy_cover)
    CardView buyCoverCard;
    @BindView(R.id.my_policies)
    CardView myPoliciesCard;
    @BindView(R.id.lodge_claim)
    CardView lodgeClaimsCard;
    @BindView(R.id.renewals)
    CardView renewalsCard;
    @BindView(R.id.quiz)
    CardView  quizCard;
    @BindView(R.id.faqs)
    CardView faqsCard;
    Context mContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public User_NotFirstTimeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment User_NotFirstTimeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static User_NotFirstTimeFragment newInstance(String param1, String param2) {
        User_NotFirstTimeFragment fragment = new User_NotFirstTimeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user__not_first_time, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,view);

        //Add click listener to the cards
        buyCoverCard.setOnClickListener(this);
        myPoliciesCard.setOnClickListener(this);
        lodgeClaimsCard.setOnClickListener(this);
        renewalsCard.setOnClickListener(this);
        quizCard.setOnClickListener(this);
        faqsCard.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {

        Intent i;

        switch (view.getId()) {

            case R.id.buy_cover :i = new Intent(getContext(),BuyCoverActivity.class);
                startActivity(i);break;
            case R.id.my_policies :i= new Intent(getContext(), MyPoliiesActivity.class);
                startActivity(i);break;
            case R.id.lodge_claim: i= new Intent(getContext(),LodgeClaimActivity.class);
                startActivity(i);break;
            case R.id.renewals: i= new Intent(getContext(), RenewalsActivity.class);
                startActivity(i);break;
            case R.id.quiz: i= new Intent(getContext(),QuizActivity.class);
                startActivity(i);break;
            case R.id.faqs: i= new Intent(getContext(),FAQsActivity.class);
                startActivity(i);break;

            default:break;

        }
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
