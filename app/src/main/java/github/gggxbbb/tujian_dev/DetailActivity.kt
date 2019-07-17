@file:Suppress("DEPRECATION")

package github.gggxbbb.tujian_dev

import android.app.Activity
import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import github.gggxbbb.tujian_dev.tools.TujianPic

import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import org.json.JSONObject
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class DetailActivity : AppCompatActivity() {

    private val code = 52
    private lateinit var tujianPic: TujianPic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        tujianPic = TujianPic(JSONObject(intent.getStringExtra("pic")))
        Glide.with(this).load(tujianPic.getLink()).into(pic)
        pic_content.text = tujianPic.getContent()
        pic_title.text = tujianPic.getTitle()
        pic_info.text = String.format(
            "%s %s %s×%s @ %s",
            tujianPic.getDate(),
            tujianPic.getTNAME(this),
            tujianPic.getWidth(),
            tujianPic.getHeight(),
            tujianPic.getUsername()
        )
        info.setBackgroundColor(Color.parseColor(tujianPic.getThemeColor()))
        pic_content.setTextColor(Color.parseColor(tujianPic.getTextColor()))
        pic_content.setLinkTextColor(Color.parseColor(tujianPic.getTextColor()))
        pic_title.setTextColor(Color.parseColor(tujianPic.getTextColor()))
        pic_info.setTextColor(Color.parseColor(tujianPic.getTextColor()))

        title = tujianPic.getTitle()

        show_archive.setOnClickListener {
            //开归档
            val i = Intent(this, ArchiveActivity::class.java)
            i.putExtra("pic", tujianPic.getString())
            startActivity(i)
        }
        show_download.setOnClickListener {
            //下载
            Snackbar.make(conten_root, R.string.action_download_start, Snackbar.LENGTH_LONG).show()
            val i = Intent(Intent.ACTION_CREATE_DOCUMENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "image/jpeg"
            i.putExtra(
                Intent.EXTRA_TITLE,
                "${tujianPic.getDate()}-${tujianPic.getTNAME(this)}_${tujianPic.getTitle()}_${tujianPic.getPID()}.${tujianPic.getUsername()}.jpeg"
            )
            startActivityForResult(i, code)
        }
        show_setWallpaper.setOnClickListener {
            //设壁纸
            Snackbar.make(conten_root, R.string.action_set_start, Snackbar.LENGTH_LONG).show()
            setPic(tujianPic, conten_root)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                downloadPic(tujianPic, conten_root, data.data!!)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        //return super.onOptionsItemSelected(item)
        return true
    }

    private fun setPic(tujianPic: TujianPic, view: View) {
        Glide.with(this).asBitmap().load(tujianPic.getLink()).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val wallpaperManager = WallpaperManager.getInstance(this@DetailActivity)
                try {
                    wallpaperManager.setBitmap(resource)
                    Snackbar.make(view, R.string.action_set_finish, Snackbar.LENGTH_LONG).show()
                } catch (e: IOException) {
                    Snackbar.make(view, Objects.requireNonNull<String>(e.message), Snackbar.LENGTH_LONG).show()
                }

            }
        })
    }

    private fun downloadPic(tujianPic: TujianPic, view: View, uri: Uri) {
        Glide.with(this).asBitmap().load(tujianPic.getLink()).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                try {
                    val pfd: ParcelFileDescriptor? = contentResolver.openFileDescriptor(uri, "w")
                    val fileOutputStream = FileOutputStream(pfd?.fileDescriptor)
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.close()
                    pfd?.close()
                    Snackbar.make(view, R.string.action_download_finish, Snackbar.LENGTH_LONG).show()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Snackbar.make(view, R.string.actoin_download_failed, Snackbar.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                    Snackbar.make(view, R.string.actoin_download_failed, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}


