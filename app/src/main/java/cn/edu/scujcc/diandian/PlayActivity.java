package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PlayActivity extends AppCompatActivity {
    private SimpleExoPlayer player;
    private PlayerView tvPlayerView;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private TextView tvName, tvQuality;
    private Button sendButton;
    private Channel currentChannel;
    private List<Comment> hostComment;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 2:
                    //显示热门评论
                    hostComment = (List<Comment>) msg.obj;
                    updateUI();
                    break;
                case 3:
                    //评论成功了，提示用户
                    Toast.makeText(PlayActivity.this, "感谢留言！",
                            Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    //评论失败了，提醒用户
                    Toast.makeText(PlayActivity.this, "评论失败！稍后重试！",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    private ChannelLab lab = ChannelLab.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Serializable s = getIntent().getSerializableExtra("channel");
        Log.d("DianDian", "取得当前频道数据是：" + s);
        if (s != null && s instanceof Channel) {
            currentChannel = (Channel) s;
            updateUI();
            sendButton = findViewById(R.id.send);
            sendButton.setOnClickListener(v -> {
                EditText t = findViewById(R.id.message);
                Comment c = new Comment();
                c.setAuthor("MyApp");
                c.setStar(6);
                c.setContent(t.getText().toString());//用户在界面中所输入的评论
                lab.addComment(currentChannel.getId(), c, handler);
            });
        }

    }

    /**
     * 修改  初始化界面
     */
    private void updateUI() {
        tvName = findViewById(R.id.tv_name);
        tvQuality = findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());

        if (hostComment != null && hostComment.size() > 0) {
            Comment c1 = hostComment.get(0);
            TextView c1_author, c1_date, c1_content, c1_star;
            c1_author = findViewById(R.id.c1_author);
            c1_date = findViewById(R.id.c1_dt);
            c1_content = findViewById(R.id.c1_content);
            c1_star = findViewById(R.id.c1_star);

            c1_author.setText(c1.getAuthor());
            c1_date.setText(dateFormat.format(c1.getDt()));
            c1_content.setText(c1.getContent());
            c1_star.setText(c1.getStar() + "");
        }
        if (hostComment != null && hostComment.size() > 1) {
            Comment c2 = hostComment.get(0);
            TextView c2_author, c2_date, c2_content, c2_star;
            c2_author = findViewById(R.id.c2_author);
            c2_date = findViewById(R.id.c2_dt);
            c2_content = findViewById(R.id.c2_content);
            c2_star = findViewById(R.id.c2_star);

            c2_author.setText(c2.getAuthor());
            c2_date.setText(dateFormat.format(c2.getDt()));
            c2_content.setText(c2.getContent());
            c2_star.setText(c2.getStar() + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    protected void onStart() {
        super.onStart();
        initPlayer();
        if (tvPlayerView != null) {
            tvPlayerView.onResume();
        }
    }

    protected void onStop() {
        super.onStop();
        if (tvPlayerView != null) {
            tvPlayerView.onPause();
        }
        clean();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lab.getHotComment(currentChannel.getId(), handler);
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

    /**
     * 重构，初始化播放器
     */
    private void initPlayer() {
        //创建播放器
        player = ExoPlayerFactory.newSimpleInstance(this);
        player.setPlayWhenReady(true);
        //从界面查找视图
        PlayerView playerView = findViewById(R.id.tv_play);
        //绑定界面与播放器
        playerView.setPlayer(player);
        //准备播放源
        Uri uri = Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        if (currentChannel != null) {
            //使用当前网址
            uri = Uri.parse(currentChannel.getUrl());
        }
        DataSource.Factory factory = new DefaultDataSourceFactory(this, "DianDian");
        MediaSource videoSource = new HlsMediaSource.Factory(factory).createMediaSource(uri);
        //把播放源传递给播放器(并开始播放)
        player.prepare(videoSource);
    }

    //重构，释放与清理资源
    private void clean() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
