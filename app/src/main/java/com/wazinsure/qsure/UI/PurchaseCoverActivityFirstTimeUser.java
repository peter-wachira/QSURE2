package com.wazinsure.qsure.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.fonts.Font;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wazinsure.qsure.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;


import com.android.volley.toolbox.StringRequest;


import com.wazinsure.qsure.helpers.DatabaseHelper;
import com.wazinsure.qsure.service.AppSingleton;
import com.wazinsure.qsure.service.SaveSharedPreference;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;


import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;


public class PurchaseCoverActivityFirstTimeUser extends AppCompatActivity implements OnClickListener {


    private static final String URL_FOR_LOGIN = "https://demo.wazinsure.com:4443/auth/login";

    //declarations
    CheckConnection checkConnection;

    private static final String URL_FOR_CUSTOMER = "https://demo.wazinsure.com:4443/api/customers";
    private static final String URL_FOR_PA_POLICY = "https://demo.wazinsure.com:4443/api/papolicies";
    private static final String URL_FOR_REGISTRATION = "https://demo.wazinsure.com:4443/auth/register";

    private String TOKEN;
    @BindView(R.id.firstnameReg)
    EditText _firstnameText;
    @BindView(R.id.surnameReg)
    EditText _surnameText;
    @BindView(R.id.id_no)
    EditText _idnoText;
    @BindView(R.id.dobReg)
    EditText _dobText;
    @BindView(R.id.startDateBuy)
    EditText _startDateText;
    @BindView(R.id.endDateBuy)
    EditText _endDateText;

    @BindView(R.id.previousCoverYes)
    RadioButton _previousCoverYes;
    @BindView(R.id.previousCoverNo)
    RadioButton _previousCoverNo;
    @BindView(R.id.accidentPreviousYes)
    RadioButton _accidentPreviousYes;
    @BindView(R.id.accidentPreviousNo)
    RadioButton _accidentPreviousNo;
    @BindView(R.id.physicaldefectYes)
    RadioButton _physicaldefectYes;
    @BindView(R.id.physicaldefectNo)
    RadioButton _physicaldefectNo;
    @BindView(R.id.goodhealthYes)
    RadioButton _goodhealthYes;
    @BindView(R.id.goodhealthNo)
    RadioButton _goodhealthNo;
    @BindView(R.id.previousCoverRadioGroup)
    RadioGroup previousCoverRadioGroup;

    @BindView(R.id.physicaldefectRadiogroup)
    RadioGroup physicaldefectRadiogroup;

    @BindView(R.id.previousaccidentRadioGroup)
    RadioGroup previousaccidentsRadioGroup;
    @BindView(R.id.goodHealthRadioGroup)
    RadioGroup goodHealthRadioGroup;

    @BindView(R.id.profile_url)
    ImageView _profileurlText;
    @BindView(R.id.email)
    EditText _emailText;

    Uri resultUri;
    private DatePickerDialog dobdatePickerDialog;
    private DatePickerDialog startdatePickerDialog;
    private DatePickerDialog enddatePickerDialog;


    DatabaseHelper mDatabaseHelper;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    Dialog dialog;
    //bind purchase Cover
    private static final String TAG = "PurchaseCoverActivity";
    private static final String MPESA_URL_TO = "https://demo.wazinsure.com:4443/api/mpesaresponsesdto/";
    SharedPreferences sharedpreferences;
    String product_name;
    String cover_name;
    String cover_details;
    String product_premium;
    String cover_id;
    String declarationGoodHealth ="";
    String declarationNotinGoodHealth ="";
    String previous_underwritter ="";
    String previous_Pacover ="";
    String physical_defect ="";
    String no_physical_defect ="";
    String previous_accidents  ="";
    String no_previous_accidents  ="";
    private String applicant_name;
    private String applicant_phone_number;


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
    String mpesa_response_id;

