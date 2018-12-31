package android.mendonca.averdapp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.activity.Profile_Edit;
import android.mendonca.averdapp.app.MyApplication;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by johnson on 04-05-2018.
 */

public class ProfileFragment extends Fragment {
    private CircleImageView circleImageView;
    private ImageView edit_profile;
    private TextView head_name,P_name,P_username,P_email,P_mobile,P_mobile2,P_add1,P_add2;
    private static int RESULT_LOAD_IMG = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        circleImageView = (CircleImageView) view.findViewById(R.id.profile);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        edit_profile = (ImageView) view.findViewById(R.id.edit);
        head_name = (TextView) view.findViewById(R.id.name);
        P_name = (TextView) view.findViewById(R.id.profile_name);
        P_username = (TextView) view.findViewById(R.id.profile_username);
        P_email = (TextView) view.findViewById(R.id.profile_email);
        P_mobile = (TextView) view.findViewById(R.id.profile_mobile);
        P_mobile2 = (TextView) view.findViewById(R.id.profile_mobile1);
        P_add1 = (TextView) view.findViewById(R.id.profile_addr1);
        P_add2 = (TextView) view.findViewById(R.id.profile_addr2);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(ProfileFragment.this.getActivity(),Profile_Edit.class);
                startActivity(i);
            }
        });
        fetch_profiledata();
        return view;
    }

    private void fetch_profiledata() {
                        try {
                            head_name.setText(MyApplication.getInstance().getPrefManager().getUser().getName());
                            P_name.setText(MyApplication.getInstance().getPrefManager().getUser().getName());
                            P_username.setText(MyApplication.getInstance().getPrefManager().getUser().getUsrName());
                            P_email.setText(MyApplication.getInstance().getPrefManager().getUser().getEmail());
                            P_mobile.setText(MyApplication.getInstance().getPrefManager().getUser().getPhone());
                            P_mobile2.setText(MyApplication.getInstance().getPrefManager().getUser().getPhone2());
                            P_add1.setText(MyApplication.getInstance().getPrefManager().getUser().getAddrs());
                            P_add2.setText(MyApplication.getInstance().getPrefManager().getUser().getAddrs2());
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                circleImageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(ProfileFragment.this.getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(ProfileFragment.this.getActivity(), "You haven't picked Image",Toast.LENGTH_LONG).show();
        }

    }

    private void getImageFromAlbum() {
        try {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }catch (Exception e){

        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        //clear the menu items in profile fragment
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }
}

