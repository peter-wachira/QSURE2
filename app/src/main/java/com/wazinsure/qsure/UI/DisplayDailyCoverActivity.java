package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wazinsure.qsure.R;

import com.wazinsure.qsure.models.PaCoverModel;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DisplayDailyCoverActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.coverName)
    TextView coverName;
    @BindView(R.id.currency)
    TextView currency;
    @BindView(R.id.annual_premium)
    TextView annualPremium;
    @BindView(R.id.productName)
    TextView product;
    @BindView(R.id.btn_purchase)
    Button btn_Purchase;
    @BindView(R.id.pa_cover_description)
    TextView pa_cover_description;
    private static final String PA_URL = "https://demo.wazinsure.com:4443/api/pacovers/3";
    public static final String TAG = DisplayPaCoversActivity.class.getSimpleName();
    ArrayList<PaCoverModel> paCoverModelArrayList;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferences;
    private String CustomerIDRetrieved;
    CheckConnection checkConnection;
    Context context;

    private String TOKEN;
    private String productPref;
    private String coverNamePref;
    private String annualPremiumPref;
    private String pa_cover_descriptionPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_single_cover);
        ButterKnife.bind(this);


        sharedpreferences = getSharedPreferences("mypref",
                Context.MODE_PRIVATE);
        CustomerIDRetrieved = (sharedpreferences.getString("n", "default"));

        String token = (sharedpreferences.getString("k", "TOKEN"));
        TOKEN = token;

        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        showDialog();
        progressDialog.setCancelable(false);

        getDailyCover();
        btn_Purchase.setOnClickListener(this);
    }

    private void getDailyCover() {
        if (!checkConnection.isConnectedToInternet(getApplicationContext())) {
            shownetworkDialog();
        } else {

            progressDialog.setMessage("Getting Daily Cover..");
            StringRequest stringRequest = new StringRequest(Request.Method.GET, PA_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            hideDialog();
                            JSONObject j = null;
                            try {
                                //Parsing the fetched Json String to JSON Object
                                j = new JSONObject(response);
                                JSONObject json = null;
                                //Storing the Array of JSON String to our JSON Array
                                json = j.getJSONObject("data");
                                //Calling method getCustomers to get the customer from the JSON Array
                                coverName.setText(json.getString("cover_name"));
                                coverNamePref = json.getString("cover_name");
                                pa_cover_description.setText(json.getString("cover_desc"));
                                pa_cover_descriptionPref = json.getString("cover_desc");
                                currency.setText(json.getString("currency"));
                                annualPremium.setText(json.getString("annual_premium"));
                                annualPremiumPref = json.getString("annual_premium");
                                product.setText(json.getString("product"));
                                productPref = json.getString("product");



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

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


    }


    public void  saveCoverInformation(){
        SharedPreferences sharedPref =  this.getSharedPreferences(
                "displayPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("product_name_display",productPref);
        editor.putString("cover_name_display", coverNamePref);
        editor.putString("cover_details_display",pa_cover_descriptionPref);
        editor.putString("product_premium_display",annualPremiumPref);
        editor.putString("k",TOKEN);
        editor.commit();
    }
    //checking internet connection
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
                .setIcon(R.mipmap.no_wifi_icon)
                .setCancelable(false)

                .show();
    }


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View view) {

        if (view == btn_Purchase) {

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), PurchaseCoverActivityFirstTimeUser.class);
                    saveCoverInformation();
                    startActivity(intent);
                }
            });
        }
    }
}