package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    //    实例变量 channelRV
    private RecyclerView channelRV;
    private ChannelRvAdapter rvAdapter;
    private ChannelLab lab = ChannelLab.getInstance();
    private final static String TAG = "DianDian";
    //线程通讯第1步，在主线程创建Handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ChannelLab.MSG_CHANNEL:
                    rvAdapter.notifyDataSetChanged();
                    break;
                case ChannelLab.MSG_NET_FAILURE:
                    failed();
                    break;
            }
        }
    };

    private void failed() {
        Toast.makeText(MainActivity.this, "token无效，禁止访问", Toast.LENGTH_LONG).show();
        Log.w(TAG, "禁止访问，因为token无效");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.channelRV = findViewById(R.id.channel_rv);
        //lambda简化
//        ChannelRvAdapter rvAdapter = new ChannelRvAdapter(this, this);//创建适配器rvAdapter
        rvAdapter = new ChannelRvAdapter(MainActivity.this, p -> {
            //跳转到新界面，使用意图Intent
            Intent intent = new Intent(MainActivity.this, PlayActivity.class);
            //通过位置p得到当前频道channel，传递用户选中的频道到下一个界面
            Channel c = lab.getChannel(p);
            intent.putExtra("channel", c);
            startActivity(intent);
        });

        this.channelRV.setAdapter(rvAdapter);
        this.channelRV.setLayoutManager(new LinearLayoutManager(this));//从上往下布局
    }

    @Override
    protected void onResume() {
        super.onResume();
        //把主线程的handler传递给子线程使用
        lab.getData(handler);
    }

    /**
     * 刷新即初始化数据
     */
//    private void initDate() {
//        //得到数据后，去更新界面
//        Handler handler = new Handler() {
//            //快捷键Ctrl O
//            @Override
//            public void handleMessage(@Nullable Message msg) {
//                //若收到来自其它线程的数据，则运行以下代码
//                rvAdapter.notifyDataSetChanged();
//            }
//        };
//        lab.getData(handler);
//    }
}
