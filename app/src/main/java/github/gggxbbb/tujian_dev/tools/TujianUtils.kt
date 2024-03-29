@file:Suppress("unused")

package github.gggxbbb.tujian_dev.tools

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONArray
import org.json.JSONObject


class TujianSort(val TID: String, val TNAME: String)

val TujianSortMap = HashMap<String, TujianSort>()

class TujianPic(private val dataJson: JSONObject) {

    private val pContent: String =
        Regex("(?!<= {2})\n").replace(dataJson.getString("p_content"), "  \n")

    private val tNAME: String = if (dataJson.has(("T_NAME"))) {
        TujianSortMap[getTID()] = TujianSort(getTID(), dataJson.getString("T_NAME"))
        dataJson.getString("T_NAME")
    } else {
        try {
            TujianSortMap[getTID()]!!.TNAME
        } catch (e: Exception) {
            ""
        }
    }

    private val pLink: String = "https://s2.images.dailypics.cn${dataJson.getString("nativePath")}"

    private val pLinkLite: String = pLink + "!w1080"

    fun getTitle(): String {
        return dataJson.getString("p_title")
    }

    fun getContent(): String {
        return pContent
    }

    fun getUsername(): String {
        return dataJson.getString("username")
    }

    fun getLink(): String {
        Log.d("pic", pLinkLite)
        return pLinkLite
    }

    fun getLinkHD(): String {
        return pLink
    }

    fun getTID(): String {
        return dataJson.getString("TID")
    }

    fun getTNAME(): String {
        return tNAME
    }

    fun getPID(): String {
        return dataJson.getString("PID")
    }

    fun getDate(): String {
        return dataJson.getString("p_date")
    }

    fun getThemeColor(): String {
        return dataJson.getString("theme_color")
    }

    fun getTextColor(): String {
        return dataJson.getString("text_color")
    }

    fun getThemeColorInt(): Int {
        return Color.parseColor(getThemeColor())
    }

    fun getTextColorInt(): Int {
        return Color.parseColor(getTextColor())
    }

    fun getWidth(): Int {
        return dataJson.getInt("width")
    }

    fun getHeight(): Int {
        return dataJson.getInt("height")
    }

    fun getString(): String {
        return dataJson.toString()
    }
}

fun tujianToady(dataJson: String): Map<String, TujianPic> {
    val todayMap = mutableMapOf<String, TujianPic>()
    val todayData = JSONArray(dataJson)
    for (i in 0 until todayData.length()) {
        val dataPic = todayData.getJSONObject(i)
        val pic = TujianPic(dataPic)
        todayMap[pic.getPID()] = pic
    }
    return todayMap
}

fun isPad(context: Context): Boolean {
    return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
}

fun getColumns(activity: Activity): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val display = activity.display
        if (activity.isInMultiWindowMode) {
            return display!!.width / 1500
        }
        val columns = display!!.width * 2 / display.height - 1
        if (columns < 1) 1 else columns
    } else {
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (!isPad(activity)){
                2
            } else {
                1
            }
        } else {
            1
        }
    }
}


fun getLink(pic: TujianPic): String {
    return "https://www.dailypics.cn/member/id/${pic.getPID()}"
}