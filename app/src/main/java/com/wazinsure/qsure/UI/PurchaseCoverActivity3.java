package com.wazinsure.qsure.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.service.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class PurchaseCoverActivity3 extends AppCompatActivity {
    private static final String TAG = "PurchaseCoverActivity2";
    private static final String MPESA_URL_TO ="https://idealbodaboda.com:4443/api/mpesaresponsesdto/" ;
    SharedPreferences sharedpreferences;
    String  product_name;
    String TOKEN;
    String  cover_name;
    String  cover_details;
    String  product_premium;
    String  cover_id;

    String desthortCode = "733539";
    String merchantID;
    String merchantkey ="12345";
    String  asyncCallback = "http://127.0.0.1:8989/stkpush";
    String STK_PUSH_URL  = "http://transactions.wazinsure.com:1780/todaytech/express/request";
    String TransactionDescription = "Payment for ";
    String TransactionId  ="PA Cover";


    //mpesa Response Params
    String transaction_id;
    String second_name;
    String key_id;
    String subscriber_nr;
    String transaction_date;
    String amount_received;
    String message_id;
    String processor_id;
    String account_nr;
    String last_name;
    String current_balance;
    String short_code;






    ProgressDialog progressDialog;
    CheckConnection checkConnection;
    @BindView(R.id.termsandCondtitionsBuy)
    CheckBox termsandConditionsBuy;
    @BindView(R.id.kra_pinBuy)
    EditText krapinBuy;
    @BindView(R.id.previousaccidentsBuy)
    EditText previousaccidentBuy;
    @BindView(R.id.infirmityBuy)
    EditText infirmityBuy;
    @BindView(R.id.declarationBuyCheckbox)
    CheckBox declarationBuyCheckbox;
    @BindView(R.id.addBenefactor)
    CheckBox addBenefactorCheckbox;

    @BindView(R.id.benefactor2)
    CardView benefactor2CardView;
    @BindView(R.id.btn_Buy)
    Button btnBuyCover;
    @BindView(R.id.mobile_noBuy)
    EditText mobile_noBuy;
    @BindView(R.id.nameofProductBuy)
    TextView productnameBuy;
    @BindView(R.id.nameofCoverBuy)
    TextView coverNameBuy;
    @BindView(R.id.coverdetailsBuy)
    TextView coverDetailsBuy;
    @BindView(R.id.premiumBuy)
    TextView premiumBuy;
    private String stkRequestId;
    private Object IOException;
    private JSONArray result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_purchase_cover);

        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);

        retrievePaCOverDetails();

        generateMerchantID();
        addBenefactorCheckbox.setChecked(false);




        btnBuyCover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checkConnection.isConnectedToInternet(PurchaseCoverActivity3.this)){
                    shownetworkDialog();
                }else {

                    try {
                        stkPushRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        addBenefactorCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked ==true){
                    benefactor2CardView.setVisibility(View.VISIBLE);
                }
                else if(isChecked==false){
                    benefactor2CardView.setVisibility(View.GONE);
                }
            }
        });

    }



    //checking internet connection
    private void shownetworkDialog() {
        final  AlertDialog.Builder builder;
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

    private void stkPushRequest() throws IOException, InterruptedException{


        String merchatid = merchantID;
        String merchatkey = merchantkey;
        String destshortcode = desthortCode ;
        String phonenumber = mobile_noBuy.getText().toString();
        String TransactionDesc =  TransactionDescription + cover_name;
        String TransactionID = TransactionId;
        String amount = product_premium;
        String asynccallback = asyncCallback  ;
        String request_seq_nr = merchatid;



        if (!validate()) {
            onStkPushFailed();
            return;
        }else if (validate())

            pushRequest(merchatid, merchatkey, destshortcode, phonenumber, TransactionDesc, TransactionID, amount,asynccallback,request_seq_nr);

    }

    //  Volley POST STK request
    private void pushRequest(String merchatid, String merchatkey, String destshortcode, String phonenumber, String TransactionDesc, String TransactionID, String amount, String asynccallback, String request_seq_nr) {
        // Tag used to cancel the request
        String cancel_req_tag = "push_request";

        progressDialog.setMessage("Requesting STk push ...");
        showDialog();

        JSONObject jsonp = new JSONObject();
        try {
            jsonp.put("merchatid", merchatid);
            jsonp.put("merchatkey", merchatkey);
            jsonp.put("destshortcode", destshortcode);
            jsonp.put("phonenumber",phonenumber);
            jsonp.put("TransactionDesc", TransactionDesc);
            jsonp.put("TransactionID", TransactionID);
            jsonp.put("amount", amount);
            jsonp.put("asynccallback", asynccallback);
            jsonp.put("request_seq_nr", request_seq_nr);
        } catch (JSONException e) {
            e.printStackTrace();
            String errorMsg = e.toString();
            Toast.makeText(getApplicationContext(),
                    errorMsg, Toast.LENGTH_LONG).show();
            btnBuyCover.setVisibility(View.VISIBLE);
        }

        //JasonObject Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, STK_PUSH_URL, jsonp,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "SDK Push Response: " + response.toString());

                        try {
                            String rStatus = response.getString("ResponseDescription");

                            if (rStatus.equals("Success. Request accepted for processing")) {
                                hideDialog();
//                                Toasty.success(getApplicationContext(),
//                                        "delay", Toast.LENGTH_LONG).show();

                                callcheckpaymentwait();

                            } else {
                                hideDialog();
                                String errorMsg = response.getString("ResponseDescription");
                                Toasty.warning(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();

                                onStkPushFailed();
                                // call progress dialog

                                btnBuyCover.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "SDKPush Error: " + error.getMessage());
                String errorMsg =error.getMessage();
                Toasty.warning(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                hideDialog();
                onStkPushFailed();
                btnBuyCover.setVisibility(View.VISIBLE);
            }
        }){ //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, cancel_req_tag);
    }

    private void callcheckpaymentwait() {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                mpesaGetRequest();
            }
        },15000);

    }

    private void mpesaGetRequest() {
        String cancel_req_tag = "mpesa_response";

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET,MPESA_URL_TO + merchantID,
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
                            getMpesaResponseParams(result);

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

    //transversing mpesa response jsonArray
    private void getMpesaResponseParams(JSONArray j) {
        if (j!=null) {
            //Traversing through all the items in the json array
            for (int i = 0; i < j.length(); i++) {
                try {
                    //Getting json object
                    JSONObject json = j.getJSONObject(i);
                    transaction_id = json.getString("transaction_id");
                    second_name = json.getString("second_name");
                    key_id = json.getString("key_id");
                    subscriber_nr = json.getString("subscriber_nr");
                    transaction_date = json.getString("transaction_date");
                    amount_received = json.getString("amount_received");
                    message_id = json.getString("message_id");
                    processor_id = json.getString("processor_id");
                    account_nr = json.getString("account_nr");
                    last_name = json.getString("last_name");
                    current_balance = json.getString("current_balance");
                    short_code = json.getString("short_code");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        if (processor_id == message_id){
        }
        else if ( processor_id != merchantID){

            try {
                progressDialog.setMessage("Retrying...");
                showDialog();

                stkPushRequest();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }



    //   Success and failure messages for STK push
    private void onStkPushFailed() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                Toasty.warning(getBaseContext(), "STK push Failed", Toast.LENGTH_SHORT, true).show();

            }
        },2000);
    }

    private void onStkPushSuccess() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                Toasty.success(getBaseContext(), "STK push Success", Toast.LENGTH_SHORT, true).show();

            }
        },2000);
    }

    // Retrieving pa cover Details
    private void retrievePaCOverDetails() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);
        if ( sharedPref != null) {

            product_name = sharedPref.getString("product_name", product_name);
            cover_id = sharedPref.getString("cover_id", cover_id);
            product_premium = sharedPref.getString("product_premium", product_premium);
            cover_details = sharedPref.getString("pa_cover_description", cover_details);
            cover_name = sharedPref.getString("cover_name", cover_name);
            TOKEN = sharedPref.getString("k",TOKEN);

        }
        productnameBuy.setText(product_name);
        coverNameBuy.setText(cover_name);
        coverDetailsBuy.setText(cover_details);
        premiumBuy.setText(product_premium);
    }
    //    input validation
    public boolean validate() {
        boolean valid = true;

        if (mobile_noBuy.getText().toString().isEmpty() || mobile_noBuy.equals("")){
            mobile_noBuy.setError("Mobile number can not be empty");
            valid = false;
        }
        if (krapinBuy.getText().toString().isEmpty()){
            krapinBuy.setError("KRA pin can not be empty");
            valid = false;

        }
        if (!declarationBuyCheckbox.isChecked()){
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toasty.warning(getBaseContext(), "Please acknowledge declaration to proceed", Toast.LENGTH_SHORT, true).show();

                }
            });
            btnBuyCover.setEnabled(false);
        }



        if (!termsandConditionsBuy.isChecked()){
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toasty.success(getBaseContext(), "Please acknowledge Terms and Conditions to proceed", Toast.LENGTH_SHORT, true).show();

                }
            });
            btnBuyCover.setEnabled(false);
        }


        else if (declarationBuyCheckbox.isChecked()){


            btnBuyCover.setEnabled(true);
        }
        else if (termsandConditionsBuy.isChecked()){

            btnBuyCover.setEnabled(true);
        }
        else {
            mobile_noBuy.setError(null);
            krapinBuy.setError(null);

        }

        return valid;
    }

    //generate merchant ID
    public void generateMerchantID(){
        Random rand = new Random();

        double y = (Math.random()*((1000000000-100000000)+1))+100000000;

        int x = (int) y;

        merchantID = String.valueOf(x);
    }

    //process Dialog
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.setMessage("Opening STK ...");
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
