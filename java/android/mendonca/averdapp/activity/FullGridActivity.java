package android.mendonca.averdapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.Config;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.fragment.CartFragment;
import android.mendonca.averdapp.model.User;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.mendonca.averdapp.activity.MainActivity.CURRENT_TAG;


/**
 * Created by johnson on 13-05-2018.
 */

public class FullGridActivity extends AppCompatActivity {
    public static int i=1;
    private TextView prd_name,stock;
    private TextView rs;
    private EditText c_textbox,s_textbox;
    //private ImageView prd_img;
    private TextView prd_desc;
    private  TextView prd_price;
    private LinearLayout btn_addcart,btn_buy;
    private NetworkImageView mNetworkImageView;
    private ImageButton cplus,cminus,splus,sminus;
    private ImageLoader imageLoader;
    private String TAG = GridActivity.class.getSimpleName();
    final String User_ID = MyApplication.getInstance().getPrefManager().getUser().getId();
    private static String product_ID;
    private static String product_price;
    private static int BUTTON_CLICK=0;
    private int count1=0,count2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullgrid);
        Intent i=getIntent();
        product_ID=i.getStringExtra("product_id");
        product_price=i.getStringExtra("price");
        prd_name = (TextView) findViewById(R.id.txt_productname);
        rs = (TextView) findViewById(R.id.txt_rs);
        prd_price=(TextView) findViewById(R.id.prod_price);
        prd_desc = (TextView) findViewById(R.id.txt_desc);
        //prd_img = (ImageView) findViewById(R.id.imageviewfull);
        btn_addcart = (LinearLayout) findViewById(R.id.addtocart);
        btn_buy = (LinearLayout) findViewById(R.id.confim);
        stock = (TextView) findViewById(R.id.stock_info);
        c_textbox = (EditText) findViewById(R.id.case_quantity);
        s_textbox = (EditText) findViewById(R.id.single_quantity);
        cplus = (ImageButton) findViewById(R.id.CQuant_more);
        cminus = (ImageButton) findViewById(R.id.CQuant_less);
        splus = (ImageButton) findViewById(R.id.SQuant_more);
        sminus = (ImageButton) findViewById(R.id.SQuant_less);

        //show the back option home
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ProgressDialog pd = new ProgressDialog(FullGridActivity.this,R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setMessage("Authenticating");
        pd.show();
        view_product();
        pd.dismiss();
        imgbutton_func();
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUTTON_CLICK=1;
                Add_product();
                CartFragment fragment = new CartFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUTTON_CLICK=2;
                Add_product();

            }
        });
    }
    private void imgbutton_func() {
        cplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count1 >= 0) {
                    count1++;
                    c_textbox.setText(String.valueOf(count1));
                }
            }
        });

        cminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count1 >= 1) {
                    count1--;
                    c_textbox.setText(String.valueOf(count1));
                }
            }
        });

        splus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count2 >= 0) {
                    count2++;
                    s_textbox.setText(String.valueOf(count2));
                }
            }
        });

        sminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count2 >= 1) {
                    count2--;
                    s_textbox.setText(String.valueOf(count2));
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
        }
        return super.onOptionsItemSelected(item);
    }
    private void view_product(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONObject productObj = obj.getJSONObject("product");
                            prd_name.setText(productObj.getString("prod_name"));
                            rs.setText("Rs.");
                            //prd_price.setText(prod_Obj.getString("prod_price"));
                            prd_price.setText(product_price);
                            stock.setText(productObj.getString("stock"));
                            prd_desc.setText(productObj.getString("prod_desc"));
                            //prd_img = (ImageView) findViewById(R.id.imageviewfull);
                            final String url = productObj.getString("img");
                            mNetworkImageView = (NetworkImageView) findViewById(R.id
                                    .imageviewfull);
                            imageLoader = MyApplication.getInstance().getImageLoader();

                            //Image URL - This can point to any image file supported by Android

                            imageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                                    R.mipmap.ic_launcher, android.R.drawable
                                            .ic_dialog_alert));
                            mNetworkImageView.setImageUrl(url, imageLoader);

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error1").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        })
        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("product_id", product_ID);
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };;
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
    private void Add_product() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CART, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        if (BUTTON_CLICK == 1) {
                            BUTTON_CLICK = 0;
                        } else if (BUTTON_CLICK == 2) {
                            BUTTON_CLICK = 0;
                            Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error1").getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // login error - simply toast the message
                       if (BUTTON_CLICK == 2){
                                BUTTON_CLICK = 0;
                                Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error1").getString("message"), Toast.LENGTH_LONG).show();
                            }

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
                params.put("product_id", product_ID);
                params.put("user_id", User_ID);
                params.put("S_Qty", s_textbox.getText().toString());
                params.put("Case_Qty", c_textbox.getText().toString());

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };
        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
