package github.gggxbbb.tujian_dev

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import github.gggxbbb.tujian_dev.tools.Http
import github.gggxbbb.tujian_dev.tools.TujianSort
import github.gggxbbb.tujian_dev.tools.isPad
import kotlinx.android.synthetic.main.activity_upload.*
import kotlinx.android.synthetic.main.content_upload.*
import org.json.JSONObject
import java.io.File


class UploadActivity : AppCompatActivity() {

    private val code = 51

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = if (isPad(this)) ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_upload)
        setSupportActionBar(toolbar)

        pic_upload_pic.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, code)
        }

        pic_sub.setOnClickListener {

            var canDo = true
            if (pic_link.text.toString() == "") {
                pic_link.error = getString(R.string.action_input_null)
                canDo = false
            }
            if (pic_title.text.toString() == "") {
                pic_title.error = getString(R.string.action_input_null)
                canDo = false
            }
            if (pic_content.text.toString() == "") {
                pic_content.error = getString(R.string.action_input_null)
                canDo = false
            }
            if (pic_username.text.toString() == "") {
                pic_username.error = getString(R.string.action_input_null)
                canDo = false
            }
            if (pic_mailbox.text.toString() == "") {
                pic_mailbox.error = getString(R.string.action_input_null)
                canDo = false
            }

            if (canDo) {
                Http.postRequestBody(
                    "https://v2.api.dailypics.cn/tg", hashMapOf(
                        "title" to pic_title.text.toString(),
                        "content" to pic_content.text.toString(),
                        "url" to pic_link.text.toString(),
                        "user" to pic_username.text.toString(),
                        "hz" to pic_mailbox.text.toString(),
                        "sort" to getTID()
                    ),
                    { _, response ->
                        Log.d("upload", response.body()!!.string())
                        val data = JSONObject(response.body()!!.string())
                        if (data.getInt("code") == 200) {
                            runOnUiThread {
                                Snackbar.make(upload_root, R.string.action_sub_finish, Snackbar.LENGTH_LONG).show()
                            }
                        }else{
                            runOnUiThread {
                                Snackbar.make(upload_root, data.getString("msg"), Snackbar.LENGTH_LONG).show()
                            }
                        }
                    },
                    { _, ioException ->
                        Snackbar.make(upload_root, ioException.message!!, Snackbar.LENGTH_LONG).show()
                    })
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == code && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                uploadPic(data.data!!)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getTID(): String {
        return when (pic_sort.checkedRadioButtonId) {
            pic_sort_acg.id -> TujianSort.acg
            pic_sort_photo.id -> TujianSort.photo
            pic_sort_wallpaper.id -> TujianSort.wallpaper
            else -> ""
        }
    }

    @SuppressLint("SetTextI18n")
    @Suppress("MoveLambdaOutsideParentheses")
    private fun uploadPic(i: Uri) {

        Snackbar.make(upload_root, R.string.action_upload_start, Snackbar.LENGTH_LONG).show()

        val inputStream = contentResolver.openInputStream(i)!!
        val parcelFileDescriptor = contentResolver.openFileDescriptor(i, "r")
        val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)

        Glide.with(this).load(image).into(pic)

        parcelFileDescriptor.close()

        val file = File.createTempFile("upload", ".tmp")
        val outputStream = file.outputStream()

        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        inputStream.close()
        outputStream.close()

        Http.upload("https://img.dpic.dev/upload", file,
            { _, response ->
                runOnUiThread {
                    file.delete()
                    val data = JSONObject(response.body()!!.string())
                    if (data.getBoolean("ret")) {
                        Snackbar.make(upload_root, R.string.action_upload_finish, Snackbar.LENGTH_LONG).show()
                        pic_link.setText("https://img.dpic.dev/${data.getJSONObject("info").getString("md5")}")
                    } else {
                        Snackbar.make(
                            upload_root,
                            "${getString(R.string.action_upload_failed)}:${data.getJSONObject("error").getString("message")}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }, { _, ioException ->
                Snackbar.make(upload_root, ioException.message!!, Snackbar.LENGTH_LONG).show()
            })

    }

}
