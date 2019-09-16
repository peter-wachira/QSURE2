package com.wazinsure.qsure.UI;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.wazinsure.qsure.constants.Constants;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.helpers.DatabaseHelper;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.firstnameReg)
    EditText _firstnameText;
    @BindView(R.id.surnameRegnameReg)
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        mDatabaseHelper = new DatabaseHelper(this);



        login_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        _registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!checkConnection.isConnectedToInternet(RegistrationActivity.this)){
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

    }






    private void setDateTimeField() {
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


    //checking internet connection
    private void shownetworkDialog() {
        final  AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog);
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

    private void chooseProfileImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            _profileurlText.setImageURI(resultUri);
        }
    }
    private void registerNewUser() throws IOException, InterruptedException{
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




        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.ThemeOverlay_AppCompat_Dialog);


        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering customer...");
        progressDialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        try {
                            register(fullname, id_no, mobile_no, email, resultUri, username, password);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },4000);

        addNewCustomer();
    }


    public boolean validate() {
        boolean valid = true;

        String pin = _pinText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String dob = _dobText.getText().toString();

        if (pin.isEmpty() || pin.length() < 4 || pin.length() > 4) {
            _pinText.setError("between 0 and 4 numeric characters");
            valid = false;
        }
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


    public void onRegistrationSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.success(getBaseContext(), "Registration Successful!", Toast.LENGTH_LONG, true).show();
                Toasty.success(getBaseContext(), "Logging you in", Toast.LENGTH_SHORT, true).show();

            }
        });
        Intent intent = new Intent( this, HomeActivity.class);
        startActivity(intent);
        finish();
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

    //    registering a  new user
    public void register(String fullname, String id_no, String mobile_no, String email, Uri profileurl, String username, String password) throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = Constants.REGISTRATION + "/register";
        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("fullname", fullname);
            postdata.put("id_no", id_no);
            postdata.put("mobile_no", mobile_no);
            postdata.put("email", email);
            postdata.put("profileurl", profileurl);
            postdata.put("username", username);
            postdata.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e(TAG, mMessage);
                JSONObject responseJSON = null;
                try {
                    responseJSON = new JSONObject(mMessage);

                    String registrationStatus = responseJSON.getString("status");
                    String status = registrationStatus;
                    if (status.equals("success")){
//                        onRegistrationSuccess();
                        addNewCustomer();
                    }
                    else if (status!="success"){
                        onRegistrationFailed();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    //adding part of the user's KYC
    private void addNewCustomer() throws IOException, InterruptedException{
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


        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.ThemeOverlay_AppCompat_Dialog);


//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Adding user...");
//        progressDialog.show();
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        try {
                            addNewCustomerRequest( firstname,  last_name, id_no,  dob, mobile_no,  email, resultUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                });



    }

    //    adding  a  new customer
    public void addNewCustomerRequest(String first_name, String last_name, String id_no, String dob,String mobile_no, String email, Uri photo_url) throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url ="https://demo.wazinsure.com:4443/api/customers";
        OkHttpClient client = new OkHttpClient();
        JSONObject postData = new JSONObject();
        try {
            postData.put("first_name", first_name);
            postData.put("last_name", last_name);
            postData.put("id_no", id_no);
            postData.put("dob", dob);
            postData.put("mobile_no", mobile_no);
            postData.put("email", email);
            postData.put("photo_url", photo_url);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //creating a new post request
        RequestBody body = RequestBody.create(MEDIA_TYPE, postData.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                call.cancel();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e(TAG, mMessage);

                try {
                    JSONObject responseJSON = new JSONObject(mMessage);

                    String customerAddStatus = responseJSON.getString("status");
                    String status = customerAddStatus;
                    if (status.equals("success")){
                        addDbData();
                        onCustomerAddSuccess();
                    }
                    else if (status!="success"){
                        onCustomerAddFailed();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void addDbData() {
        String firstname = _firstnameText.getText().toString();
        String last_name = _surnameText.getText().toString();
        String id_no = _idnoText.getText().toString();
        String mobile_no = _mobilenoText.getText().toString();
        String email = _emailText.getText().toString();
        String dob = _dobText.getText().toString();


        if (firstname.length() !=0 && id_no.length() !=0 && last_name.length() !=0){
            boolean isInserted = mDatabaseHelper.insertData(id_no,firstname,last_name);
//            if (isInserted == true ) {
//                toastMessage(this,"Data Successfully Inserted!");
//            } else {
//                toastMessage(this,"Something went wrong");
//            }
        }

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(final Context context, String message){
        if (context != null && message != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context,message, Toast.LENGTH_SHORT).show();

                }
            });
        }

    }




    public void onCustomerAddSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.success(getBaseContext(), "Information Saved!", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    public void onCustomerAddFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.warning(getBaseContext(), "Information not Saved", Toast.LENGTH_SHORT, true).show();

            }
        });
    }


}