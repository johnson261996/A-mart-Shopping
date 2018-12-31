package android.mendonca.averdapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.fragment.CartFragment;
import android.mendonca.averdapp.fragment.ProfileFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
 * Created by johnson on 11-05-2018.
 */

public class GridActivity extends AppCompatActivity {
    private String TAG = GridActivity.class.getSimpleName();
    GridView simpleGrid;
    private static String name;
    private static String subcat;
    //private MenuItem cartitem;
    ArrayList<String> p_id= new ArrayList<>();
    ArrayList<String> p_name = new ArrayList<>();
    //ArrayList<String> product_rs = new ArrayList<>();
    ArrayList<String> product_price = new ArrayList<>();
    ArrayList<String> images = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);
        simpleGrid = (GridView) findViewById(R.id.gv);
       // final Controller ct = (Controller) getApplicationContext();
        //ItemDetails products = null;
        //show the back option home
        Intent i= getIntent();
        name=i.getStringExtra("name");
        subcat=i.getStringExtra("subcat");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ProgressDialog pd = new ProgressDialog(GridActivity.this,R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setMessage("Loading...");
        pd.show();
        fecth_product();
        pd.dismiss();

        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(GridActivity.this, "You Clicked at " +p_name.get(position), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(GridActivity.this, FullGridActivity.class);
                i.putExtra("product_id",p_id.get(position));
                i.putExtra("price",product_price.get(position));
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.sort,menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.sort_relevance:
                Toast.makeText(getApplicationContext(),"You clicked Relevance",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_pop:
                Toast.makeText(getApplicationContext(),"You clicked popularity",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_lth:
                Toast.makeText(getApplicationContext(),"You clicked low to high",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.sort_htl:
                Toast.makeText(getApplicationContext(),"You clicked High to Low",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void fecth_product(){
     //   final MyApplication ct = (MyApplication) getApplicationContext();
        // ItemDetails products = null;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.PRODUCTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray productArray = obj.getJSONArray("products");
                    for (int i = 0; i < productArray.length(); i++) {

                        JSONObject productObj = (JSONObject) productArray.get(i);
                        p_id.add(productObj.getString("product_id"));
                        p_name.add(productObj.getString("product_name"));
                       // product_rs.add("Rs.");
                        product_price.add(productObj.getString("price"));
                        images.add(productObj.getString("image"));
                        Log.e(TAG, "This is inner loop: " + obj);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                GridAdapter gridViewAdapter = new GridAdapter(getApplicationContext(),p_id,p_name,product_price,images);
                //Adding adapter to gridview
                simpleGrid.setAdapter(gridViewAdapter);


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
                params.put("name",name );
                params.put("subcat", subcat);
                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(strReq);
    }
}
