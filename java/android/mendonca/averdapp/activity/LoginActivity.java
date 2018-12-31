package android.mendonca.averdapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.model.User;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.mendonca.averdapp.app.HttpsTrustManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by johnson on 01-05-2018.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String TAG="LoginActivty";
    private static final int REQUEST_LOGIN = 0;

    EditText _email;
    EditText paswd;
    Button loginbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        _email = (EditText) findViewById(R.id.input_email);
         paswd = (EditText) findViewById(R.id.input_password);
        loginbtn = (Button) findViewById(R.id.btn_login);

        getSupportActionBar().setTitle(R.string.nav_signin);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==REQUEST_LOGIN){
            if(resultCode == RESULT_OK)
            {
                LoginActivity.this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //disable going to the Mainactivity
        moveTaskToBack(true);
    }

    private void login() {
        Log.d(TAG,"Login");
        if(!validate()){
            onLoginFailed();
            return;
        }
        else
        {
            final ProgressDialog pd = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
            pd.setIndeterminate(true);
            pd.setMessage("Authenticating");
            pd.show();
            final String email = _email.getText().toString();
            final String passwrd = paswd.getText().toString();
            //HttpsTrustManager.allowAllSSL();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);
                        Log.e(TAG, "response: " + obj.length());
                        // check for error flag
                        if (obj.getBoolean("error") == false) {
                            // user successfully logged in

                            JSONObject userObj = obj.getJSONObject("user");
                            User user = new User(userObj.getString("user_id"),
                                    userObj.getString("name"),
                                    userObj.getString("email"),
                                    userObj.getString("type"),
                                    userObj.getString("usrname"),
                                    userObj.getString("phone"),
                                    userObj.getString("phone1"),
                                    userObj.getString("addrs"),
                                    userObj.getString("addrs1"));

                            // storing user in shared preferences
                            MyApplication.getInstance().getPrefManager().storeUser(user);

                            // start main activity
                            onLoginSucess();
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {
                            // login error - simply toast the message
                            Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error1").getString("message"), Toast.LENGTH_LONG).show();
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
                    params.put("email", email);
                    params.put("password", passwrd);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };
            pd.dismiss();
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }
    }

    private void onLoginSucess() {
        loginbtn.setEnabled(true);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(),"Login failed",Toast.LENGTH_SHORT).show();
        loginbtn.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;

        String email_id = _email.getText().toString();
        String password = paswd.getText().toString();

        if(email_id.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_id).matches()){
            _email.setError("Enter a valid email address");
            valid = false;
        } else {
            _email.setError(null);
        }

        if(password.isEmpty() || password.length()<4 || password.length()>10) {
            paswd.setError("Between 4 and 10 alphanumeric characters");
            valid = false;
        }else {
            paswd.setError(null);
        }

        Log.e(TAG,"result:" + valid);
        return  valid;
    }
}