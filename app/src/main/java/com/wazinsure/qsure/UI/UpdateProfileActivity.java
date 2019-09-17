package com.wazinsure.qsure.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.wazinsure.qsure.R;
import com.wazinsure.qsure.constants.Constants;
import com.wazinsure.qsure.models.CustomerModel;

import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.wazinsure.qsure.UI.LoginActivity.CustomerID;
import static com.wazinsure.qsure.UI.LoginActivity.mypreference;

public class UpdateProfileActivity extends AppCompatActivity {

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
    @BindView(R.id.btn_updateCustomer)
    Button btn_updateText;
    @BindView(R.id.photo_url)
    ImageView photo_urlText;
    SharedPreferences sharedpreferences;


    private static final String TAG = "CustomerUpdateActivity";
    ArrayList<CustomerModel> allCustomers;
    private String CustomerIDRetrieved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(CustomerID)) {
            CustomerIDRetrieved = (sharedpreferences.getString(CustomerID, ""));
        }

        getAllCustomers();

    }

    private void getAllCustomers() {
        final UpdateProfileActivity getService = new UpdateProfileActivity();
        getService.allCustomers(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                allCustomers =  getService.processResults(response);
                UpdateProfileActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                    }
                });
            }
        });
    }



    public static void allCustomers(Callback callback) {

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.CUSTOMERS + CustomerID).newBuilder();

        String url = urlBuilder.build().toString();

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);

    }


    // making get request and adding items to arraylist of objects
    public ArrayList<CustomerModel> processResults(Response response) {
        //create empty Arraylist that will be used to store customer details from the response
        allCustomers = new ArrayList<>();
        try {

            String jsonData = response.body().string();
            //JSONObject
            JSONObject customersJSON = new JSONObject(jsonData);
            String status = customersJSON.getString("status");
            JSONArray arrayList = customersJSON.getJSONArray("data");
            Log.v(TAG, "Response " + customersJSON.toString());

            if (status.equals("success")) {
                for (int i = 0; i < arrayList.length(); i++) {

                    JSONObject resultJSON = arrayList.getJSONObject(i);
                    String customer_id = resultJSON.getString("customer_id");
                    String id_no = resultJSON.getString("id_no");
                    String first_name = resultJSON.getString("first_name");
                    String last_name = resultJSON.getString("last_name");
                    String dob = resultJSON.getString("dob");
                    String kra_pin = resultJSON.getString("kra_pin");
                    String occupation = resultJSON.getString("occupation");
                    String mobile_no = resultJSON.getString("mobile_no");
                    String email = resultJSON.getString("email");
                    String location = resultJSON.getString("location");
                    String postal_address = resultJSON.getString("postal_address");
                    String postal_code = resultJSON.getString("postal_code");
                    String town = resultJSON.getString("town");
                    String country = resultJSON.getString("country");
                    String photo_url = resultJSON.getString("photo_url");
                    String nok_fullname = resultJSON.getString("nok_fullname");
                    String nok_mobileno = resultJSON.getString("nok_mobileno");
                    String nok_relation = resultJSON.getString("nok_relation");
                    String agent_code = resultJSON.getString("agent_code");
                    String agent_usercode = resultJSON.getString("agent_usercode");
                    String sales_channel = resultJSON.getString("sales_channel");


                    CustomerModel customerModel =  new CustomerModel( customer_id,id_no, first_name, last_name, dob, kra_pin, occupation, mobile_no, email,
                            location, postal_address, postal_code, town, country, photo_url, nok_fullname, nok_mobileno, nok_relation, agent_code, agent_usercode, sales_channel);
                    //adding customer objects to allCustomers list
                    allCustomers.add(customerModel);

                }
            }else {
                Toasty.error(getBaseContext(), "Error !", Toast.LENGTH_SHORT, true).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }







}
