package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.adapters.PaCoverAdapter;
import com.wazinsure.qsure.models.PaCoverModel;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


import java.util.ArrayList;

import java.util.HashMap;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;



public class DisplayPaCoversActivity extends AppCompatActivity {
    private static final String PA_URL = "https://demo.wazinsure.com:4443/api/pacovers";
    private String TOKEN;
    private RecyclerView recyclerView ;
    public static final String TAG = DisplayPaCoversActivity.class.getSimpleName();
    ArrayList<PaCoverModel> paCoverModelArrayList;
    ProgressDialog progressDialog;
    private JSONArray result;
    SharedPreferences sharedpreferences;
    private String CustomerIDRetrieved;
    private String CustomerID;
    boolean isDark =false;
    ConstraintLayout rootLayout;
    EditText searchInput;
    FloatingActionButton fabSwitcher;
    PaCoverAdapter myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_display_covers);

        fabSwitcher = findViewById(R.id.fab_switcher);
        rootLayout = findViewById(R.id.root_layout);
        searchInput = findViewById(R.id.search_input);



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
        recyclerView = findViewById(R.id.recyclerview);
        getPaCovers();



        // load theme state

        isDark = getThemeStatePref();
        if(isDark) {
            // dark theme is on
            rootLayout.setBackgroundColor(getResources().getColor(R.color.black));
            searchInput.setBackgroundResource(R.drawable.search_input_dark_style);
        }
        else
        {
            // light theme is on
            searchInput.setBackgroundResource(R.drawable.search_input_style);
            rootLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }


        fabSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isDark =!isDark;
                if(isDark){
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.black));
                    searchInput.setBackgroundResource(R.drawable.search_input_dark_style);
                }
                else{
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    searchInput.setBackgroundResource(R.drawable.search_input_style);
                }
                myadapter = new PaCoverAdapter(getApplicationContext(),paCoverModelArrayList,isDark);
                recyclerView.setAdapter(myadapter);
                saveThemeStatePref(isDark);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                myadapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





    }




    //loading theme state

    private void saveThemeStatePref(boolean isDark) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDark",isDark);
        editor.commit();
    }

    private boolean getThemeStatePref () {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPref",MODE_PRIVATE);
        boolean isDark = pref.getBoolean("isDark",false) ;
        return isDark;

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

                SetUpRecyclerView(paCoverModelArrayList);

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

        return paCoverModelArrayList;

    }



    //set up recycler view

    private  void SetUpRecyclerView(ArrayList<PaCoverModel> paCoverModelArrayList){
        myadapter = new PaCoverAdapter(this, this.paCoverModelArrayList,isDark) ;
        recyclerView.setAdapter(myadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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



