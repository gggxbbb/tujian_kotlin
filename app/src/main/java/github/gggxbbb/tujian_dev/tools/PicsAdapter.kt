package github.gggxbbb.tujian_dev.tools

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
//import com.zzhoujay.richtext.RichText
import github.gggxbbb.tujian_dev.DetailActivity
import github.gggxbbb.tujian_dev.R
import github.gggxbbb.tujian_dev.tools.PicsAdapter.TujianViewHolder

class PicsAdapter(
    private val data: MutableList<TujianPic>,
    private val activity: Activity
) : RecyclerView.Adapter<TujianViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TujianViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_main_item, parent, false)
        return TujianViewHolder(view)
    }

    override fun onBindViewHolder(holder: TujianViewHolder, position: Int) {
        val tujianPic = data[position]
        holder.pic.load(tujianPic.getLink()) {
            crossfade(true)
            listener(onStart = {
                holder.onLoading.visibility = View.VISIBLE
            },
                onSuccess = { _, _ ->
                    holder.onLoading.visibility = View.GONE
                },
                onError = { _, e ->
                    holder.onLoading.visibility = View.GONE
                    holder.infoText.text = e.message
                })
        }
        holder.pic.minimumHeight = tujianPic.getHeight() * (holder.pic.width / tujianPic.getWidth())
        //RichText.fromMarkdown(tujianPic.getContent()).into(holder.content)
        //holder.content.text = tujianPic.getContent()
        holder.title.text = tujianPic.getTitle()
        /*holder.info.text = String.format(
            "%s %s %s×%s @ %s",
            tujianPic.getDate(),
            tujianPic.getTNAME(),
            tujianPic.getWidth(),
            tujianPic.getHeight(),
            tujianPic.getUsername()
        )*/
        holder.linearLayout.setBackgroundColor(Color.parseColor(tujianPic.getThemeColor()))
        /*holder.content.setTextColor(Color.parseColor(tujianPic.getTextColor()))
        holder.content.setLinkTextColor(Color.parseColor(tujianPic.getTextColor()))*/
        holder.title.setTextColor(Color.parseColor(tujianPic.getTextColor()))
        /*holder.info.setTextColor(Color.parseColor(tujianPic.getTextColor()))*/
        holder.root.setOnClickListener {
            val intent = Intent(activity, DetailActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            }
            intent.putExtra("pic", tujianPic.getString())
            activity.startActivity(intent)
        }

        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY

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