package github.gggxbbb.tujian_dev.java;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import github.gggxbbb.tujian_dev.R;
import github.gggxbbb.tujian_dev.tools.TujianPic;

import java.io.IOException;
import java.util.Objects;

public class PicUtils {
    static public void downloadPic(final Context context, final TujianPic tujianPic, final View view){
        Glide.with(context).asBitmap().load(tujianPic.getLink()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                // TODO: 2019/7/16 完成图片下载 
                Snackbar.make(view, R.string.action_download_finish,Snackbar.LENGTH_LONG).show();
            }
        });
    }
    static public void setPic(final Context context, final TujianPic tujianPic, final View view){
        Glide.with(context).asBitmap().load(tujianPic.getLink()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                try {
                    wallpaperManager.setBitmap(resource);
                    Snackbar.make(view, R.string.action_set_finish,Snackbar.LENGTH_LONG).show();
                }catch (IOException e){
                    Snackbar.make(view, Objects.requireNonNull(e.getMessage()),Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
