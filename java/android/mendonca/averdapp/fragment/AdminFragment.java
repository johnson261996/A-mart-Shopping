package android.mendonca.averdapp.fragment;

import android.content.Intent;
import android.mendonca.averdapp.R;
import android.mendonca.averdapp.activity.SignupActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by johnson on 04-05-2018.
 */

public class AdminFragment extends Fragment {

    private Button button_create;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin,container,false);
        button_create = (Button) view.findViewById(R.id.btn_create);
        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminFragment.this.getActivity(),SignupActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}
