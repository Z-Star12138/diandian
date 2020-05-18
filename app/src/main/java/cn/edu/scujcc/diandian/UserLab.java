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
    public final static int USER_PASSWORD_FAIL = 0;
    public final static int USER_REGISTER_SUCCESS = 2;
    public final static int USER_REGISTER_FAIL = -2;
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
                    Log.d(TAG, "登陆成功！" + response.body().toString());
                    String result = response.body().toString();
                    if (result.equals("1")) {
                        Message msg = new Message();
                        msg.what = USER_LOGIN_SUCCESS;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = USER_LOGIN_FAIL;
                        handler.sendMessage(msg);
                    }
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

    /**
     * 想用户发送注册信息
     *
     * @param user 用户填写的信息
     */

    public void register(User user, Handler handler) {
        Retrofit retrofit = RetrofitClient.get();
        UserApi api = retrofit.create(UserApi.class);
        Log.d(TAG, "准备注册的用户信息是：" + user);
        Call<User> call = api.register(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "登陆成功！");
                Log.d(TAG, "返回数据是：" + response.body().toString());
                Message msg = new Message();
                msg.what = USER_LOGIN_SUCCESS;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "注册失败", t);
                Message msg = new Message();
                msg.what = USER_REGISTER_FAIL;
                handler.sendMessage(msg);
            }
        });
    }
}
