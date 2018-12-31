package android.mendonca.averdapp.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.CustomVolleyRequest;
import android.mendonca.averdapp.fragment.CartFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by johnson on 15-05-2018.
 */

public class CartAdapter extends BaseAdapter {
    private Context mContext;

    private ImageLoader imageLoader;
    private ArrayList<String> P_Code = new ArrayList<>();
    private ArrayList<String> P_name = new ArrayList<>();
    private ArrayList<String> P_prize = new ArrayList<>();
    private ArrayList<String> Images = new ArrayList<>();

    private ArrayList<String> P_S_Qty = new ArrayList<>();
    private ArrayList<String> P_Case_Qty = new ArrayList<>();
    private ArrayList<String> P_S_price = new ArrayList<>();
    private ArrayList<String> P_Case_price = new ArrayList<>();
    private ArrayList<String> SGST = new ArrayList<>();
    private ArrayList<String> CGST = new ArrayList<>();
    public CartAdapter(Context mContext,ArrayList p_Code,ArrayList p_name, ArrayList p_prize, ArrayList images,ArrayList p_S_Qty,ArrayList p_Case_Qty,ArrayList p_S_price,ArrayList p_C_price,ArrayList sGst,ArrayList cGst) {
        this.mContext = mContext;
        P_Code=p_Code;
        P_name = p_name;
        Images = images;
        P_prize = p_prize;
        P_S_Qty=p_S_Qty;
        P_Case_Qty=p_Case_Qty;
        P_S_price=p_S_price;
        P_Case_price=p_C_price;
        SGST=sGst;
        CGST=cGst;
    }
    public CartAdapter(){

    }

    @Override
    public int getCount() {
        return P_name.size();
    }

    @Override
    public Object getItem(int position) {
        return P_name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            if (imageLoader == null)
                imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
            NetworkImageView networkImageView = new NetworkImageView(mContext);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.cart_item, null);
            TextView textView = (TextView) grid.findViewById(R.id.cart_prodname);
            TextView textView2 = (TextView) grid.findViewById(R.id.cart_prize);
             imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
            imageLoader.get(Images.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            networkImageView.setImageUrl(Images.get(position), imageLoader);

            final ImageButton imgbut1 = (ImageButton)grid.findViewById(R.id.addnew);
            ImageButton imgbut2= (ImageButton)grid.findViewById(R.id.lessnew);
            ImageButton imgbut3 = (ImageButton)grid.findViewById(R.id.cart_cancel);
            final EditText editText = (EditText)grid.findViewById(R.id.cart_quantity);
            ImageButton imgbut4 = (ImageButton)grid.findViewById(R.id.S_addnew);
            ImageButton imgbut5 = (ImageButton)grid.findViewById(R.id.S_lessnew);
            final EditText editText2 = (EditText)grid.findViewById(R.id.S_cart_quantity);
            textView.setText(P_name.get(position));
            textView2.setText(P_prize.get(position));
            editText.setText(P_Case_Qty.get(position));
            editText2.setText(P_S_Qty.get(position));
           // imageView.setImageResource(Images.get(position));
             NetworkImageView iv = (NetworkImageView) grid.findViewById(R.id.productcart_img);
             iv.setImageUrl(Images.get(position), imageLoader);
            imgbut1.setTag(new Integer(position));
            imgbut2.setTag(new Integer(position));
            imgbut3.setTag(new Integer(position));
            imgbut4.setTag(new Integer(position));
            imgbut5.setTag(new Integer(position));
            imgbut1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    int counter=Integer.parseInt(editText.getText().toString());
                    if(counter>=0) {
                        counter++;
                        editText.setText(Integer.toString(counter));
                        P_Case_Qty.set(position,Integer.toString(counter));
                    }

                }
            });

            imgbut2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    Log.e("position","position "+position);
                    int counter=Integer.parseInt(editText.getText().toString());
                    if(counter>=1) {
                        counter--;
                        editText.setText(Integer.toString(counter));
                        P_Case_Qty.set(position,Integer.toString(counter));
                    }
                }
            });
            imgbut4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    Log.e("position","position "+position);
                    int counter=Integer.parseInt(editText2.getText().toString());
                    if(counter>=0) {
                        counter++;
                        editText2.setText(Integer.toString(counter));
                        P_S_Qty.set(position,Integer.toString(counter));
                    }

                }
            });

            imgbut5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=(Integer)v.getTag();
                    Log.e("position","position "+position);
                    int counter=Integer.parseInt(editText2.getText().toString());
                    if(counter>=1) {
                        counter--;
                        editText2.setText(Integer.toString(counter));
                        P_S_Qty.set(position,Integer.toString(counter));
                    }
                }
            });
            imgbut3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position=(Integer)v.getTag();
                    Log.e("position","position "+position);
                    AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setMessage("Are you sure, you want to delete this item");
                    alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#FF0000'>Yes</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            P_Code.remove(position);
                            P_name.remove(position);
                            Images.remove(position);
                            P_prize.remove(position);
                            P_S_Qty.remove(position);
                            P_Case_Qty.remove(position);
                            P_S_price.remove(position);
                            P_Case_price.remove(position);
                            SGST.remove(position);
                            CGST.remove(position);
                            notifyDataSetChanged();
                        }
                    });
                    alertDialogBuilder.setNegativeButton(Html.fromHtml("<font color='#000000'>No</font>"), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(mContext,"You clicked No",Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog alertDialog=alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
    public String buyProduct(){
        ArrayList<JSONObject> list_obj=new ArrayList<JSONObject>(getCount());
        int s_price=0;
        int case_price=0;
        int case_qty=0;
        int s_qty=0;
        int Totl=0;
        for(int i = 0; i < getCount();i++) {
            try {
                JSONObject obj=new JSONObject();
                s_price=Integer.parseInt(P_S_price.get(i));
                case_price=Integer.parseInt(P_Case_price.get(i));
                s_qty=Integer.parseInt(P_S_Qty.get(i));
                case_qty=Integer.parseInt(P_Case_Qty.get(i));
                Totl=(s_qty*s_price)+(case_qty*case_price);
                obj.put("p_code", P_Code.get(i));
                obj.put("p_s_qty",P_S_Qty.get(i));
                obj.put("p_case_qty",P_Case_Qty.get(i));
                obj.put("tot_cost",Integer.toString(Totl));
                list_obj.add(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.e("List",list_obj.toString());
        return list_obj.toString();
    }
    public String Calculate(){
        double s_price=0;
        double case_price=0;
        int case_qty=0;
        int s_qty=0;
        double Totl=0;
        double sgst=0;
        double cgst=0;
        double Pay_Totl=0.0;
        for(int i=0;i<getCount();i++){
            s_price=Double.parseDouble(P_S_price.get(i));
            case_price=Double.parseDouble(P_Case_price.get(i));
            Log.e("S_price",Double.toString(s_price));
            Log.e("case_price",Double.toString(case_price));
            s_qty=Integer.parseInt(P_S_Qty.get(i));
            case_qty=Integer.parseInt(P_Case_Qty.get(i));
            sgst=Double.parseDouble(SGST.get(i));
            Log.e("sgst",Double.toString(sgst));
            cgst=Double.parseDouble(CGST.get(i));
            Log.e("cgst",Double.toString(cgst));
            Totl=Totl+(s_qty*s_price)+(case_qty*case_price);
            Pay_Totl=Pay_Totl+((s_qty*((sgst/100)+s_price))+((case_qty*((cgst/100)+case_price))));
            Log.e("Total",Double.toString(Pay_Totl));
        }
        return Double.toString(Pay_Totl);
    }
}
