package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.models.PaCoverModel;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;


import butterknife.BindView;
import java.util.ArrayList;

import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;



public class BuyCoverActivity extends AppCompatActivity {
    private static final String PA_URL = "https://demo.wazinsure.com:4443/api/pacovers";
    private String TOKEN;
    private RecyclerView recyclerView ;
    public static final String TAG = BuyCoverActivity.class.getSimpleName();
    ArrayList<PaCoverModel> paCoverModelArrayList;
    ProgressDialog progressDialog;
    private JSONArray result;
    SharedPreferences sharedpreferences;
    private String CustomerIDRetrieved;
    private String CustomerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_cover);




        sharedpreferences = getSharedPreferences("mypref",
                Context.MODE_PRIVATE);
        CustomerIDRetrieved = (sharedpreferences.getString("n", "default"));
        CustomerID = CustomerIDRetrieved;
        String token = (sharedpreferences.getString("k", "TOKEN"));
        TOKEN = token;


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Records ...");
        showDialog();
        getPaCovers();
    }

    private void getPaCovers() {
        progressDialog.setMessage("Getting PA Covers..");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,PA_URL ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("data");
                            //Calling method getCustomers to get the customer from the JSON Array
                            getCovers(result);

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
                })

        {


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



    //getting PA cover items
    private ArrayList<PaCoverModel> getCovers(JSONArray result) {
        //Traversing through all the items in the json array

        paCoverModelArrayList = new ArrayList<>();

        for (int i = 0; i < result.length(); i++) {
            try {
                JSONObject json = null;
                //Getting json object

                json = result.getJSONObject(i);

                String cover_id =  json.getString("pa_cover_id");
                String cover_name =  json.getString("cover_name");
                String cover_desc =  json.getString("cover_desc");
                String currency   =  json.getString("currency");
                String annual_premium =  json.getString("annual_premium");
                String benefit1_name =  json.getString("benefit1_name");
                String benefit1_desc =  json.getString("benefit1_desc");
                String benefit1_amount =  json.getString("benefit1_amount");
                String benefit2_name =  json.getString("benefit2_name");
                String benefit2_desc =  json.getString("benefit2_desc");
                String benefit2_amount =  json.getString("benefit2_amount");
                String benefit3_name =  json.getString("benefit3_name");
                String benefit3_desc =  json.getString("benefit3_desc");
                String benefit3_amount =  json.getString("benefit3_amount");
                String product = json.getString("product");

                PaCoverModel paCoverModel = new PaCoverModel(cover_id,cover_name,cover_desc,currency,annual_premium,benefit1_name,benefit1_desc,benefit1_amount,benefit2_name,benefit2_desc,benefit2_amount,benefit3_name,benefit3_desc,benefit3_amount,product);

                //adding PA items to arrayList

                 paCoverModelArrayList.add(paCoverModel);


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
        return paCoverModelArrayList;
    }



    //set up recycler view

    private  void SetUpRecyclerView(){

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }







}



