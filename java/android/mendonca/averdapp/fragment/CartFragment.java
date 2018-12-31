package android.mendonca.averdapp.fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.activity.CartAdapter;
import android.mendonca.averdapp.activity.MyorderActivity;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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

public class CartFragment extends Fragment {
    private String TAG = CartFragment.class.getSimpleName();
    private View view;
    private GridView gridView;
    private CartAdapter cart;
    private Button confirm,plus;
    private TextView textView;
   // private String[] P_name = {"product name","Product name","product name","Product name"} ;
   // private String[] P_prize = {"1000","1000","1000","1000"} ;
    private ArrayList<String> P_name = new ArrayList<>();
    private ArrayList<String> P_prize = new ArrayList<>();
    private ArrayList<String> P_code = new ArrayList<>();
    private ArrayList<String> P_S_Qty = new ArrayList<>();
    private ArrayList<String> P_Case_Qty = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> P_S_price = new ArrayList<>();
    private ArrayList<String> P_Case_price = new ArrayList<>();
    private ArrayList<String> SGST = new ArrayList<>();
    private ArrayList<String> CGST = new ArrayList<>();
    private String Total_pay="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_cart,container,false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(R.string.nav_cart);
        //get the gridview
        gridView = (GridView) view.findViewById(R.id.gridviewcatalog);
        textView=(TextView) view.findViewById(R.id.cart_totalprice2);
        final ProgressDialog pd = new ProgressDialog(getContext(),R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setMessage("Loading...");
        pd.show();
        fecth_cart();

        pd.dismiss();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        confirm = (Button) view.findViewById(R.id.confim_btn);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(getContext(),R.style.AppTheme_Dark_Dialog);
                pd.setIndeterminate(true);
                pd.setMessage("Loading...");
                pd.show();
                send_cart();

                pd.dismiss();
            }
        });

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //clear the menu items in cart fragment
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }
    private void fecth_cart(){
        //   final MyApplication ct = (MyApplication) getApplicationContext();
        // ItemDetails products = null;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.CART_PRODUCT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray productArray = obj.getJSONArray("cart_product");
                    for (int i = 0; i < productArray.length(); i++) {

                        JSONObject productObj = (JSONObject) productArray.get(i);
                        P_code.add(productObj.getString("p_code"));
                        P_name.add(productObj.getString("p_name"));
                        P_prize.add(productObj.getString("p_price"));
                        images.add(productObj.getString("image"));
                        P_S_Qty.add(productObj.getString("s_qty"));
                        P_Case_Qty.add(productObj.getString("case_qty"));
                        P_S_price.add(productObj.getString("s_price"));
                        P_Case_price.add(productObj.getString("case_price"));
                        SGST.add(productObj.getString("sgst"));
                        CGST.add(productObj.getString("cgst"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(CartFragment.this.getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                cart = new CartAdapter(CartFragment.this.getContext(),P_code,P_name,P_prize,images,P_S_Qty,P_Case_Qty,P_S_price,P_Case_price,SGST,CGST);
                gridView.setAdapter(cart);
                cart.notifyDataSetChanged();
                Total_pay=cart.Calculate();
                textView.setText(Total_pay);
                Log.e("Total",Total_pay);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(CartFragment.this.getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(CartFragment.this.getContext());
        //Adding our request to the queue
        requestQueue.add(strReq);
    }
    private void send_cart(){
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == true) {
                            Toast.makeText(getActivity(), "" + obj.getJSONObject("error1").getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(obj.getBoolean("error") == false){
                        Intent intent = new Intent(CartFragment.this.getActivity(), MyorderActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(CartFragment.this.getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(CartFragment.this.getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("cust_id", MyApplication.getInstance().getPrefManager().getUser().getId());
                params.put("cart_data", cart.buyProduct());
               // Log.e(TAG, "params: " + cart.buyProduct());
                return params;
            }
        };
        //Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(CartFragment.this.getContext());
        //Adding our request to the queue
        requestQueue.add(strReq);
    }
}
