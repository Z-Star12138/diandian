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
    public final static int MSG_CHANNEL = 1;
    public final static int MSG_HOT_COMMENT = 2;
    public final static int MSG_ADD_COMMENT = 3;
    public final static int MSG_NET_FAILURE = 4;
    //用常量代替编码内容
    private final static String TAG = "DianDian";
    //单例第1步
    private static ChannelLab INSTANCE = null;
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
        Call<Result<List<Channel>>> call = api.getAllChannels();
        //enqueue把代码放在子线程中运行
        call.enqueue(new Callback<Result<List<Channel>>>() {
            @Override
            public void onResponse(Call<Result<List<Channel>>> call,
                                   Response<Result<List<Channel>>> response) {
                //如果访问成功
                if (response.code() == 403) {//说明缺少token 或者token错误
                    Message msg = new Message();
                    msg.what = MSG_NET_FAILURE;
                    handler.sendMessage(msg);
                } else if (null != response && null != response.body()) {
                    Log.d(TAG, "Succeed,从阿里云获取的数据是：");
                    Log.d(TAG, response.body().toString());
                    Result<List<Channel>> result = response.body();
                    data = result.getData();
                    //不能在此操作RecyLerView刷新页面，只能使用线程通讯数据传递到主线程
                    Message msg = new Message();
                    msg.what = MSG_CHANNEL;
                    handler.sendMessage(msg);
                } else {
                    Log.w(TAG, "response没有数据");
                }
            }

            @Override
            public void onFailure(Call<Result<List<Channel>>> call, Throwable t) {
                //如果访问失败了
                Log.e(TAG, "访问网络失败了。。。", t);
            }
        });
    }

    public List<Comment> getHotComment(String channelId, Handler handler) {
        List<Comment> result = null;
        //调用单例
        Retrofit retrofit = RetrofitClient.get();
        ChannelAPI api = retrofit.create(ChannelAPI.class);
        Call<Result<List<Comment>>> call = api.getHotComments(channelId);
        call.enqueue(new Callback<Result<List<Comment>>>() {
            @Override
            public void onResponse(Call<Result<List<Comment>>> call, Response<Result<List<Comment>>> response) {
                if (response.code() == 403) {
                    Message msg = new Message();
                    msg.what = MSG_NET_FAILURE;
                    handler.sendMessage(msg);
                } else if (null != response && null != response.body()) {
                    Log.d(TAG, "获取的热门频道是：");
                    Log.d(TAG, response.body().toString());
                    //发通知
                    Message msg = new Message();
                    msg.what = MSG_HOT_COMMENT;
                    msg.obj = response.body();
                    handler.sendMessage(msg);
                } else {
                    Log.w(TAG, "response没有数据！");
                }
            }

            @Override
            public void onFailure(Call<Result<List<Comment>>> call, Throwable t) {
                Log.e(TAG, "访问网络失败了。。。", t);
                Message msg = new Message();
                msg.what = MSG_NET_FAILURE;
                handler.sendMessage(msg);
            }
        });
        return result;
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
                Log.d(TAG, "添加评论后获取的数据是：");
                Log.d(TAG, response.body().toString());
                Message msg = new Message();
                msg.what = MSG_ADD_COMMENT;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                Log.e("DianDian", "访问网络失败！", t);
                Message msg = new Message();
                msg.what = MSG_NET_FAILURE;
                handler.sendMessage(msg);
            }
        });
    }
}
