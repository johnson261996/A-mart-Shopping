package android.mendonca.averdapp.activity;

import android.content.Context;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.CustomVolleyRequest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> Bill_No = new ArrayList<>();
    private ArrayList<String> P_Name = new ArrayList<>();
    private ArrayList<String> S_Qty = new ArrayList<>();
    private ArrayList<String> C_Qty = new ArrayList<>();
    private ArrayList<String> Addrss = new ArrayList<>();
    private ArrayList<String> Status = new ArrayList<>();
    private ArrayList<String> Grand_Tot = new ArrayList<>();
    private ArrayList<String> O_Date = new ArrayList<>();

    public OrderAdapter(Context mContext, ArrayList billNo,ArrayList p_Name,ArrayList s_Qty,ArrayList c_Qty, ArrayList addrss, ArrayList status, ArrayList grand_Tot, ArrayList o_Date) {
        this.mContext = mContext;
        Bill_No=billNo;
        P_Name=p_Name;
        S_Qty=s_Qty;
        C_Qty=c_Qty;
        Addrss=addrss;
        Status=status;
        Grand_Tot=grand_Tot;
        O_Date=o_Date;
    }
    @Override
    public int getCount() {
        return Bill_No.size();
    }

    @Override
    public Object getItem(int position) {
        return Bill_No.get(position);
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
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.activity_order_item, null);
            android.support.v7.widget.AppCompatTextView textView = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_id);
            android.support.v7.widget.AppCompatTextView textView1 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_name);
            android.support.v7.widget.AppCompatTextView textView2 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_Sqty);
            android.support.v7.widget.AppCompatTextView textView3 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_Cqty);
            android.support.v7.widget.AppCompatTextView textView4 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_address);
            android.support.v7.widget.AppCompatTextView textView5 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_status);
            android.support.v7.widget.AppCompatTextView textView6 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_prize);
            android.support.v7.widget.AppCompatTextView textView7 = (android.support.v7.widget.AppCompatTextView) grid.findViewById(R.id.order_date);

            textView.setText(Bill_No.get(position));
            textView1.setText(P_Name.get(position));
            textView2.setText(S_Qty.get(position));
            textView3.setText(C_Qty.get(position));
            textView4.setText(Addrss.get(position));
            textView5.setText(Status.get(position));
            textView6.setText(Grand_Tot.get(position));
            textView7.setText(O_Date.get(position));
        } else {
            grid = (View) convertView;
        }
        return grid;
    }

}
