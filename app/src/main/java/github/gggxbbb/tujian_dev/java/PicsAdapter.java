package github.gggxbbb.tujian_dev.java;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import github.gggxbbb.tujian_dev.R;
import github.gggxbbb.tujian_dev.tools.TujianPic;

import java.util.List;

public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.BeautyViewHolder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 数据集合
     */
    private List<TujianPic> data;

    public PicsAdapter(List<TujianPic> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public BeautyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //加载item 布局文件
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item, parent, false);
        return new BeautyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BeautyViewHolder holder, int position) {
        //将数据设置到item上
        TujianPic beauty = data.get(position);
        Glide.with(mContext).load(beauty.getLink()).into(holder.pic);
        holder.pic.setMinimumHeight(beauty.getHeight()*(holder.pic.getWidth()/beauty.getWidth()));
        holder.content.setText(beauty.getContent());
        holder.title.setText(beauty.getTitle());
        holder.info.setText(String.format("%s @ %s", beauty.getTNAME(),beauty.getUsername()));
        holder.linearLayout.setBackgroundColor(Color.parseColor(beauty.getThemeColor()));
        holder.content.setTextColor(Color.parseColor(beauty.getTextColor()));
        holder.title.setTextColor(Color.parseColor(beauty.getTextColor()));
        holder.info.setTextColor(Color.parseColor(beauty.getTextColor()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title,info,content;
        LinearLayout linearLayout;
        public BeautyViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.pic_title);
            info = itemView.findViewById(R.id.pic_info);
            content = itemView.findViewById(R.id.pic_content);
            linearLayout = itemView.findViewById(R.id.info);
        }
    }
}
