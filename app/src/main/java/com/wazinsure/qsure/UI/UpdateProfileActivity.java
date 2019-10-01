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
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.wazinsure.qsure.R;
import com.wazinsure.qsure.constants.Constants;
import com.wazinsure.qsure.models.CustomerModel;

import butterknife.BindView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wazinsure.qsure.service.AppSingleton;

import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;



public class UpdateProfileActivity extends AppCompatActivity {

    private static final String URL_FOR_CUSTOMER = "https://demo.wazinsure.com:4443/api/customers";
    @BindView(R.id.id_no)
    EditText id_noText;
    @BindView(R.id.first_name) EditText firstNameText;
    @BindView(R.id.last_name) EditText lastNameText;
    @BindView(R.id.dob) EditText dobText;
    @BindView(R.id.kra_pin)
    EditText kra_PinText;
    @BindView(R.id.occupation) EditText occupationText;
    @BindView(R.id.mobile_no) EditText mobile_noText;
    @BindView(R.id.email) EditText  emailText;
    @BindView(R.id.location) EditText locationText;
    @BindView(R.id.postal_address) EditText postal_addressText;
    @BindView(R.id.postal_code) EditText postal_codeText;
    @BindView(R.id.town) EditText townText;
    @BindView(R.id.country) EditText countryText;
    @BindView(R.id.nok_fullname) EditText nok_fullnameText;
    @BindView(R.id.nok_mobileno) EditText nok_mobilenoText;
    @BindView(R.id.nok_relation) EditText nok_relationText;
    @BindView(R.id.agent_code) EditText agent_codeText;
    @BindView(R.id.agent_usercode) EditText agent_usercodeText;
    @BindView(R.id.sales_channel) EditText sales_channelText;
    @BindView(R.id.course) EditText courseText;
    @BindView(R.id.institution) EditText institutionText;
    @BindView(R.id.gender) EditText gendertext;
    @BindView(R.id.btn_updateCustomer)
    Button btn_updateText;
    @BindView(R.id.photo_url)
    ImageView profileUrlText;
    CheckConnection checkConnection;
    SharedPreferences sharedpreferences;
    private DatePickerDialog dobdatePickerDialog;
    private static final String PREFERENCE_FILE_KEY = "n";
    ProgressDialog progressDialog;
    private static final String TAG = "CustomerUpdateActivity";
    ArrayList<CustomerModel> allCustomers;
    private String CustomerIDRetrieved;
    private String CustomerID;
    private  String CUSTOMER_URL = "https://demo.wazinsure.com:4443/api/customers/";
    private JSONArray result;
    private CustomerModel customerModel;
    private Uri resultUri;
    public static final int PICK_IMAGE = 1;
    private String TOKEN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);




        sharedpreferences = getSharedPreferences("mypref",
                Context.MODE_PRIVATE);
               CustomerIDRetrieved = (sharedpreferences.getString("n", "default"));
               CustomerID = CustomerIDRetrieved;
               String token = (sharedpreferences.getString("k", "TOKEN"));
               TOKEN = token;

               Log.d("customerID here ",CustomerID);



        if(!checkConnection.isConnectedToInternet(UpdateProfileActivity.this)){
            shownetworkDialog();
        }



        btn_updateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomer();
            }
        });

        // picking profile image from gallery
        profileUrlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseProfileImage();
            }
        });


        //opening date picker dialog
        dobText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showMyDialog();
                dobdatePickerDialog.show();
            }
        });
        setDateTimeField();


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Records ...");
        showDialog();
        getData();
    }




    //choose profile image
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
            profileUrlText.setImageURI(resultUri);
        }
    }

    private void updateCustomer() {
        String firstname = firstNameText.getText().toString();
        String last_name = lastNameText.getText().toString();
        String gender = gendertext.getText().toString();
        String id_no = id_noText.getText().toString();
        String dob = dobText.getText().toString();
        String kra_pin = kra_PinText.getText().toString();
        String occupation = occupationText.getText().toString();
        String mobile_no = mobile_noText.getText().toString();
        String email = emailText.getText().toString();
        String course = courseText.getText().toString();
        String institution = institutionText.getText().toString();
        String location = locationText.getText().toString();
        String postal_address = postal_addressText.getText().toString();
        String postal_code = postal_codeText.getText().toString();
        String town = townText.getText().toString();
        String country = countryText.getText().toString();

        String nok_fullname = nok_fullnameText.getText().toString();
        String nok_mobileno = nok_mobilenoText.getText().toString();
        String nok_relation = nok_relationText.getText().toString();
        String agent_code = agent_codeText.getText().toString();
        String agent_usercode = agent_usercodeText.getText().toString();
        String sales_channel = sales_channelText.getText().toString();


            addNewCustomerUpdateRequest( firstname,  last_name, gender,  dob, kra_pin, occupation, mobile_no,  email, course, institution, location, postal_address, postal_code, town, country, nok_fullname, nok_mobileno, nok_relation, agent_code, agent_usercode, sales_channel, resultUri);

    }

    private void addNewCustomerUpdateRequest(String firstname, String last_name, String gender, String dob, String kra_pin, String occupation, String mobile_no, String email, String course, String institution, String location, String postal_address, String postal_code, String town, String country, String nok_fullname, String nok_mobileno, String nok_relation, String agent_code, String agent_usercode, String sales_channel, Uri photo_url) {


        // Tag used to cancel the request

        String cancel_req_tag = "customer";
        progressDialog.setMessage("Updating Customer Record...");
        showDialog();
        StringRequest strReqUpdate = new StringRequest(Request.Method.PUT,CUSTOMER_URL + CustomerID,
                new Response.Listener<String>() {

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

                            onUpdateSuccess();
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
                // Put params to customer url
                Map<String, String> params = new HashMap<>();
                params.put("first_name",firstname);
                params.put("last_name",last_name );
                params.put("gender",gender );
//                params.put("id_no",id_no);
                params.put("dob",dob );
                params.put("kra_pin",kra_pin );
                params.put("occupation",occupation );
                params.put("mobile_no",mobile_no );
                params.put("email",email );
                params.put("course",course );
                params.put("institution",institution );
                params.put("location",location );
                params.put("postal_address",postal_address );
                params.put("postal_code",postal_code );
                params.put("country ",country  );
                params.put("nok_fullname",nok_fullname );
                params.put("nok_mobileno",nok_mobileno );
                params.put("nok_relation",nok_relation );
                params.put("agent_code",agent_code  );
                params.put("agent_usercode",agent_usercode  );
                params.put("sales_channel",sales_channel  );
                params.put("photo_url",String.valueOf(photo_url));

                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReqUpdate,cancel_req_tag);

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

    //set date dialog
    private void setDateTimeField() {
        dobText.setInputType(InputType.TYPE_NULL);

        Calendar newCalendar = Calendar.getInstance();
        dobdatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                dobText.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }


    //get customer information using ID
    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET,CUSTOMER_URL + CustomerID,
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
                            getCustomers(result);

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


    private void getCustomers(JSONArray j) throws JSONException {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);
                firstNameText.setText(json.getString("first_name"));
                lastNameText.setText(json.getString("last_name"));
                dobText.setText(json.getString("dob"));
                kra_PinText.setText(json.getString("kra_pin"));
                occupationText.setText(json.getString("occupation"));
                mobile_noText.setText(json.getString("mobile_no"));
                emailText.setText(json.getString("email"));
                locationText.setText(json.getString("location"));
                postal_addressText.setText(json.getString("postal_address"));
                postal_codeText.setText(json.getString("postal_code"));
                townText.setText(json.getString("town"));
                countryText.setText(json.getString("country"));
                nok_fullnameText.setText(json.getString("nok_fullname"));
                nok_mobilenoText.setText(json.getString("nok_mobileno"));
                nok_relationText.setText(json.getString("nok_relation"));
                agent_codeText.setText(json.getString("agent_code"));
                agent_usercodeText.setText(json.getString("agent_usercode"));
                sales_channelText.setText(json.getString("sales_channel"));
                gendertext.setText(json.getString("gender"));
                courseText.setText(json.getString("course"));
                institutionText.setText(json.getString("institution"));
                String imageUrl = json.getString("photo_url");

                if ( imageUrl.isEmpty() || imageUrl == null){

                    Glide.with(getApplicationContext()).load(R.mipmap.user_icon).into(profileUrlText);

                }
                else {

                    Glide.with(getApplicationContext()).load(imageUrl).into(profileUrlText);
                }


            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }

    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    //success and failure messages nb

    public void onUpdateSuccess() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.success(getBaseContext(), "Update Successful!", Toast.LENGTH_LONG, true).show();

            }
        });
    }

    public void onUpdateFailed() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                Toasty.warning(getBaseContext(), "Update  Failed", Toast.LENGTH_SHORT, true).show();

            }
        });
        btn_updateText.setEnabled(true);
    }

}
