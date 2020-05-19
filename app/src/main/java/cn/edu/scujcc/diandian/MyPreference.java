package cn.edu.scujcc.diandian;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 用来处理share preference中保存数据
 */
public class MyPreference {
    private static MyPreference INSTANCE = null;
    private SharedPreferences prefs = null;
    private Context context = null;

    private MyPreference() {
    }

    public static MyPreference getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MyPreference();
        }
        return INSTANCE;
    }

    public void setup(Context ctx) {
        context = ctx;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveUser(String username) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(UserLab.USER_CURRENT, username);
        editor.apply();
    }

    public String currentUser() {
        return prefs.getString(UserLab.USER_CURRENT, "未登录");
    }
}
