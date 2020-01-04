package github.gggxbbb.tujian_dev.tools

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import github.gggxbbb.tujian_dev.DetailActivity
import github.gggxbbb.tujian_dev.R
import github.gggxbbb.tujian_dev.tools.PicsAdapter.TujianViewHolder

class PicsAdapter(
    private val data: MutableList<TujianPic>,
    private val mContext: Context
) : RecyclerView.Adapter<TujianViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TujianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_item, parent, false)
        return TujianViewHolder(view)
    }

    override fun onBindViewHolder(holder: TujianViewHolder, position: Int) {
        val beauty = data[position]
        Glide.with(mContext).load(beauty.getLink()).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable?>,
                isFirstResource: Boolean
            ): Boolean {
                holder.onLoading.visibility = View.GONE
                holder.infoText.text = e?.message
                return false
            }
            override fun onResourceReady(
                resource: Drawable?,
                model: Any,
                target: Target<Drawable?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }).into(holder.pic)
        holder.pic.minimumHeight = beauty.getHeight() * (holder.pic.width / beauty.getWidth())
        holder.content.text = beauty.getContent()
        holder.title.text = beauty.getTitle()
        holder.info.text = String.format(
            "%s %s %s×%s @ %s",
            beauty.getDate(),
            beauty.getTNAME(mContext),
            beauty.getWidth(),
            beauty.getHeight(),
            beauty.getUsername()
        )
        holder.linearLayout.setBackgroundColor(Color.parseColor(beauty.getThemeColor()))
        holder.content.setTextColor(Color.parseColor(beauty.getTextColor()))
        holder.content.setLinkTextColor(Color.parseColor(beauty.getTextColor()))
        holder.title.setTextColor(Color.parseColor(beauty.getTextColor()))
        holder.info.setTextColor(Color.parseColor(beauty.getTextColor()))
        holder.root.setOnClickListener {
            val intent = Intent(mContext, DetailActivity::class.java)
            intent.putExtra("pic", beauty.getString())
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class TujianViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var pic: ImageView = itemView.findViewById(R.id.pic)
        var title: TextView = itemView.findViewById(R.id.pic_title)
        var info: TextView = itemView.findViewById(R.id.pic_info)
        var content: TextView = itemView.findViewById(R.id.pic_content)
        var infoText: TextView = itemView.findViewById(R.id.info_text)
        var linearLayout: LinearLayout = itemView.findViewById(R.id.info)
        var root: View = itemView.rootView
        var onLoading: ProgressBar = itemView.findViewById(R.id.onLoading)

    }

    fun addItem(tujianPic: TujianPic) {
        data.add(tujianPic)
        notifyItemInserted(data.size)
    }

}