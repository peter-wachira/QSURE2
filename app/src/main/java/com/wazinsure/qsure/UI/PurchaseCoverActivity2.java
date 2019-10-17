package com.wazinsure.qsure.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
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

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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


public class PurchaseCoverActivity2 extends AppCompatActivity{


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

        retrieveUserInformation();

        generateMerchantID();
        addBenefactorCheckbox.setChecked(false);


        btnBuyCover.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checkConnection.isConnectedToInternet(getApplicationContext())){
                    shownetworkDialog();
                }else {
                    if (!validate()) {
                        return;
                    } else {

                        confirmnpesano(mobile_noBuy.getText().toString());
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

        //date onclick Listeners

        setDoBTimeField();
        _dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                dobdatePickerDialog.show();
            }
        });
        setPAStartDateField();
        _startDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                startdatePickerDialog.show();
            }
        });

        setPAEndDAteField();
        _endDateText.setOnClickListener(new View.OnClickListener() {
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


    private void retrieveUserInformation() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "userRegistrationDetials", Context.MODE_PRIVATE);
        if ( sharedPref != null) {

            _firstnameText.setText(sharedPref.getString("first_name", "default"));
            _surnameText.setText(sharedPref.getString("last_name", "default"));
            _idnoText.setText(sharedPref.getString("id_no", "default"));
            mobile_noBuy.setText( sharedPref.getString("mobile_no","default" ));
            _emailText.setText(sharedPref.getString("email", "default"));
            _dobText.setText(sharedPref.getString("dob", "default"));
        }
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

    private void confirmnpesano(final String usermobno) {


        AlertDialog.Builder mpesanobuilder = new AlertDialog.Builder(PurchaseCoverActivity2.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        mpesanobuilder.setTitle("Confirm  mpesa number to pay " + "Ksh " + product_premium + " for " + cover_name);


        final EditText mpesa_input = new EditText(PurchaseCoverActivity2.this);
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


        AlertDialog.Builder mpesarequestretrybuilder = new AlertDialog.Builder(PurchaseCoverActivity2.this, android.R.style.ThemeOverlay_Material_Dialog_Alert);

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
                progressDialog.setMessage("Retrying...");
                showDialog();
                mpesaGetRequest();

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

    //transversing mpesa response jsonArray
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

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {
                                    Toasty.success(getApplicationContext(), "PA policy Generated Successfully ", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), MyPoliiesActivity.class);
                                    startActivity(intent);
                                }
                            });

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
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
