package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.wazinsure.qsure.R;


import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;

import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.content.Context;

import com.wazinsure.qsure.constants.Constants;
import com.wazinsure.qsure.service.SaveSharedPreference;

import org.json.JSONException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static com.wazinsure.qsure.UI.LoginActivityVolley.CustomerID;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";





    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());


    @BindView(R.id.usernamelogin) EditText _idNoText;
    @BindView(R.id.passwordlogin) EditText _pinText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;
    @BindView(R.id.loginForm) ScrollView login_form;
    @BindView(R.id.connectionStatus) TextView connectionText;
    CheckConnection checkConnection;
    SharedPreferences sharedpreferences;

    private String TOKEN;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        connectionText.setText("");



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



        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.ThemeOverlay_AppCompat_Dialog);


        progressDialog.setIcon(R.mipmap.loading_icon);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loggin you in...");
        progressDialog.show();


        // TODO: Implement authentication logic here.


        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        try {
                            userSignin();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                },3000);

    }



    private void userSignin() throws IOException, InterruptedException {
        String id_no = _idNoText.getText().toString();
        String pin = _pinText.getText().toString();
        login(id_no,pin);

    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

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

    //    User login method
    public void login(String username, String password) throws IOException {
        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        String url = Constants.LOGIN + "/login";
        OkHttpClient client = new OkHttpClient();
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("username", username);
            postdata.put("password", password);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        //creating an new post request
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
//                .header("Authorization","Bearer " + TOKEN)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                call.cancel();;
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage = response.body().string();

                Log.e(TAG, mMessage);

                JSONObject responseJSON = null;

                try {

                    responseJSON = new JSONObject(mMessage);
                    String loginStatus = responseJSON.getString("status");
                    String  token = responseJSON.getString("token");
                    TOKEN = token;

                    String status =loginStatus;

                    if (status.equals("success")){
                        SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |FLAG_ACTIVITY_CLEAR_TASK);
//                        saveUserID();
                        onLoginSuccess();
                        startActivity(intent);

                    }
                    else if (status!="success"){
                        onLoginFailed();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//        private void saveUserID() {
//        String n = _idNoText.getText().toString();
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.putString(CustomerID, n);
//        editor.commit();
//    }


    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}