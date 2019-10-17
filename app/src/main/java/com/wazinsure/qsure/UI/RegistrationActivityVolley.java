package com.wazinsure.qsure.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.wazinsure.qsure.service.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class RegistrationActivityVolley extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_CUSTOMER = "https://demo.wazinsure.com:4443/api/customers" ;
    private String TOKEN;
    @BindView(R.id.firstnameReg)
    EditText _firstnameText;
    @BindView(R.id.surnameReg)
    EditText _surnameText;
    @BindView(R.id.id_no)
    EditText _idnoText;
    @BindView(R.id.dobReg)
    EditText _dobText;
    @BindView(R.id.genderReg)
    EditText _genderText;
    @BindView(R.id.mobileNoText)
    EditText _mobilenoText;
    @BindView(R.id.profile_url)
    ImageView _profileurlText;
    @BindView(R.id.email)
    EditText _emailText;
    @BindView(R.id.pinReg)
    EditText _pinText;
    @BindView(R.id.btn_register)
    Button _registerButton;
    @BindView(R.id.signin)
    TextView login_link;
    Uri resultUri;
    private DatePickerDialog dobdatePickerDialog;
    CheckConnection checkConnection;
    DatabaseHelper mDatabaseHelper;
    private RequestQueue requestQueue;
    ProgressDialog progressDialog;
    private static final String URL_FOR_REGISTRATION = "https://demo.wazinsure.com:4443/auth/register";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        mDatabaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);


        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivityVolley.class);
                startActivity(intent);
            }
        });


        _registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checkConnection.isConnectedToInternet(RegistrationActivityVolley.this)){
                    shownetworkDialog();
                }else {


                    try {


                        registerNewUser();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // picking profile image from gallery
        _profileurlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfileImage();
            }
        });


        setDateTimeField();
        _dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                dobdatePickerDialog.show();
            }
        });




        _genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] listItems = {"Male", "Female"};

                AlertDialog.Builder builderg = new AlertDialog.Builder(RegistrationActivityVolley.this);
                builderg.setTitle("Choose gender");

                int checkedItem = 0; //this will checked the item when user open the dialog
                builderg.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _genderText.setText(listItems[which]);
                    }
                });

                builderg.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builderg.create();
                dialog.show();
            }
        });

    }



    //checking internet connection
    public void shownetworkDialog() {
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


    //choose profile image

    private void chooseProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }


    //set date dialog
    private void setDateTimeField() {
        _dobText.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        dobdatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                _dobText.setError(null);
                _dobText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    //registering a new user

    public void registerNewUser() throws IOException, InterruptedException{
        String firstname = _firstnameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String gender = _genderText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String username = _idnoText.getText().toString();
        String mobile_no = _mobilenoText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _pinText.getText().toString();
        String dob = _dobText.getText().toString();
        String fullname = firstname +" "+ surname;


        if (!validate()) {
            onRegistrationFailed();
            return;
        }

        // On complete call either onLoginSuccess or onLoginFailed

        register(fullname, id_no, mobile_no, email, resultUri, username, password);

    }


    //volley request for registering customer
    public void register( final String fullname,final String id_no,final String mobile_no, final String email, final Uri photo_url, final String username, final String password) {
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
                            onRegistrationSuccess();
                        }


                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),"Error occurred. Please, contact ICT "+errorMsg, Toast.LENGTH_LONG).show();

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
                Toasty.warning(getApplicationContext(),"Error occurred. Please, contact the ICT "+error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("photo_url", String.valueOf(photo_url));
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
        String gender = _genderText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String username = _idnoText.getText().toString();
        String mobile_no = _mobilenoText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _pinText.getText().toString();
        String dob = _dobText.getText().toString();
        String fullname = firstname +" "+ last_name;

        addNewCustomerRequest( firstname,  last_name, id_no,  dob, mobile_no,  email,gender, resultUri);

    }


    public void addNewCustomerRequest( final String first_name, final String last_name, final String id_no, final String dob, final String mobile_no, final String email,final String gender, final Uri photo_url) {
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
                            onRegistrationSuccess();
                        }

                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toasty.warning(getApplicationContext(),"An Error occurred "+errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Customer Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(),"Please ensure you correct values  "+error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("gender",gender);
                params.put("photo_url", String.valueOf(photo_url));

                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReqAdd,cancel_req_tag);
    }



    //adding firstname lastname and ID to DB
    public void addDbData() {
        String firstname = _firstnameText.getText().toString();
        String last_name = _surnameText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String mobile_no = _mobilenoText.getText().toString();
        String email = _emailText.getText().toString();
        String dob = _dobText.getText().toString();

        if (firstname.length() !=0 && id_no.length() !=0 && last_name.length() !=0){
            boolean isInserted = mDatabaseHelper.insertData(id_no,firstname,last_name);
        }
    }

    public void  saveUserDetails(){
        SharedPreferences sharedPref =  this.getSharedPreferences(
                "userRegistrationDetials", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("first_name",_firstnameText.getText().toString());
        editor.putString("last_name",_surnameText.getText().toString() );
        editor.putString("id_no",_idnoText.getText().toString());
        editor.putString("mobile_no",_mobilenoText.getText().toString());
        editor.putString("email", _emailText.getText().toString() );
        editor.putString("dob",_dobText.getText().toString());
        editor.commit();


    }

    //validating user input
    public boolean validate() {
        boolean valid = true;

        String pin = _pinText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String dob = _dobText.getText().toString();

//        if (pin.isEmpty() || pin.length() < 4 || pin.length() > 4) {
//            _pinText.setError("between 0 and 4 numeric characters");
//            valid = false;
//        }
        if (id_no.isEmpty() || id_no.equals("")){
            _idnoText.setError("Id number can not be empty");
            valid = false;
        }
        if (dob.isEmpty()){
            _dobText.setError("Date of Birth can not be empty");
            valid = false;

        }

        else {
            _pinText.setError(null);
            _idnoText.setError(null);
            _dobText.setError(null);
        }

        return valid;
    }

    //success and failure messages nb
    public void onRegistrationSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.success(getBaseContext(), "Registration Successful!", Toast.LENGTH_LONG, true).show();

            }
        });
    }

    public void onRegistrationFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.warning(getBaseContext(), "Registration Failed", Toast.LENGTH_SHORT, true).show();

            }
        });
        _registerButton.setEnabled(true);
    }



    //process Dialog
    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.setMessage("Registering you ...");
        progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
