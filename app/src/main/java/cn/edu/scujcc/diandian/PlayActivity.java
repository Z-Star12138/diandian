package cn.edu.scujcc.diandian;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

public class PlayActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView tvPlayerView;
    private MediaSource videoSource;
    private Channel channel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        channel = (Channel) getIntent().getSerializableExtra("Channel");
        updateUI();
        initPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player == null) {
            initPlayer();
            if (tvPlayerView != null) {
                tvPlayerView.onResume();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tvPlayerView != null) {
            tvPlayerView.onPause();
        }
        clean();
    }

    //重构，初始化播放器

    private void initPlayer() {
//        if (player == null) {
//            tvPlayerView = findViewById(R.id.tv_play);
        //创建播放器
        player = ExoPlayerFactory.newSimpleInstance(this);
        player.setPlayWhenReady(true);
        //从界面查找视图
        PlayerView playerView = findViewById(R.id.tv_play);
        //绑定界面与播放器
        playerView.setPlayer(player);
        //准备播放源
        Uri videoUrl = Uri.parse(channel.getUrl());
        Uri uri = Uri.parse("http://nclive.grtn.cn/jjkt/sd/live.m3u8");
        DataSource.Factory factory = new DefaultDataSourceFactory(this, "DianDian");
        MediaSource videoSource = new HlsMediaSource.Factory(factory).createMediaSource(uri);
//        }
        //把播放源传递给播放器(并开始播放)
        player.prepare(videoSource);
    }

    /**
     * 修改  初始化界面
     */
    private void updateUI() {
        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(this.channel.getTitle());
        // 修改清晰度
        TextView tvQuality = findViewById(R.id.tv_quality);
        tvQuality.setText(this.channel.getQuality());
    }


    //重构，释放与清理资源
    private void clean() {
        if (player != null) {
            player.release();
            player = null;
            videoSource = null;
        }
    }
}
