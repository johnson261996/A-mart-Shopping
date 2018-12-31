package android.mendonca.averdapp.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.activity.ExpandListAdapter;
import android.mendonca.averdapp.activity.GridActivity;
import android.mendonca.averdapp.activity.LoginActivity;
import android.mendonca.averdapp.activity.MainActivity;
import android.mendonca.averdapp.app.EndPoints;
import android.mendonca.averdapp.app.MyApplication;
import android.mendonca.averdapp.model.Child;
import android.mendonca.averdapp.model.Group;
import android.mendonca.averdapp.model.User;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static android.mendonca.averdapp.app.MyApplication.TAG;

/**
 * Created by johnson on 04-05-2018.
 */

public class HomeFragment extends Fragment {
    ExpandListAdapter listAdapter;
    ExpandableListView expListView;

    //ProgressDialog Pd;
    ArrayList<Group> listDataHeader;
   // HashMap<String, ArrayList<String>> listdataChild;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        //get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvexp);
        //preparing list data function
        final ProgressDialog pd = new ProgressDialog(getActivity(),R.style.AppTheme_Dark_Dialog);
        pd.setIndeterminate(true);
        pd.setMessage("Loading...");
        pd.show();
        prepareListData();
        pd.dismiss();

        //listAdapter = new ExpandListAdapter(getActivity(), listDataHeader);

        //setting list adapter
        //expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Child child = (Child) listAdapter.getChild(groupPosition,childPosition);

                Toast.makeText(getContext(),"selected : " + child.getName(), Toast.LENGTH_SHORT).show();
                for(int i=0;i<3;i++) {
                    if (groupPosition == i) {
                        if (childPosition == 0) {
                            Intent intent = new Intent(HomeFragment.this.getActivity(), GridActivity.class);
                            intent.putExtra("name",child.getName());
                            intent.putExtra("subcat","0");
                            startActivity(intent);
                        } else if (childPosition == 1) {
                            Intent intent = new Intent(HomeFragment.this.getActivity(), GridActivity.class);
                            intent.putExtra("name",child.getName());
                            intent.putExtra("subcat","1");
                            startActivity(intent);
                        } else if (childPosition == 2) {
                            Intent intent = new Intent(HomeFragment.this.getActivity(), GridActivity.class);
                            intent.putExtra("name",child.getName());
                            intent.putExtra("subcat","2");
                            startActivity(intent);
                        }
                    }
                }
                return true;
            }
        });

        return view;
    }

    //preparing the list data
    private void prepareListData() {

       // listDataHeader = new ArrayList<String>();
        //listdataChild = new HashMap<String, ArrayList<String>>();
        final String endPoint = EndPoints.HOME;
        Log.e(TAG, "endPoint: " + endPoint);
            JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET, endPoint,
                    null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ArrayList<Group> list = new ArrayList<Group>();
                ArrayList<Child> ch_list;
                Log.e(TAG, "response: " + response);

                try {
                    //if(response.keys()==null) {
                        Iterator<String> key = response.keys();
                        while (key.hasNext()) {
                            String k = key.next();
                            Group gru = new Group();
                            gru.setName(k);
                            ch_list = new ArrayList<Child>();
                            JSONArray ja = response.getJSONArray(k);
                            for (int i = 0; i < ja.length(); i++) {
                                JSONObject jo = ja.getJSONObject(i);
                                Child ch = new Child();
                                ch.setName(jo.getString("sub_cat"));
                                ch_list.add(ch);
                            }
                            gru.setItems(ch_list);
                            list.add(gru);
                        }
                        listAdapter = new ExpandListAdapter(HomeFragment.this.getActivity(), list);
                        expListView.setAdapter(listAdapter);

                    //else{
                      //  Toast.makeText(getContext().getApplicationContext(), "No records found", Toast.LENGTH_LONG).show();
                    //}
                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), "json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity().getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
