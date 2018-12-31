package android.mendonca.averdapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.fragment.AdminFragment;
import android.mendonca.averdapp.model.User;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by johnson on 01-05-2018.
 */

public class SignupActivity extends AppCompatActivity implements LocationListener {

    String items[]={"state","Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh","Goa","Gujarat","Haryana","Himachal pradesh",
            "Jammu & kashmir","Jharkhand","karnataka","kerala","Madhya Pradesh","Maharashra","Manipur","Meghalaya","Mizoram","Nagaland",
            "odisha","Punjab","Rajastan","Sikkim","Tamil Nadu","Telangana","Tripura","UttarKhand","Uttar Pradesh","West Bengal"};

    private static final String TAG = "SignupActivty";
    private EditText nametext,latitide,longitude;
    private EditText username;
    private EditText addresstext;
    private EditText addresstext2;
    private  EditText emailtext;
    private EditText mobiletexthome;
    private EditText mobiletextoffice;
    private EditText passwd;
    private EditText re_passwd;
    private Spinner spinnertext;
    private EditText citytext;
    private Button signupbtn;
    private ImageButton getLocBtn;
    private LocationManager locationManager;
    private RadioGroup radioButtonGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Higher versions of android will request the permission in this way
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        //get spinner from the xml
        Spinner dropdown =(Spinner) findViewById(R.id.states);
        //create adapter to describe how the items are displayed
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignupActivity.this,android.R.layout.simple_spinner_dropdown_item,items);
        //set the spinner adapter to the previously created one
        dropdown.setAdapter(adapter);
        int selectionPosition = adapter.getPosition("state");
        dropdown.setSelection(selectionPosition);
        Log.e("Position" + selectionPosition,TAG);
        nametext = (EditText) findViewById(R.id.input_name);
        username = (EditText) findViewById(R.id.input_uname);
        addresstext = (EditText) findViewById(R.id.input_address);
        addresstext2 = (EditText) findViewById(R.id.input_address2);
        latitide = (EditText) findViewById(R.id.input_latitide);
        longitude = (EditText) findViewById(R.id.input_longitude);
        emailtext = (EditText) findViewById(R.id.input_email);
        mobiletexthome = (EditText) findViewById(R.id.input_mobile);
        mobiletextoffice = (EditText) findViewById(R.id.input_mobile2);
        passwd = (EditText) findViewById(R.id.input_password);
        re_passwd = (EditText) findViewById(R.id.input_reEnterPassword);
        citytext = (EditText) findViewById(R.id.input_city);
        spinnertext = (Spinner) findViewById(R.id.states);
        getLocBtn = (ImageButton) findViewById(R.id.imgbtn_loc);
        radioButtonGroup=(RadioGroup)findViewById(R.id.radioGroup);

        getSupportActionBar().setTitle(R.string.nav_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signupbtn = (Button) findViewById(R.id.btn_signup);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        getLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
                getLocBtn.setEnabled(false);
            }
        });
    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }

    }

    private void signup() {
        Log.e(TAG,"Signup");

        if(!validate())
        {
            onSignupfailed();
            return;
        }else{
            //Toast.makeText(getBaseContext(),"Signup success",Toast.LENGTH_SHORT).show();
            signupbtn.setEnabled(false);
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("creating Account..");
            progressDialog.show();
            sign_up();
            onSignupSuccess();
            progressDialog.dismiss();
        }
    }

    private void onSignupSuccess() {
        signupbtn.setEnabled(true);
        setResult(RESULT_OK,null);
        finish();
    }


    private void onSignupfailed() {
        Toast.makeText(getBaseContext(),"Signup failed",Toast.LENGTH_SHORT).show();
        signupbtn.setEnabled(true);
    }
    //checking validation of the information enter by the user
    private boolean validate() {
        boolean valid = true;

        String name = nametext.getText().toString();
        String usrname= username.getText().toString();
        String address = addresstext.getText().toString();
        String address2 = addresstext2.getText().toString();
        String lat = latitide.getText().toString();
        String longi = longitude.getText().toString();
        String email =  emailtext.getText().toString();
        String mobile1 = mobiletexthome.getText().toString();
        String mobile2 = mobiletextoffice.getText().toString();
        String pasword = passwd.getText().toString();
        String repassword = re_passwd.getText().toString();
        String city = citytext.getText().toString();
        int selectitemspinner = spinnertext.getSelectedItemPosition();
        String actualposofspinner = (String) spinnertext.getItemAtPosition(selectitemspinner);

        if(actualposofspinner.equals("state")){
            View selectedView = spinnertext.getSelectedView();
            if(selectedView !=null && selectedView instanceof TextView){
                spinnertext.requestFocus();
                TextView selectedtext = (TextView) selectedView;
                selectedtext.setText("can't be empty");
            }
        }

        if(name.isEmpty() || name.length() <3){
            nametext.setError("at least 3 characters");
            valid =false;
        }
        else {
            nametext.setError(null);
        }
        if(usrname.isEmpty() || usrname.length() <3){
            username.setError("at least 3 characters");
            valid =false;
        }
        else {
            username.setError(null);
        }

        if(address.isEmpty()){
            addresstext.setError("Enter valid address");
            valid =false;
        }else{
            addresstext.setError(null);
        }

        if(address2.isEmpty()){
            addresstext2.setError("Enter valid address");
            valid =false;
        }else{
            addresstext2.setError(null);
        }

        if(lat.isEmpty()){
            latitide.setError("can't be empty");
            valid =false;
        }else{
            latitide.setError(null);
        }

        if(longi.isEmpty()){
            longitude.setError("can't be empty");
            valid =false;
        }else{
            longitude.setError(null);
        }

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtext.setError("Enter a valid email address");
            valid = false;
        } else {
            emailtext.setError(null);
        }

        if(mobile1.isEmpty() || mobile1.length()!=10){
            mobiletexthome.setError("Enter valid phone Number");
            valid =false;
        } else {
            mobiletexthome.setError(null);
        }

        if(mobile2.isEmpty() || mobile2.length()!=10){
            mobiletextoffice.setError("Enter valid phone Number");
            valid =false;
        } else {
            mobiletextoffice.setError(null);
        }

        if(pasword.isEmpty() ||pasword.length()<4 || pasword.length()>10) {
            passwd.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        }else {
            passwd.setError(null);
        }

        if(repassword.isEmpty() ||repassword.length()<4 || repassword.length()>10 || !(repassword.equals(pasword))){
           re_passwd.setError("Password do not match");
           valid = false;
        }else {
            re_passwd.setError(null);
        }

        if(city.isEmpty()){
            citytext.setError("Enter valid city");
            valid =false;
        }else{
            citytext.setError(null);
        }

        return valid;
    }
    private void sign_up(){
        final String name = nametext.getText().toString();
        final String usrname = username.getText().toString();
        final String address = addresstext.getText().toString();
        final String address2 = addresstext2.getText().toString();
        final String lat = latitide.getText().toString();
        final String longi = longitude.getText().toString();
        final String email =  emailtext.getText().toString();
        final String mobile1 = mobiletexthome.getText().toString();
        final String mobile2 = mobiletextoffice.getText().toString();
        final String pasword = passwd.getText().toString();
        final String city = citytext.getText().toString();
        final int selectitemspinner = spinnertext.getSelectedItemPosition();
        final String actualposofspinner = (String) spinnertext.getItemAtPosition(selectitemspinner);
        int radioButtonID= radioButtonGroup.getCheckedRadioButtonId();
        View radioButton= radioButtonGroup.findViewById(radioButtonID);
         int idx=radioButtonGroup.indexOfChild(radioButton);
         final String cust_type=String.valueOf((idx+1));

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.SIGN_UP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    Log.e(TAG, "response: " + obj.length());
                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in
                        // start main activity
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("message").getString("message"), Toast.LENGTH_LONG).show();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("message").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("fname",name);
                params.put("usrname",usrname );
                params.put("email", email);
                params.put("ctype", cust_type);
                params.put("addrs1", address);
                params.put("addrs2", address2);
                params.put("city", city);
                params.put("state", actualposofspinner);
                params.put("phone1", mobile1);
                params.put("phone2", mobile2);
                params.put("latitude", lat);
                params.put("longitude", longi);
                params.put("password", pasword);
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        latitide.setText(""+location.getLatitude());
        longitude.setText(""+location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SignupActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();

    }
}
