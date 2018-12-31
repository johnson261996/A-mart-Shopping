package android.mendonca.averdapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.mendonca.averdapp.R;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by johnson on 19-05-2018.
 */

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            //showing the splash screen with a timer useful to show app logo
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(i);
                //close the splash Activity
                finish();
            }
        },2000);
    }
}
