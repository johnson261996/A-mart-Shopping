package android.mendonca.averdapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.model.User;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by johnson on 22-06-2018.
 */
public class Profile_Edit extends AppCompatActivity {
    private EditText old_pwd,new_pwd,con_pwd;
    private CircleImageView circleImageView;
    private static int RESULT_LOAD_IMG = 1;
    private static final String TAG="Profile Edit Activty";
    private String password,c_password,o_pwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.nav_edit_profile);
        setContentView(R.layout.activity_profile_edit);
        old_pwd = (EditText) findViewById(R.id.new_input_oldpassword);
        new_pwd = (EditText) findViewById(R.id.new_input_password);
        con_pwd = (EditText) findViewById(R.id.new_input_reEnterPassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        circleImageView = (CircleImageView) findViewById(R.id.new_header_cover_image);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_ok:

            if(!validate())
            {
                editfailed();
            }else{
                password = new_pwd.getText().toString();
                o_pwd = old_pwd.getText().toString();
                final ProgressDialog progressDialog = new ProgressDialog(Profile_Edit.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Updating Details..");
                progressDialog.show();
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.PROFILE, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);

                        try {
                            JSONObject obj = new JSONObject(response);

                            // check for error flag
                            if (obj.getBoolean("error") == false) {
                                // user successfully logged in
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
                        params.put("cust_id",MyApplication.getInstance().getPrefManager().getUser().getId());
                        params.put("password", o_pwd);
                        params.put("newpassword", password);
                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                };

                                progressDialog.dismiss();
                                editSuccess();

                MyApplication.getInstance().addToRequestQueue(strReq);
            }
        }
        return super.onOptionsItemSelected(item);

    }

    private void editSuccess() {

        setResult(RESULT_OK,null);
        finish();
    }
    private void editfailed() {
        Toast.makeText(getBaseContext(),"failed",Toast.LENGTH_SHORT).show();
    }

    private boolean validate() {
        boolean valid = true;
        password = new_pwd.getText().toString();
        c_password = con_pwd.getText().toString();
        o_pwd = old_pwd.getText().toString();


        if(password.isEmpty() || password.length()<4 || password.length()>10) {

            new_pwd.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        }else {
            new_pwd.setError(null);
        }

        if(c_password.isEmpty() || c_password.length()<4 || c_password.length()>10 || !(c_password.equals(password))){
            con_pwd.setError("Password do not match");
            valid = false;
        }else {
            con_pwd.setError(null);
        }
        return  valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_edit_ok,menu);
        return true;
    }


}
