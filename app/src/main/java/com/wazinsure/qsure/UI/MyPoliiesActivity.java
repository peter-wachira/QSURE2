package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.helpers.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class MyPoliiesActivity extends AppCompatActivity {
    private static final String TAG = "MyPoliciesActivity";
    @BindView(R.id.nameofCoverPolicy)
    TextView nameOfCoverPolicy;
    @BindView(R.id.nameofProductPolicy)
    TextView nameofProductPolicy;
    @BindView(R.id.premiumPolicy)
    TextView premiumPolicy;
    @BindView(R.id.applicantsnamePolicy)
    TextView applicantSnamePolicy;
    @BindView(R.id.applicantPhonePolicy)
    TextView applicantPhonePolicy;
    @BindView(R.id.applicantEmailPolicy)
    TextView applicantEmailPolicy;
    @BindView(R.id.startDatePolicy)
    TextView startDatePolicy;
    @BindView(R.id.endDatePolicy)
    TextView endDatePolicy;

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
    @BindView(R.id.linearitemscontainer6)
    LinearLayout linearitemscontainer6;
    @BindView(R.id.linearitemscontainer7)
    LinearLayout linearitemscontainer7;
    @BindView(R.id.linearitemscontainer8)
    LinearLayout linearitemscontainer8;
    @BindView(R.id.linearitemscontainer9)
    LinearLayout linearitemscontainer9;

    @BindView(R.id.imagePolicies)
    ImageView imagePolicies;

    DatabaseHelper mDatabaseHelper;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    CheckConnection checkConnection;
    private String user_id;
    private String TOKEN;
    private JSONArray result;
    private String PA_POLICY_FOR ="https://demo.wazinsure.com:4443/api/papolicies/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_poliies);
        ButterKnife.bind(this);

        mDatabaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Hold on while we get your Policy...");
        createAnimations();
        retrieveUserDetailsandToken();



        if (!checkConnection.isConnectedToInternet(MyPoliiesActivity.this)) {
            hideDialog();
            shownetworkDialog();
        }
        else {
            getPApolicy();
        }
    }

    private void getPApolicy() {

        String cancel_req_tag = "my_pa_policy";

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PA_POLICY_FOR + user_id,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "PA policies for " + user_id);

                        hideDialog();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            Log.e(TAG, "PA POLicy Request" + j.getString("status"));

                            if (j.getString("status").equals("success")){

                                //Storing the Array of JSON String to our JSON Array
                                result = j.getJSONArray("data");
                                //Calling method getCustomers to get the customer from the JSON Array
                                getMyPoliciesResponseParams(result);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e(TAG, "MY Policies Response Error: " + error.getMessage());
                        Toasty.warning(getApplicationContext(), "MY Policies Response Error:  " + error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();


                    }
                }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);//put your token here
                return headers;

            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);


    }

    private void getMyPoliciesResponseParams(JSONArray j) {

        if (j != null) {
            //Traversing through all the items in the json array
            for (int i = 0; i < j.length(); i++) {
                try {
                    //Getting json object
                    JSONObject json = j.getJSONObject(i);
                    nameOfCoverPolicy.setText(json.getString("cover_name"));
                    nameofProductPolicy.setText(json.getString("product_name"));
                    premiumPolicy.setText(json.getString("premium"));
                    applicantSnamePolicy.setText(json.getString("applicant_name"));
                    applicantPhonePolicy.setText(json.getString("applicant_phone_number"));
                    applicantEmailPolicy.setText(json.getString("applicant_email"));
                    startDatePolicy.setText(json.getString("start_date"));
                    endDatePolicy.setText(json.getString("end_date"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    //Network connection Dialog
    private void shownetworkDialog() {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("No Internet")
                .setMessage("Please make sure you have internet connection")
                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)

                .show();
    }

        private void createAnimations() {
        imagePolicies.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation));
        linearitemscontainer.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer2.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer3.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer4.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer5.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer6.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer7.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer8.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
        linearitemscontainer9.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_scale_animation));
    }

    private void retrieveUserDetailsandToken() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);

        if (sharedPref != null) {
            user_id = sharedPref.getString("n", user_id);
            String token = sharedPref.getString("k", TOKEN);
            TOKEN = token;

        }

    }


    //process Dialog
    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



}
