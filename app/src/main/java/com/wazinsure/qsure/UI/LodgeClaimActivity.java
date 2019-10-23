package com.wazinsure.qsure.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.helpers.DatabaseHelper;
import com.wazinsure.qsure.service.AppSingleton;

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

public class LodgeClaimActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LodgeClaimActivity";
    private String claimReferenceNo;
    ProgressDialog progressDialog;
    DatabaseHelper mDatabaseHelper;
    private RequestQueue requestQueue;
    CheckConnection checkConnection;
    private DatePickerDialog claimDatePickerDialog;

    Dialog dialog;

    @BindView(R.id.policyNumberClaim)
    EditText policyNumberClaim;
    @BindView(R.id.claimDescriptionClaim)
    EditText claimDescriptionClaim;
    @BindView(R.id.claimEstimateAmountClaim)
    EditText claimEstimateAmountClaim;
    @BindView(R.id.claimDateClaim)
    EditText claimDateClaim;
    @BindView(R.id.surnameReg)
    EditText surnameRegClaim;
    @BindView(R.id.firstnameReg)
    EditText firstnameRegClaim;
    @BindView(R.id.emailClaim)
    EditText emailClaim;
    @BindView(R.id.mobile_noClaim)
    EditText mobile_noClaim;
    @BindView(R.id.id_noClaim)
    EditText id_noClaim;
    @BindView(R.id.termsandCondtitionsCheckboxClaim)
    CheckBox termsandCondtitionsCheckboxClaim;
    @BindView(R.id.declarationClaimCheckbox)
    CheckBox declarationClaimCheckbox;
    @BindView(R.id.btn_Claim)
    Button btnSubmitClaim;
    private String URL_FOR_PA_ClAIM ="https://demo.wazinsure.com:4443/api/paclaims";
    private String TOKEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lodge_claim);
        ButterKnife.bind(this);

        mDatabaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);
        dialog = new Dialog(this);

        retrieveToken();
        btnSubmitClaim.setOnClickListener(this);



        setClaimDateField();
        claimDateClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                claimDatePickerDialog.show();
            }
        });

    }

    private void setClaimDateField() {
        claimDateClaim.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        claimDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                claimDateClaim.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }


    private void initClaim() throws IOException, InterruptedException {
        if (!checkConnection.isConnectedToInternet(LodgeClaimActivity.this)) {
            shownetworkDialog();
        } else {

            if (!validate()) {
                return;
            }
            else {
                submitClaimPostMethod();
            }

        }
    }

    private void submitClaimPostMethod() {
        String claim_refno = claimReferenceNo;
        String claim_type = "Personal Accident";
        String claim_description = claimDescriptionClaim.getText().toString();
        String claim_currency = "Ksh";
        String claim_est_amount = claimEstimateAmountClaim.getText().toString();
        String claim_date = claimDateClaim.getText().toString();
        String policy_no = policyNumberClaim.getText().toString();
        String applicant_name = firstnameRegClaim.getText().toString() +" "+ surnameRegClaim.getText().toString();
        String applicant_idno = id_noClaim.getText().toString();
        String applicant_phone_number = mobile_noClaim.getText().toString();
        String applicant_email = emailClaim.getText().toString();



        paCaimVolleyRequest(claim_refno,claim_type,claim_description,claim_currency,claim_est_amount,claim_date,policy_no,applicant_name,applicant_idno,applicant_phone_number,applicant_email);

    }

    private void paCaimVolleyRequest(String claim_refno, String claim_type, String claim_description, String claim_currency, String claim_est_amount, String claim_date, String policy_no, String applicant_name, String applicant_idno, String applicant_phone_number, String applicant_email) {



        String cancel_req_tag = "pa_claim";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_PA_ClAIM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "PA Claim Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");

                    if (status.equals("success")) {

                        {
                            Toasty.success(getApplicationContext(), " Claim Submitted Successfully", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toasty.warning(getApplicationContext(), "Claim Submission Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "PA payment Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(), "Claim Submission Error. Please, contact the ICT " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("claim_refno", claim_refno);
                params.put("claim_type", claim_type);
                params.put("claim_description", claim_description);
                params.put("claim_currency", claim_currency);
                params.put("claim_est_amount", claim_est_amount);
                params.put("claim_date", claim_date);
                params.put("policy_no", policy_no);
                params.put("applicant_name", applicant_name);
                params.put("applicant_idno", applicant_idno);
                params.put("applicant_phone_number", applicant_phone_number);
                params.put("applicant_email", applicant_email);
                params.put("policy_no", policy_no);

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


    public boolean validate() {
        final boolean[] valid = {true};

        String id_no = id_noClaim.getText().toString();

        if (id_no.isEmpty()) {
            id_noClaim.setError("Id number can not be empty");
            valid[0] = false;
        }
         else if (mobile_noClaim.getText().toString().isEmpty()) {
            mobile_noClaim.setError("Mobile Number can not be empty");
            valid[0] = false;
        }

        else if (policyNumberClaim.getText().toString().isEmpty()) {
            policyNumberClaim.setError("Have you added a policy Number?");
            valid[0] = false;
        } else if (claimDescriptionClaim.getText().toString().isEmpty()) {
            claimDescriptionClaim.setError("Have you added a description for the claim?");
            valid[0] = false;
        }
        else if (claimEstimateAmountClaim.getText().toString().isEmpty()) {
            claimEstimateAmountClaim.setError("Have you added an Estimate amount for your Claim?");
            valid[0] = false;
        }
        else if (claimDateClaim.getText().toString().isEmpty()) {
            claimDateClaim.setError("Have you added the Claim Date?");
            valid[0] = false;
        }
        else if (!declarationClaimCheckbox.isChecked()) {
            declarationClaimCheckbox.setError("Please Confirm Declaration to Proceed");
            Toasty.warning(getBaseContext(), "Please Confirm Declaration to Proceed", Toast.LENGTH_LONG, true).show();
            valid[0] = false;
        } else if (!termsandCondtitionsCheckboxClaim.isChecked()) {
            termsandCondtitionsCheckboxClaim.setError("Please Accept Terms and Conditions to Proceed");
            Toasty.warning(getBaseContext(), "Please Accept Terms and Conditions to Proceed", Toast.LENGTH_LONG, true).show();

            valid[0] = false;
        } else {
            id_noClaim.setError(null);
            mobile_noClaim.setError(null);
            policyNumberClaim.setError(null);
            claimDescriptionClaim.setError(null);
            claimEstimateAmountClaim.setError(null);
            declarationClaimCheckbox.setError(null);
            termsandCondtitionsCheckboxClaim.setError(null);


        }

        return valid[0];
    }



    //generate merchant ID
    public void generateClaimRefNo() {
        Random rand = new Random();

        double y = (Math.random() * ((1000000000 - 100000000) + 1)) + 100000000;

        int x = (int) y;

        claimReferenceNo = String.valueOf(x);

        Log.d("ClaimRefNo Here",claimReferenceNo);
    }

    //input validation

    // Retrieving pa cover Details

    //Network connection Dialog
    private void shownetworkDialog(){
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


    private void retrieveToken() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "userRegistrationDetials", Context.MODE_PRIVATE);
        if ( sharedPref != null) {
          TOKEN = sharedPref.getString("k",TOKEN);
        }
    }

    // OnClick Method
    @Override
    public void onClick(View view) {
        if (view == btnSubmitClaim) {
            generateClaimRefNo();

            try {
                initClaim();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
