@file:Suppress("DEPRECATION")

package github.gggxbbb.tujian_dev

import android.annotation.SuppressLint
import android.app.Activity
import android.app.WallpaperManager
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import coil.Coil
import coil.load
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.snackbar.Snackbar
//import com.zzhoujay.richtext.RichText
import github.gggxbbb.tujian_dev.tools.TujianPic
import github.gggxbbb.tujian_dev.tools.getColumns
import github.gggxbbb.tujian_dev.tools.getLink
import github.gggxbbb.tujian_dev.tools.isPad
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.content_detail_info.*
import kotlinx.android.synthetic.main.content_detail_pic.*
import kotlinx.android.synthetic.main.content_detail_pic.onLoading
import kotlinx.android.synthetic.main.content_main.*
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

        pic.load(tujianPic.getLink()) {
            crossfade(true)
            listener(onStart = {
                onLoading.visibility = View.VISIBLE
            },
                onSuccess = { _, _ ->
                    onLoading.visibility = View.GONE

                    val params: ViewGroup.LayoutParams = pic.layoutParams
                    params.height = pic.width * tujianPic.getHeight() / tujianPic.getWidth()
                    pic.layoutParams = params

                },
                onError = { _, e ->
                    onLoading.visibility = View.GONE
                    info_text.text = e.message
                })
        }
        //pic_content.text = tujianPic.getContent()
        //RichText.fromMarkdown(tujianPic.getContent()).into(pic_content)
        pic_title.text = tujianPic.getTitle()
        pic_info.text = String.format(
            "%s %s %s×%s @ %s",
            tujianPic.getDate(),
            tujianPic.getTNAME(),
            tujianPic.getWidth(),
            tujianPic.getHeight(),
            tujianPic.getUsername()
        )
        //info.setBackgroundColor(tujianPic.getThemeColorInt())
        //pic_content.setTextColor(tujianPic.getTextColorInt())
        //pic_content.setLinkTextColor(tujianPic.getTextColorInt())
        //pic_title.setTextColor(tujianPic.getTextColorInt())
        //pic_info.setTextColor(tujianPic.getTextColorInt())

        title = tujianPic.getTitle()

        show_archive.setOnClickListener {
            //开归档
            val i = Intent(this, ArchiveActivity::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.addFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT)
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            i.putExtra("pic", tujianPic.getString())
            startActivity(i)
        }
        show_download.setOnClickListener {
            //下载
            Snackbar.make(content_root, R.string.action_download_start, Snackbar.LENGTH_LONG).show()
            val i = Intent(Intent.ACTION_CREATE_DOCUMENT)
            i.addCategory(Intent.CATEGORY_OPENABLE)
            i.type = "image/jpeg"
            i.putExtra(
                Intent.EXTRA_TITLE,
                "${tujianPic.getDate()}-${tujianPic.getTNAME()}_${tujianPic.getTitle()}_${tujianPic.getPID()}.${tujianPic.getUsername()}.jpeg"
            )
            startActivityForResult(i, code)
        }
        show_setWallpaper.setOnClickListener {
            //设壁纸
            Snackbar.make(content_root, R.string.action_set_start, Snackbar.LENGTH_LONG).show()
            setPic(tujianPic, content_root)
        }
        show_copy_link.setOnClickListener {
            //复制链接
            val cmb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cmb.text = getLink(tujianPic)
            Snackbar.make(content_root, R.string.action_copy_finish, Snackbar.LENGTH_LONG).show()
        }
        show_browser.setOnClickListener {
            //开浏览器
            val uri = Uri.parse(getLink(tujianPic))
            val builder = CustomTabsIntent.Builder()
            val tab = builder.build()
            tab.launchUrl(this, uri)
        }
        show_share.setOnClickListener {
            //分享
            val i = Intent(Intent.ACTION_SEND)
            i.putExtra(
                Intent.EXTRA_TEXT,
                "《${tujianPic.getTitle()}》\n${tujianPic.getContent()}\n${getLink(tujianPic)}"
            )
            i.type = "text/plain"
            startActivity(Intent.createChooser(i, getString(R.string.title_share)))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                downloadPic(tujianPic, content_root, data.data!!)
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

    @SuppressLint("MissingPermission")
    private fun setPic(tujianPic: TujianPic, view: View) {
        Coil.enqueue(
            ImageRequest.Builder(this)
                .data(Uri.parse(tujianPic.getLinkHD()))
                .target(
                    onSuccess = {
                        val wallpaperManager = WallpaperManager.getInstance(this@DetailActivity)
                        try {
                            wallpaperManager.setBitmap(it.toBitmap())
                            Snackbar.make(view, R.string.action_set_finish, Snackbar.LENGTH_LONG)
                                .show()
                        } catch (e: IOException) {
                            Snackbar.make(
                                view,
                                Objects.requireNonNull<String>(e.message),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )
                .listener(
                    onError = { _, e ->
                        Snackbar.make(
                            view,
                            Objects.requireNonNull<String>(e.message),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                )
                .build()
        )

    }

    @SuppressLint("MissingPermission")
    private fun downloadPic(tujianPic: TujianPic, view: View, uri: Uri) {
        Coil.enqueue(
            ImageRequest.Builder(this)
                .data(Uri.parse(tujianPic.getLinkHD()))
                .target(
                    onSuccess = {
                        val wallpaperManager = WallpaperManager.getInstance(this@DetailActivity)
                        try {
                            val pfd: ParcelFileDescriptor? =
                                contentResolver.openFileDescriptor(uri, "w")
                            val fileOutputStream = FileOutputStream(pfd?.fileDescriptor!!)
                            it.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                            fileOutputStream.close()
                            pfd.close()
                            Snackbar.make(view, R.string.action_download_finish, Snackbar.LENGTH_LONG)
                                .show()
                        } catch (e: IOException) {
                            Snackbar.make(
                                view,
                                Objects.requireNonNull<String>(e.message),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                )
                .listener(
                    onError = { _, e ->
                        Snackbar.make(
                            view,
                            Objects.requireNonNull<String>(e.message),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                )
                .build()
        )

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        super.onConfigurationChanged(newConfig)
    }
}



