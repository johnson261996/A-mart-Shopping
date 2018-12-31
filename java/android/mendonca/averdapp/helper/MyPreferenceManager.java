package android.mendonca.averdapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import android.mendonca.averdapp.model.User;

/**
 * Created by Lincoln on 07/01/16.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AveRdApp_data";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USER_USRNAME = "user_usrname";
    private static final String KEY_USER_MOBILE = "user_mobile";
    private static final String KEY_USER_MOBILE1 = "user_mobile1";
    private static final String KEY_USER_ADDRS = "user_addrs";
    private static final String KEY_USER_ADDRS1 = "user_addrs1";
    //private static final String KEY_NOTIFICATIONS = "notifications";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_TYPE, user.getType());
        editor.putString(KEY_USER_USRNAME,user.getUsrName());
        editor.putString(KEY_USER_MOBILE,user.getPhone());
        editor.putString(KEY_USER_MOBILE1,user.getPhone2());
        editor.putString(KEY_USER_ADDRS,user.getAddrs());
        editor.putString(KEY_USER_ADDRS1,user.getAddrs2());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getName() + ", " + user.getEmail());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, email, type,usrname,phone,phone1,addrs,addrs1;
            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            type = pref.getString(KEY_USER_TYPE, null);
            usrname=pref.getString(KEY_USER_USRNAME,null);
            phone=pref.getString(KEY_USER_MOBILE,null);
            phone1=pref.getString(KEY_USER_MOBILE1,null);
            addrs=pref.getString(KEY_USER_ADDRS,null);
            addrs1=pref.getString(KEY_USER_ADDRS1,null);
            User user = new User(id, name, email, type,usrname,phone,phone1,addrs,addrs1);
            return user;
        }
        return null;
    }
    public void clear() {
        editor.clear();
        editor.commit();
    }
}
