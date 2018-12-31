package android.mendonca.averdapp.activity;

import android.content.Context;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.app.CustomVolleyRequest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import java.util.ArrayList;
import  android.widget.LinearLayout;

/**
 * Created by johnson on 12-05-2018.
 */

public class GridAdapter extends BaseAdapter {
    private ImageLoader imageLoader;
    private Context mContext;
    private ArrayList<String> p_id = new ArrayList<>();
    private ArrayList<String> p_name = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();
    private ArrayList<String> p_price = new ArrayList<>();

    public GridAdapter(Context mContext, ArrayList<String> p_id,ArrayList<String> p_name, ArrayList<String> p_price, ArrayList<String> images) {
        this.mContext = mContext;
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_price = p_price;
        this.images = images;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if (imageLoader == null)
                imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
            NetworkImageView networkImageView = new NetworkImageView(mContext);
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_item, null);

            // LinearLayout linearLayout = new LinearLayout(mContext);
            //linearLayout.setOrientation(LinearLayout.VERTICAL);
            TextView textView = (TextView) grid.findViewById(R.id.product_name);
            TextView textView2 = (TextView) grid.findViewById(R.id.product_price);
            TextView textView3 = (TextView) grid.findViewById(R.id.product_other);

            imageLoader = CustomVolleyRequest.getInstance(mContext).getImageLoader();
            imageLoader.get(images.get(position), ImageLoader.getImageListener(networkImageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));
            networkImageView.setImageUrl(images.get(position), imageLoader);
            textView.setText(p_name.get(position));
            textView2.setText(p_price.get(position));
            textView3.setText("Rs.");
            NetworkImageView iv = (NetworkImageView) grid.findViewById(R.id.pic);
            iv.setImageUrl(images.get(position), imageLoader);

        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}