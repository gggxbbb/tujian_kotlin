package github.gggxbbb.tujian_dev.tools

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import github.gggxbbb.tujian_dev.R
import org.json.JSONArray

fun checkUpdata(context: Context) {
    val manager = context.packageManager
    var versionName = ""
    try {
        val info = manager.getPackageInfo(context.packageName, 0)
        versionName = "v" + info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    Http.get("https://api.github.com/repos/gggxbbb/tujian_kotlin/releases",
        { _, response ->
            val data = JSONArray(response.body()!!.string()).getJSONObject(0)
            if (versionName != data.getString("tag_name") && !data.getBoolean("draft")) run {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(data.getString("name"))
            builder.setMessage(data.getString("body"))
            builder.setPositiveButton(R.string.title_go_update) { _, _ ->
                val uri = Uri.parse(data.getString("html_url"))
                val i = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(i)
            }
            builder.setNegativeButton(R.string.title_cancel,null)
            Looper.prepare()
            builder.show()
            Looper.loop()
            }
        })
}