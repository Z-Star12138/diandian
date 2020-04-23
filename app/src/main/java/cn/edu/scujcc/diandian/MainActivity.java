package cn.edu.scujcc.diandian;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements ChannelRvAdapter.ChannelClickListener {
    //    实例变量 channelRV
    private RecyclerView channelRV;
    private ChannelRvAdapter rvAdapter;
    private ChannelLab lab = ChannelLab.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.channelRV = findViewById(R.id.channel_rv);
        //lambda简化
        ChannelRvAdapter rvAdapter = new ChannelRvAdapter(this, this);//创建适配器rvAdapter
        this.channelRV.setAdapter(rvAdapter);
        this.channelRV.setLayoutManager(new LinearLayoutManager(this));//从上往下布局
        initDate();
    }

    @Override
    public void onChannelClick(int position) {
        Log.d("DianDian", "用户点击频道是" + position);
        Channel c = lab.getChannel(position);
        //跳转到新界面，使用意图Intent
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        intent.putExtra("Channel", c);
        startActivity(intent);


        //得到此网络上的数据后，更新界面
    }

    //刷新即初始化数据
    private void initDate() {
        //得到数据后，去更新界面
        Handler handler = new Handler() {
            //快捷键Ctrl O
            @Override
            public void handleMessage(@Nullable Message msg) {
                //若收到来自其它线程的数据，则运行以下代码
                rvAdapter.notifyDataSetChanged();
            }
        };
        lab.getData(handler);
    }
}
