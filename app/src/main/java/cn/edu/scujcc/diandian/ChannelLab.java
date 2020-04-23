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
    private static ChannelLab INSTANCE;
    private List<Channel> data = new ArrayList<>();

    private ChannelLab() {
    }

    public static ChannelLab getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChannelLab();
        }
        return INSTANCE;
    }

    public void setData(List<Channel> newData) {
        this.data = newData;
    }

    /**
     * 生成测试数据，用于引入网络的项目
     */

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
                    //不能在此操作RecyLerView刷新页面，只能使用线程通讯数据传递到主线程
                    Message msg = new Message();
                    msg.obj = response.body();
                    handler.sendMessage(msg);
                } else {
                    Log.w("DianDian", "response没有数据");
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                //如果访问失败了
                Log.e("DianDian", "访问网络失败了。。。", t);

            }
        });
    }
}
