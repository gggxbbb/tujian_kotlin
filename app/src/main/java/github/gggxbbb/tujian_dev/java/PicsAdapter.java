package github.gggxbbb.tujian_dev.java;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import github.gggxbbb.tujian_dev.DetailActivity;
import github.gggxbbb.tujian_dev.R;
import github.gggxbbb.tujian_dev.tools.TujianPic;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PicsAdapter extends RecyclerView.Adapter<PicsAdapter.BeautyViewHolder> {

    private Context mContext;

    private List<TujianPic> data;

    public PicsAdapter(List<TujianPic> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @NotNull
    @Override
    public BeautyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item, parent, false);
        return new BeautyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull final BeautyViewHolder holder, int position) {
        final TujianPic beauty = data.get(position);
        Glide.with(mContext).load(beauty.getLink()).into(holder.pic);
        holder.pic.setMinimumHeight(beauty.getHeight()*(holder.pic.getWidth()/beauty.getWidth()));
        holder.content.setText(beauty.getContent());
        holder.title.setText(beauty.getTitle());
        holder.info.setText(String.format("%s %s×%s @ %s", beauty.getTNAME(),beauty.getWidth(),beauty.getHeight(),beauty.getUsername()));
        holder.linearLayout.setBackgroundColor(Color.parseColor(beauty.getThemeColor()));
        holder.content.setTextColor(Color.parseColor(beauty.getTextColor()));
        holder.title.setTextColor(Color.parseColor(beauty.getTextColor()));
        holder.info.setTextColor(Color.parseColor(beauty.getTextColor()));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("pic",beauty.getString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class BeautyViewHolder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView title,info,content;
        LinearLayout linearLayout;
        View root;
        CardView pic_root;
        BeautyViewHolder(View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.pic_title);
            info = itemView.findViewById(R.id.pic_info);
            content = itemView.findViewById(R.id.pic_content);
            linearLayout = itemView.findViewById(R.id.info);
            root = itemView.getRootView();
            pic_root = itemView.findViewById(R.id.pic_root);
        }
    }
}