    String desthortCode = "533419";
    String merchantID;
    String merchantkey = "12345";
    String asyncCallback = "http://127.0.0.1:8989/stkpush";
    String STK_PUSH_URL = "http://transactions.wazinsure.com:1780/todaytech/express/request";
    String TransactionDescription = "Payment for ";
    String TransactionId = "PA Cover";


    @BindView(R.id.termsandCondtitionsBuy)
    CheckBox termsandConditionsBuy;

    @BindView(R.id.previousaccidentsBuy)
    EditText previousaccidentBuy;
    @BindView(R.id.infirmityBuy)
    EditText infirmityBuy;
    @BindView(R.id.previousCoverDescription)
    EditText previousCoverDescriptionBuy;
    @BindView(R.id.badHealthDescription)
    EditText badHealthDescriptionBuy;
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

    RegistrationActivityVolley registrationActivityVolley;
    private String start_date;
    private String end_date;
    private String security_pin;
    private String user_gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_purchase_cover_first_time_user);
        ButterKnife.bind(this);

        mDatabaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);
        dialog = new  Dialog(this);

        retrievePaCOverDetails();
        generateMerchantID();
        addBenefactorCheckbox.setChecked(false);
        //add benefactor onclick listener
        btnBuyCover.setOnClickListener(this);
        addBenefactorCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked == true) {
                    benefactor2CardView.setVisibility(View.VISIBLE);
                } else if (isChecked == false) {
                    benefactor2CardView.setVisibility(View.GONE);
                }
            }
        });


        //date onclick Listeners

        setDoBTimeField();
        _dobText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                dobdatePickerDialog.show();
            }
        });
        setPAStartDateField();
        _startDateText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                startdatePickerDialog.show();
            }
        });

        setPAEndDAteField();
        _endDateText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                enddatePickerDialog.show();
            }
        });

        //RadioGroup Listeners

        previousCoverRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (_previousCoverYes.isChecked()) {
                    previousCoverDescriptionBuy.setVisibility(View.VISIBLE);
                    previous_Pacover = "Yes";
                    previous_underwritter = previousCoverDescriptionBuy.getText().toString();
                }
                else if (_previousCoverNo.isChecked()) {
                    previousCoverDescriptionBuy.setVisibility(View.GONE);
                }
            }
        });

        goodHealthRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (_goodhealthYes.isChecked()) {
                    badHealthDescriptionBuy.setVisibility(View.GONE);
                    declarationGoodHealth = "Yes";
                }
                else if (_goodhealthNo.isChecked()) {
                    badHealthDescriptionBuy.setVisibility(View.VISIBLE);
                    declarationGoodHealth = "No";
                    declarationNotinGoodHealth = badHealthDescriptionBuy.getText().toString();
                }
            }
        });

        previousaccidentsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (_accidentPreviousYes.isChecked()) {
                    previousaccidentBuy.setVisibility(View.VISIBLE);
                    previous_accidents = previousCoverDescriptionBuy.getText().toString();

                } else if (_accidentPreviousNo.isChecked()) {
                    previousaccidentBuy.setVisibility(View.GONE);
                    no_previous_accidents ="Yes";
                }
            }
        });

        physicaldefectRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (_physicaldefectYes.isChecked()) {
                    infirmityBuy.setVisibility(View.VISIBLE);
                    physical_defect = infirmityBuy.getText().toString();

                } else if (_physicaldefectNo.isChecked()) {
                    infirmityBuy.setVisibility(View.GONE);
                    no_physical_defect = "Yes";
                }
            }
        });

    }


    //Date Pickers
    //set DoB date dialog
    private void setDoBTimeField() {
        _dobText.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        dobdatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                _dobText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    //set start date dialog
    private void setPAStartDateField() {
        _startDateText.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        startdatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                _startDateText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    //set pa end date dialog
    private void setPAEndDAteField() {
        _endDateText.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        enddatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                _endDateText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }


    // Initializing Purchase Process
    private void initPurchase() throws IOException, InterruptedException {
        if (!checkConnection.isConnectedToInternet(PurchaseCoverActivityFirstTimeUser.this)) {
            shownetworkDialog();
        } else {

            if (!validate()) {
                return;
            } else {

                confirmnpesano(mobile_noBuy.getText().toString());

            }

        }
    }

    //confirm Mpesa Number Dialog
    private void confirmnpesano(final String usermobno) {


        AlertDialog.Builder mpesanobuilder = new AlertDialog.Builder(PurchaseCoverActivityFirstTimeUser.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        mpesanobuilder.setTitle("Confirm  mpesa number to pay " + "Ksh " + product_premium + " for " + cover_name);


        final EditText mpesa_input = new EditText(PurchaseCoverActivityFirstTimeUser.this);
        ImageView imageView = new ImageView(this);

        LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 300);
        imageView.setImageResource(R.drawable.checkout_image);
        imageView.setLayoutParams(image_params);




        mpesa_input.setText(usermobno);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(35, 50, 35, 20);
        mpesa_input.setLayoutParams(lp);



        layout.addView(imageView);
        layout.addView(mpesa_input);
        mpesanobuilder.setView(layout);


        mpesanobuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mobile_noBuy.setText(mpesa_input.getText().toString());

                try {
                    stkPushRequest();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        mpesanobuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = mpesanobuilder.create();
        dialog.show();
    }


    private void mpesaRetryDialog() {


        AlertDialog.Builder mpesarequestretrybuilder = new AlertDialog.Builder(PurchaseCoverActivityFirstTimeUser.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);

        mpesarequestretrybuilder.setTitle("Do you want to Retry Purchase?");


        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(this);

        LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 300);
        imageView.setImageResource(R.drawable.industry_finance);
        imageView.setLayoutParams(image_params);


        layout.addView(imageView);



        mpesarequestretrybuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.setMessage("Retrying...");
                        showDialog();
                        mpesaGetRequest();
                    }
                }, 3000);
            }
        });

        mpesarequestretrybuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog = mpesarequestretrybuilder.create();
        dialog.show();
    }


    // Start of STK push
    private void stkPushRequest() throws IOException, InterruptedException {
        String merchatid = merchantID;
        String merchatkey = merchantkey;
        String destshortcode = desthortCode;
        String phonenumber = mobile_noBuy.getText().toString();
        String TransactionDesc = TransactionDescription + cover_name;
        String TransactionID = TransactionId;
        String amount = product_premium;
        String asynccallback = asyncCallback;
        String request_seq_nr = merchatid;


        pushRequest(merchatid, merchatkey, destshortcode, phonenumber, TransactionDesc, TransactionID, amount, asynccallback, request_seq_nr);

    }

    // Volley POST STK request
    private void pushRequest(String merchatid, String merchatkey, String destshortcode, String phonenumber, String TransactionDesc, String TransactionID, String amount, String asynccallback, String request_seq_nr) {
        // Tag used to cancel the request
        String cancel_req_tag = "push_request";
        hideDialog();
        progressDialog.setMessage("Requesting STk push ...");
        showDialog();

        JSONObject jsonp = new JSONObject();
        try {

            jsonp.put("merchatid", merchatid);
            jsonp.put("merchatkey", merchatkey);
            jsonp.put("destshortcode", destshortcode);
            jsonp.put("phonenumber", phonenumber);
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
                        hideDialog();
                        try {
                            String rStatus = response.getString("ResponseDescription");

                            if (rStatus.equals("Success. Request accepted for processing")) {

                                callcheckpaymentwait();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e(TAG, "SDKPush Error: " + error.getMessage());
                String errorMsg = error.getMessage();
                Toasty.warning(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                onStkPushFailed();

            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, cancel_req_tag);
    }


    // Check payment wait method
    private void callcheckpaymentwait() {

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                progressDialog.setMessage("Checking Payment...");
                progressDialog.show();
                mpesaGetRequest();
            }
        }, 15000);

    }

    // Mpesa GET REquest
    private void mpesaGetRequest() {
        String cancel_req_tag = "mpesa_response";

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MPESA_URL_TO + merchantID,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "MerchantIDD" + merchantID);

                        hideDialog();
                        try {
                            JSONObject j = null;
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);
                            Log.e(TAG, "Mpesa GetRequest Response:" + j.getString("status"));

                            if (j.getString("status").equals("success")){

                                //Storing the Array of JSON String to our JSON Array
                                result = j.getJSONArray("data");
                                //Calling method getCustomers to get the customer from the JSON Array
                                getMpesaResponseParams(result);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        JSONObject j = null;
                        //Parsing the fetched Json String to JSON Object
                        Log.e(TAG, "Mpesa Response Error: " + error.getMessage());
                        Toasty.warning(getApplicationContext(), "Mpesa Response Error:  " + error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
                        mpesaRetryDialog();
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


    // Transversing mpesa response jsonArray
    private void getMpesaResponseParams(JSONArray j) {
        if (j != null) {
            //Traversing through all the items in the json array
            for (int i = 0; i < j.length(); i++) {
                try {
                    //Getting json object
                    JSONObject json = j.getJSONObject(i);
                    mpesa_response_id = json.getString("mpesa_response_id");
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

                    if (processor_id.equals(merchantID)) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                progressDialog.setMessage("Generating Policy...");
                                progressDialog.show();
                                generatePaPolicy();
                            }
                        }, 2000);


                    } else if (!processor_id.equals(merchantID)) {

                        Toasty.warning(getApplicationContext(), "Error Processor_id did not match merchantID " , Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private void generatePaPolicy() {

        String policy_no = merchantID.substring(0,6);
        String applicant_dob = _dobText.getText().toString();
        applicant_name = _firstnameText.getText().toString() + " " + _surnameText.getText().toString();

        String applicant_idno = _idnoText.getText().toString();
        applicant_phone_number = mobile_noBuy.getText().toString();
        String applicant_email = _emailText.getText().toString();
        start_date = _startDateText.getText().toString();
        end_date = _endDateText.getText().toString();

        String declaration_goodhealth = declarationGoodHealth;
        String notin_goodhealth = declarationNotinGoodHealth;
        String previous_pacovers = previous_Pacover;
        String previous_paunderwritter = previous_underwritter;





        paVolleyRequest(policy_no,cover_name, product_name, product_premium, applicant_dob, applicant_name, applicant_idno, applicant_phone_number, applicant_email, start_date, end_date, declaration_goodhealth, notin_goodhealth, previous_pacovers, previous_paunderwritter,physical_defect,no_physical_defect,previous_accidents,no_previous_accidents);

    }

    private void paVolleyRequest(String policy_no,String cover_name, String product_name, String premium, String applicant_dob, String applicant_name, String applicant_idno, String applicant_phone_number, String applicant_email, String start_date, String end_date, String declaration_goodhealth, String notin_goodhealth, String previous_pacovers, String previous_paunderwritter,String physical_defect, String no_physical_defect,String previous_accidents, String no_previous_accidents ) {


        String cancel_req_tag = "pa_policy";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_PA_POLICY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "PA Policy Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    if (status.equals("success")) {

                        {

                            addRegistrationDetails("","",_idnoText.getText().toString());

                            logout();
                        }

                    } else {




                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(), "PA policy error. Please, contact the ICT " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("policy_no", policy_no);
                params.put("cover_name", cover_name);
                params.put("product_name", product_name);
                params.put("premium", premium);
                params.put("applicant_dob", applicant_dob);
                params.put("applicant_name", applicant_name);
                params.put("applicant_idno", applicant_idno);
                params.put("applicant_email", applicant_email);
                params.put("start_date", start_date);
                params.put("applicant_phone_number", applicant_phone_number);
                params.put("end_date", end_date);
                params.put("declaration_goodhealth", declaration_goodhealth);
                params.put("notin_goodhealth", notin_goodhealth);
                params.put("previous_pacovers", previous_pacovers);
                params.put("previous_paunderwritter", previous_paunderwritter);
                params.put("physical_defect", physical_defect);
                params.put("no_physical_defect", no_physical_defect);
                params.put("previous_accidents", previous_accidents);
                params.put("no_previous_accidents", no_previous_accidents);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + TOKEN);//put your token here
                return headers;

            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);


    }




    private void addRegistrationDetails(final String pin, final String gender, final String id_no ) {

        AlertDialog.Builder userRegistrationBuilder = new AlertDialog.Builder(PurchaseCoverActivityFirstTimeUser.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        userRegistrationBuilder.setTitle("Confirm  details to Register as a User");

        Context dialogContext = userRegistrationBuilder.getContext();
        LinearLayout layout = new LinearLayout(dialogContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText pin_input = new EditText(dialogContext);
        final EditText gender_input = new EditText(dialogContext);
        final EditText id_input = new EditText(dialogContext);
        ImageView imageView = new ImageView(dialogContext);


        LinearLayout.LayoutParams image_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 300);
        imageView.setImageResource(R.drawable.industry_finance);
        imageView.setLayoutParams(image_params);


        gender_input.setHint("Gender ");
        gender_input.setText(gender);
        LinearLayout.LayoutParams gender_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        gender_params.setMargins(35, 30, 35, 15);
        gender_input.setLayoutParams(gender_params);


        pin_input.setHint("Security Pin");
        pin_input.setText(pin);
        LinearLayout.LayoutParams  securitypin_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT );

        securitypin_params.setMargins(35, 30, 35, 15);

        pin_input.setLayoutParams(securitypin_params);

        id_input.setHint("Id Number ");
        id_input.setText(id_no);
        LinearLayout.LayoutParams idno_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        idno_params.setMargins(35, 30, 35, 15);
        id_input.setLayoutParams(idno_params);


        layout.addView(imageView);
        layout.addView(pin_input);
        layout.addView(gender_input);
        layout.addView(id_input);

        userRegistrationBuilder.setView(layout);

        userRegistrationBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                security_pin = pin_input.getText().toString();
                user_gender = gender;
                _idnoText.setText(id_input.getText().toString());



                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                registerNewUser();
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }
                    });


            }
        });

        userRegistrationBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        AlertDialog dialog = userRegistrationBuilder.create();
        dialog.show();
    }



    private void logout() {
        SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
        sharedpreferences = getSharedPreferences("mypref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("n");
        editor.remove("k");
        editor.commit();

        removeCurrentUser();

    }

    private void removeCurrentUser() {
        SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
        sharedpreferences = getSharedPreferences("userRegistrationDetials",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("first_name");
        editor.remove("last_name");
        editor.remove("id_no");
        editor.remove("mobile_no");
        editor.remove("email");
        editor.remove("dob");
        editor.commit();
    }

    private void saveUserID() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("k",TOKEN);
        editor.putString("n", _idnoText.getText().toString() );
        editor.commit();
    }


    private void login( final String username, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();



                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    String tokn = jObj.getString("token");
                    TOKEN = tokn;
                    if (status.equals("success")) {
                        {
                            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);

                            saveUserID();
                            startActivity(intent);
                        }

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toasty.warning(getApplicationContext(),"Error occurred. Please, contact ICT "+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(),"Please ensure your USERNAME and PASSWORD are correct "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }




    //registering a new user

    public void registerNewUser() throws IOException, InterruptedException{

        String firstname = _firstnameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String gender = user_gender;
        String id_no = _idnoText.getText().toString();
        String username = _idnoText.getText().toString();
        String mobile_no = mobile_noBuy.getText().toString();
        String email = _emailText.getText().toString();
        String password = security_pin;
        String dob = _dobText.getText().toString();
        String fullname = firstname +" "+ surname;

        register(fullname, id_no, mobile_no, email, username, password);

    }


    public void register( final String fullname,final String id_no,final String mobile_no, final String email, final String username, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    String token = jObj.getString("token");
                    TOKEN = token;
                    if (status.equals("success")) {

                        {
                            addNewCustomer();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),"User Registration Error "+errorMsg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(),"User Registration Error "+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })



        {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("fullname", fullname);
                params.put("id_no", id_no);
                params.put("mobile_no", mobile_no);
                params.put("password", password);
                params.put("username", username);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }

    //adding part of the user's KYC
    public void addNewCustomer() throws IOException, InterruptedException{
        String firstname = _firstnameText.getText().toString();
        String last_name = _surnameText.getText().toString();
        String gender = user_gender;
        String id_no = _idnoText.getText().toString();
        String username = _idnoText.getText().toString();
        String mobile_no = mobile_noBuy.getText().toString();
        String email = _emailText.getText().toString();
        String password = security_pin;
        String dob = _dobText.getText().toString();
        String fullname = firstname +" "+ last_name;

        addNewCustomerRequest( firstname,  last_name, id_no,  dob, mobile_no,  email,gender);

    }

    public void addNewCustomerRequest( final String first_name, final String last_name, final String id_no, final String dob, final String mobile_no, final String email,final String gender) {
        // Tag used to cancel the request

        String cancel_req_tag = "customer";
        progressDialog.setMessage("Creating Customer Record...");
        showDialog();

        StringRequest strReqAdd = new StringRequest(Request.Method.POST,
                URL_FOR_CUSTOMER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Customer Response: " + response.toString());
                hideDialog();

                try {

                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    String msg = jObj.getString("message");


                    if (status.equals("success")) {
                        {
                            addDbData();
                            saveUserDetails();
                            Toasty.success(getApplicationContext(),"Registration Successful", Toast.LENGTH_LONG).show();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toasty.warning(getApplicationContext(),"Adding Customer Error"+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Customer Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(),"Adding Customer Error"+error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+TOKEN);//put your token here
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                // Posting params to customer url
                Map<String, String> params = new HashMap<>();
                params.put("first_name",first_name);
                params.put("last_name",last_name);
                params.put("id_no",id_no);
                params.put("dob",dob);
                params.put("mobile_no",mobile_no);
                params.put("email",email);
                params.put("gender",email);

                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReqAdd,cancel_req_tag);
    }


    public void  saveUserDetails(){
        SharedPreferences sharedPref =  this.getSharedPreferences(
                "userRegistrationDetials", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("first_name",_firstnameText.getText().toString());
        editor.putString("last_name",_surnameText.getText().toString() );
        editor.putString("id_no",_idnoText.getText().toString());
        editor.putString("mobile_no",mobile_noBuy.getText().toString());
        editor.putString("email", _emailText.getText().toString() );
        editor.putString("dob",_dobText.getText().toString());
        editor.commit();


    }



    //adding firstname lastname and ID to DB
    public void addDbData() {
        String firstname = _firstnameText.getText().toString();
        String last_name = _surnameText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String mobile_no = mobile_noBuy.getText().toString();
        String email = _emailText.getText().toString();
        String dob = _dobText.getText().toString();

        if (firstname.length() !=0 && id_no.length() !=0 && last_name.length() !=0){
            boolean isInserted = mDatabaseHelper.insertData(id_no,firstname,last_name);
        }











        login(_idnoText.getText().toString(),security_pin);
    }


    // Success and failure messages for STK push
    private void onStkPushFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.warning(getBaseContext(), "STK push Failed", Toast.LENGTH_SHORT, true).show();

            }
        });
    }

    private void onStkPushSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.success(getBaseContext(), "STK push Success", Toast.LENGTH_SHORT, true).show();

            }
        });
    }

    //generate merchant ID
    public void generateMerchantID() {
        Random rand = new Random();

        double y = (Math.random() * ((1000000000 - 100000000) + 1)) + 100000000;

        int x = (int) y;

        merchantID = String.valueOf(x);

    }

    //input validation
    public boolean validate() {
        boolean valid = true;

        String id_no = _idnoText.getText().toString();
        String dob = _dobText.getText().toString();


//        if ( !_goodhealthYes.isChecked() || !_goodhealthNo.isChecked()) {
//            badHealthDescriptionBuy.setVisibility(View.VISIBLE);
//            badHealthDescriptionBuy.setError("Please confirm your health status");
//            valid = false;
//        }
//
//         else if ( !_accidentPreviousYes.isChecked() || !_accidentPreviousYes.isChecked()) {
//            previousCoverDescriptionBuy.setVisibility(View.VISIBLE);
//            previousCoverDescriptionBuy.setError("Please confirm previous accidents");
//
//            valid = false;
//        }
//        else if ( !_physicaldefectYes.isChecked() || !_physicaldefectYes.isChecked()) {
//            infirmityBuy.setVisibility(View.VISIBLE);
//            infirmityBuy.setError("Please confirm your health status");
//            valid = false;
//        }


         if (id_no.isEmpty()) {
            _idnoText.setError("Id number can not be empty");
            valid = false;
        } else if (dob.isEmpty() || dob.equals("")) {
            _dobText.setError("Date of Birth can not be empty");
            valid = false;

        } else if (mobile_noBuy.getText().toString().isEmpty()) {
            mobile_noBuy.setError("Mobile Number can not be empty");
            valid = false;
        }

        else if (_startDateText.getText().toString().isEmpty()) {
            _startDateText.setError("Have you added a start Date for the Cover?");
            valid = false;
        } else if (_endDateText.getText().toString().isEmpty()) {
            _endDateText.setError("Have you added an End Date for the Cover?");
            valid = false;
        } else if (!declarationBuyCheckbox.isChecked()) {
            declarationBuyCheckbox.setError("Please Confirm Declaration to Proceed");
            Toasty.warning(getBaseContext(), "Please Confirm Declaration to Proceed", Toast.LENGTH_LONG, true).show();
            valid = false;
        } else if (!termsandConditionsBuy.isChecked()) {
            termsandConditionsBuy.setError("Please Accept Terms and Conditions to Proceed");
            Toasty.warning(getBaseContext(), "Please Accept Terms and Conditions to Proceed", Toast.LENGTH_LONG, true).show();

            valid = false;
        } else {
            badHealthDescriptionBuy.setError(null);
             previousCoverDescriptionBuy.setError(null);
             infirmityBuy.setError(null);
            _idnoText.setError(null);
            _dobText.setError(null);
            _startDateText.setError(null);
            _endDateText.setError(null);
            declarationBuyCheckbox.setError(null);
            termsandConditionsBuy.setError(null);

        }

        return valid;
    }

    // Retrieving pa cover Details
    private void retrievePaCOverDetails() {


        SharedPreferences sharedPref = this.getSharedPreferences(
                "displayPref", Context.MODE_PRIVATE);

        if (sharedPref != null) {
            product_name = sharedPref.getString("product_name_display", product_name);
            cover_id = sharedPref.getString("cover_id_display", cover_id);
            product_premium = sharedPref.getString("product_premium_display", product_premium);
            cover_details = sharedPref.getString("cover_details_display", cover_details);
            cover_name = sharedPref.getString("cover_name_display", cover_name);
            String token = sharedPref.getString("k", TOKEN);
            TOKEN = token;

        }
        productnameBuy.setText(product_name);
        coverNameBuy.setText(cover_name);
        coverDetailsBuy.setText(cover_details);
        premiumBuy.setText(product_premium);
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

    //process Dialog
        public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    // OnClick Method
    @Override
    public void onClick(View view) {
        if (view == btnBuyCover) {
            try {
                initPurchase();

            } catch (java.io.IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}
