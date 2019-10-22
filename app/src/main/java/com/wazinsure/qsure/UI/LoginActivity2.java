package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.helpers.DatabaseHelper;
import com.wazinsure.qsure.service.AppSingleton;
import com.wazinsure.qsure.service.SaveSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class LoginActivity2 extends AppCompatActivity {
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
        mDatabaseHelper = new DatabaseHelper(this);
        progressDialog = new ProgressDialog(this, android.R.style.ThemeOverlay_Material_Dialog_Alert);
        progressDialog.setCancelable(false);

        checkWhetherthereareRegisteredUsersinDB();


    }


    public void  checkWhetherthereareRegisteredUsersinDB(){
        Cursor result = mDatabaseHelper.getAllData();


        //retrieve data from SQlite DB

        //check if there are any records
        if(result.getCount() == 0 ){


            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    userSignin();
                }
            });

        }
        else if (result.getCount() !=0 ){

            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {

                    Intent intent = new Intent(getApplicationContext(), LoginActivityVolley.class);
                    startActivity(intent);

                }
            });
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
                .setIcon(R.mipmap.no_wifi_icon)
                .setCancelable(false)

                .show();
    }


    private void userSignin() {


        String username = "AutoLogin";
        String password = "AutoLogin";
        login(username, password);


    }

    private void login(String username, String password) {

        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status = jObj.getString("status");
                    String tokn = jObj.getString("token");
                    TOKEN = tokn;
                    if (status.equals("success")) {
                        {
                            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity2.class);
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
                hideDialog();
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toasty.warning(getApplicationContext(),"Please ensure your USERNAME and PASSWORD are correct "+error.getMessage(), Toast.LENGTH_LONG).show();

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

    private void saveUserID() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "mypref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("k",TOKEN);
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
