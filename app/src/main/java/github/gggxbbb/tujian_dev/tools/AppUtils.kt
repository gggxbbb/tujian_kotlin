package github.gggxbbb.tujian_dev.tools

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import android.util.Log
import github.gggxbbb.tujian_dev.R
import org.json.JSONArray
import java.util.regex.Pattern

fun checkForUpdate(context: Context) {
    val manager = context.packageManager
    var versionName = ""
    try {
        val info = manager.getPackageInfo(context.packageName, 0)
        versionName = info.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    Http.get("https://api.github.com/repos/gggxbbb/tujian_kotlin/releases",
        { _, response ->
            val data = JSONArray(response.body()!!.string()).getJSONObject(0)
            if (ifNeedUpdate(versionName,data.getString("tag_name")) && !data.getBoolean("draft") && data.getString("name") != "null") run {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder.setTitle(data.getString("name"))
                builder.setMessage(data.getString("body"))
                builder.setPositiveButton(R.string.title_go_update) { _, _ ->
                    val uri = Uri.parse(data.getString("html_url"))
                    val i = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(i)
                }
                builder.setNeutralButton(R.string.action_download) {_,_->
                    val uri = Uri.parse(data.getJSONArray("assets").getJSONObject(0).getString("browser_download_url"))
                    val i = Intent(Intent.ACTION_VIEW, uri)
                    context.startActivity(i)
                }
                builder.setNegativeButton(R.string.title_cancel, null)
                Looper.prepare()
                builder.show()
                Looper.loop()
            }
        })
}

fun ifNeedUpdate(localVersion:String,remoteVersion:String):Boolean{
    return keepDigital(remoteVersion)> keepDigital(localVersion)
}

fun keepDigital(oldString: String): Long {
    val newString = StringBuffer()
    val matcher = Pattern.compile("\\d").matcher(oldString)
    while (matcher.find()) {
        newString.append(matcher.group())
    }
    Log.d("debug",newString.toString())
    return newString.toString().toLong()
}