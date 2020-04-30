package cn.edu.scujcc.diandian;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class ChannelRvAdapter extends RecyclerView.Adapter<ChannelRvAdapter.ChannelRowHolder> {

    private ChannelLab lab = ChannelLab.getInstance();
    private ChannelClickListener listener;
    private Context context;

    public ChannelRvAdapter(Context context, ChannelClickListener lis) {
        this.listener = lis;
        this.context = context;
    }

    /**
     * 当需要新的一行时，此方法负责创建一行对应的对象，即ChannelRowHolder对象
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ChannelRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row, parent, false);
        ChannelRowHolder holder = new ChannelRowHolder(rowView);
        return holder;
    }

    /**
     * 确定列表有一共有几行(即多少个ChannelRowHolder对象)
     *
     * @return 返回几行
     */
    @Override
    public int getItemCount() {
        return lab.getSize();
    }

    /**
     * 用于确定每一行内容是什么，即填充行中各个视图内容
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ChannelRowHolder holder, int position) {
        Log.d("DD", "position=" + position);
        Channel c = lab.getChannel(position);
        holder.bind(c);
    }

    //自定义接口
    public interface ChannelClickListener {
        public void onChannelClick(int position);
    }

    /**
     * 单行布局对应的java控制类
     */
    public class ChannelRowHolder extends RecyclerView.ViewHolder {
        private TextView title;     //频道标题   实例对象
        private TextView quality;    //频道清晰度
        private ImageView cover;    //频道图片

        public ChannelRowHolder(@NonNull View row) {
            super(row);
            this.title = row.findViewById(R.id.channel_title);
            this.quality = row.findViewById(R.id.channel_quality);
            this.cover = row.findViewById(R.id.channel_cover);
            row.setOnClickListener((v) -> {
                int position = getLayoutPosition();
                Log.d("DianDian", position + "被点击了！");
                //TODO 调用实际的跳转代码
                listener.onChannelClick(position);
            });
        }

        /**
         * 自定义方法，用于向内部的title提供数据(便于使用私有title对象)
         *
         * @param c
         */
        public void bind(Channel c) {
            this.title.setText(c.getTitle());
            this.quality.setText(c.getQuality());
            //获得上下文
            Log.d("DianDian", c.getTitle() + "：准备从网络加载封面：" + c.getCover());
            Glide.with(context)
                    .load(c.getCover())
                    .placeholder(R.drawable.c1)
                    .into(this.cover);
//            this.cover.setImageResource(c.getCover());
        }
    }
}
