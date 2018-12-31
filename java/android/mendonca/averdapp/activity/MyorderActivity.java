package android.mendonca.averdapp.activity;

import android.content.Intent;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.fragment.CartFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by johnson on 04-05-2018.
 */

public class MyorderActivity extends AppCompatActivity {
    private ArrayList<String> Bill_No = new ArrayList<>();
    private ArrayList<String> P_Name = new ArrayList<>();
    private ArrayList<String> S_Qty = new ArrayList<>();
    private ArrayList<String> C_Qty = new ArrayList<>();
    private ArrayList<String> Addrss = new ArrayList<>();
    private ArrayList<String> Status = new ArrayList<>();
    private ArrayList<String> Grand_Tot = new ArrayList<>();
    private ArrayList<String> O_Date = new ArrayList<>();
    private TextView gt,ordernotfound;
    private String TAG = "Myorder";
    private OrderAdapter orderAdapter;
    private GridView gridView;
    private static int ORDER_AVL=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        getSupportActionBar().setTitle(R.string.nav_myorder);
        gt = (TextView) findViewById(R.id.order_prize);
        ordernotfound = (TextView) findViewById(R.id.no_order);
        RelativeLayout mainlayout = (RelativeLayout) this.findViewById(R.id.mainlayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gridView = (GridView) findViewById(R.id.order_gridview);
        try {
            ORDER_AVL=0;
            fetch_order();
            Log.e("Ord",Integer.toString(ORDER_AVL));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(ORDER_AVL==1)
        {
            mainlayout.setVisibility(RelativeLayout.INVISIBLE);
            ordernotfound.setText("No Orders");
            ORDER_AVL=0;
        }

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
    private void fetch_order(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.ORDER_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("error") == false){
                        JSONArray productArray = obj.getJSONArray("order_product");
                        for (int i = 0; i < productArray.length(); i++) {
                            JSONObject productObj = (JSONObject) productArray.get(i);
                            Bill_No.add(productObj.getString("bill_no"));
                            P_Name.add(productObj.getString("p_name"));
                            S_Qty.add(productObj.getString("s_qty"));
                            C_Qty.add(productObj.getString("c_qty"));
                            Addrss.add(productObj.getString("addrss"));
                            Status.add(productObj.getString("status"));
                            Grand_Tot.add(productObj.getString("grand_tot"));
                            O_Date.add(productObj.getString("o_date"));
                        }
                    }
                    else{
                        ORDER_AVL=1;
                        Log.e("Hello O",Integer.toString(ORDER_AVL));
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error1").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                orderAdapter = new OrderAdapter(getApplicationContext(),Bill_No,P_Name,S_Qty,C_Qty,Addrss,Status,Grand_Tot,O_Date);
                gridView.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cust_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };
        //Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding our request to the queue
        requestQueue.add(strReq);
    }
}
