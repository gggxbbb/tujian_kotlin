package github.gggxbbb.tujian_dev

import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import github.gggxbbb.tujian_dev.java.PicUtils
import github.gggxbbb.tujian_dev.tools.TujianPic

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val tujianPic = TujianPic(JSONObject(intent.getStringExtra("pic")))
        Glide.with(this).load(tujianPic.getLink()).into(pic)
        pic_content.text = tujianPic.getContent()
        pic_title.text = tujianPic.getTitle()
        pic_info.text = String.format(
            "%s %s×%s @ %s",
            tujianPic.getTNAME(),
            tujianPic.getWidth(),
            tujianPic.getHeight(),
            tujianPic.getUsername()
        )
        info.setBackgroundColor(Color.parseColor(tujianPic.getThemeColor()))
        pic_content.setTextColor(Color.parseColor(tujianPic.getTextColor()))
        pic_title.setTextColor(Color.parseColor(tujianPic.getTextColor()))
        pic_info.setTextColor(Color.parseColor(tujianPic.getTextColor()))

        title = tujianPic.getTitle()

        show_archive.setOnClickListener { view ->
            //开归档
            //TODO 完成归档
            Toast.makeText(this, "Developing...", Toast.LENGTH_LONG).show()
        }
        show_download.setOnClickListener { view ->
            //下载
            Toast.makeText(this, "Developing...", Toast.LENGTH_LONG).show()
            //TODO 完成下载
            //Snackbar.make(fab, R.string.action_download_start, Snackbar.LENGTH_LONG).show()
            //PicUtils.downloadPic(this, tujianPic, fab)
        }
        show_setWallpaper.setOnClickListener { view ->
            //设壁纸
            Snackbar.make(fab, R.string.action_set_start, Snackbar.LENGTH_LONG).show()
            PicUtils.setPic(this, tujianPic, fab)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        //return super.onOptionsItemSelected(item)
        return true
    }

}



