package cn.edu.scujcc.diandian;

import android.os.Message;
import android.util.Log;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 举例数据，提供频道数据
 * 使用了单例模式，保证数据只有一份
 */

public class ChannelLab {
    //单例第1步
    private static ChannelLab INSTANCE;
    private List<Channel> data;

    //单例第2步
    private ChannelLab() {
        data = new ArrayList<>();
    }

    //单例第3步
    public static ChannelLab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChannelLab();
        }
        return INSTANCE;
    }

    /**
     * 获取当前数据资源中总共有多少个频道
     *
     * @return
     */
    public int getSize() {
        return data.size();
    }

    /**
     * 获取一个指定频道
     *
     * @param position 频道序号
     * @return 频道对象Channel
     */
    public Channel getChannel(int position) {
        return data.get(position);
    }

    /**
     * 访问网络得到真实数据，代替以前的的tese()方法
     */
    public void getData(Handler handler) {
        Retrofit retrofit = RetrofitClient.get();

        ChannelAPI api = retrofit.create(ChannelAPI.class);
        Call<List<Channel>> call = api.getAllChannels();
        //enqueue把代码放在子线程中运行
        call.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call,
                                   Response<List<Channel>> response) {
                //如果访问成功
                if (null != response && null != response.body()) {
                    Log.d("DianDian", "Succeed,从阿里云获取的数据是：");
                    Log.d("DianDian", response.body().toString());
                    data = response.body();
                    //不能在此操作RecyLerView刷新页面，只能使用线程通讯数据传递到主线程
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    Log.w("DianDian", "response没有数据");
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                //如果访问失败了
                Log.e("DianDian", "访问网络失败了。。。", t);

            }
        });
    }

    public void getHotComment(String channelId, Handler handler) {
        //调用单例
        Retrofit retrofit = RetrofitClient.get();
        ChannelAPI api = retrofit.create(ChannelAPI.class);
        Call<List<Comment>> call = api.getHotComments(channelId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                Log.d("DianDian", "Succeed,从阿里云获取的数据是：");
                Log.d("DianDian", response.body().toString());
                //发通知
                Message msg = new Message();
                msg.what = 2; //自己规定2，代表从阿里云获取频道。
                msg.obj = response.body();
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.e("DianDian", "访问网络失败了。。。", t);
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 添加评论
     */
    public void addComment(String channelId, Comment comment, Handler handler) {
        //调用单例
        Retrofit retrofit = RetrofitClient.get();
        ChannelAPI api = retrofit.create(ChannelAPI.class);
        Call<Channel> call = api.addComment(channelId, comment);
        call.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                Log.d("DianDian", "Succeed!!!从阿里云获取的数据是：");
                Log.d("DianDian", response.body().toString());
                Message msg = new Message();
                msg.what = 3;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.e("DianDian", "访问网络失败！", t);
                Message msg = new Message();
                msg.what = 4;
                handler.sendMessage(msg);
            }
        });
    }
}
