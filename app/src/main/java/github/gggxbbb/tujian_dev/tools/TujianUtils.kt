@file:Suppress("unused")

package github.gggxbbb.tujian_dev.tools

import org.json.JSONArray
import org.json.JSONObject

class TujianSort {
    val photo: String = "5398f27b-a9f7-11e8-a8ea-0202761b0892"
    val acg: String = "4ac1c07f-a9f7-11e8-a8ea-0202761b0892"
    val wallpaper: String = "e5771003-b4ed-11e8-a8ea-0202761b0892"
}

class TujianPic(val dataJson: JSONObject) {
    fun getTitle(): String {
        return dataJson.getString("p_title")
    }

    fun getContent(): String {
        return dataJson.getString("p_content")
    }

    fun getUsername(): String {
        return dataJson.getString("username")
    }

    fun getLink(): String {
        return dataJson.getString("local_url")
    }

    fun getTID(): String {
        return dataJson.getString("TID")
    }

    fun getTNAME(): String {
        return dataJson.getString("T_NAME")
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

    fun getWidth(): Int {
        return dataJson.getInt("width")
    }

    fun getHeight(): Int {
        return dataJson.getInt("height")
    }
    fun getString():String{
        return dataJson.toString()
    }
}

fun tujianToady(dataJson: String):Map<String,TujianPic> {
    val todayMap = mutableMapOf<String, TujianPic>()
    val todayData = JSONArray(dataJson)
    for (i in 0 until todayData.length()) {
        val dataPic = todayData.getJSONObject(i)
        val pic = TujianPic(dataPic)
        todayMap.put(pic.getTID(),pic)
    }
    return todayMap
}