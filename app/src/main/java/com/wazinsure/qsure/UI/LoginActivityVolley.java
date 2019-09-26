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
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.constants.Constants;
import com.wazinsure.qsure.helpers.DatabaseHelper;
import com.wazinsure.qsure.service.AppSingleton;
import com.wazinsure.qsure.service.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivityVolley extends AppCompatActivity {

    private static final String PREFERENCE_FILE_KEY = "n";
    @BindView(R.id.usernamelogin)
    EditText _idNoText;
    @BindView(R.id.passwordlogin) EditText _pinText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.loginForm)
    ScrollView login_form;
    @BindView(R.id.connectionStatus) TextView connectionText;
    CheckConnection checkConnection;
    SharedPreferences sharedpreferences;

    private String TOKEN;
    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "https://demo.wazinsure.com:4443/auth/login";

    public static String CustomerID;
    ProgressDialog progressDialog;
    DatabaseHelper mDatabaseHelper;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mDatabaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);



        // Check if UserResponse is Already Logged In
        if(SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            Toasty.info(getBaseContext(), " Welcome back !", Toast.LENGTH_SHORT, true).show();
            startActivity(intent);
        } else {
            login_form.setVisibility(View.VISIBLE);
        }


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check internet connectivity
                if (!checkConnection.isConnectedToInternet(getApplicationContext())) {
                    shownetworkDialog();
                }
                else {
                    try{
                        loginInit();

                    } catch(IOException e){
                        e.printStackTrace();
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegistrationActivityVolley.class);
                startActivity(intent);
                finish();
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
                .setIcon(R.mipmap.no_wifi_icon)
                .setCancelable(false)

                .show();
    }


    public void loginInit() throws IOException, InterruptedException {
        Log.d(TAG, "Login trouble shoot");


        if (!validate()) {
            onLoginFailed();
            return;
        }

        userSignin();


    }

    private void userSignin() throws IOException, InterruptedException {
        String id_no = _idNoText.getText().toString();
        String pin = _pinText.getText().toString();
        login(id_no,pin);

    }

    //validate details entered
    public boolean validate() {
        boolean valid = true;

        String pin = _pinText.getText().toString();
        String id_no = _idNoText.getText().toString();

        if (pin.isEmpty() || pin.length() < 4 || pin.length() > 4) {
            _pinText.setError("between 0 and 4 numeric characters");
            _pinText.requestFocus();
            valid = false;
        }
        if (id_no.isEmpty() || id_no.equals("")){
            _idNoText.setError("Id number can not be empty");
            _idNoText.requestFocus();
            valid = false;
        }

        else {
            _pinText.setError(null);
            _idNoText.setError(null);
        }

        return valid;
    }


    //success and failure messages
    public void onLoginSuccess() {

        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {

                Toasty.success(getBaseContext(), " Login Success!", Toast.LENGTH_SHORT, true).show();

            }
        });

        Intent intent = new Intent( this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {

                Toasty.warning(getBaseContext(), "Login Failed!", Toast.LENGTH_SHORT, true).show();
            }
        });
        _loginButton.setEnabled(true);
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
                            onLoginSuccess();
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

    //saving user ID
    private void saveUserID() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("k",TOKEN);
        editor.putString("n", _idNoText.getText().toString() );
        editor.commit();
    }
    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
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
