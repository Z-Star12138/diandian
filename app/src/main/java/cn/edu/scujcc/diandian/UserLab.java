package cn.edu.scujcc.diandian;

import android.os.Message;
import android.util.Log;

import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UserLab {
    public final static int USER_LOGIN_SUCCESS = 1;
    public final static int USER_LOGIN_FAIL = -1;
    private final static String TAG = "DianDian";
    //单例
    private static UserLab INSTANCE = null;

    private UserLab() {
    }

    public static UserLab getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new UserLab();
        }
        return INSTANCE;
    }

    //登陆
    public void login(String username, String password, Handler handler) {
        Retrofit retrofit = RetrofitClient.get();
        ChannelAPI api = retrofit.create(ChannelAPI.class);
        Call<Integer> call = api.login(username, password);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.body() != null) {
                    Log.d(TAG, "登陆成功！");
                    Log.d(TAG, response.body().toString());
                    Message msg = new Message();
                    msg.what = USER_LOGIN_SUCCESS;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = USER_LOGIN_FAIL;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d(TAG, "登陆失败！", t);
                Message msg = new Message();
                msg.what = USER_LOGIN_FAIL;
                handler.sendMessage(msg);
            }
        });
    }
}
