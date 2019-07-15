package github.gggxbbb.tujian_dev.tools

import org.json.JSONArray
import org.json.JSONObject

class TujianSort {
    public var PHONE_PHOTO: String = "5398f27b-a9f7-11e8-a8ea-0202761b0892"
    public var PHONE_ACG: String = "4ac1c07f-a9f7-11e8-a8ea-0202761b0892"
    public var COMPUTER_WALLPAPER: String = "e5771003-b4ed-11e8-a8ea-0202761b0892"
}

class TujianPic(val dataJson: JSONObject) {
    public fun getTitle(): String {
        return dataJson.getString("p_title")
    }

    public fun getContent(): String {
        return dataJson.getString("p_content")
    }

    public fun getUsername(): String {
        return dataJson.getString("username")
    }

    public fun getLink(): String {
        return dataJson.getString("local_url")
    }

    public fun getTID(): String {
        return dataJson.getString("TID")
    }

    public fun getTNAME(): String {
        return dataJson.getString("T_NAME")
    }

    public fun getPID(): String {
        return dataJson.getString("PID")
    }

    public fun getDate(): String {
        return dataJson.getString("p_date")
    }

    public fun getThemeColor(): String {
        return dataJson.getString("theme_color")
    }

    public fun getTextColor(): String {
        return dataJson.getString("text_color")
    }

    public fun getWidth(): Int {
        return dataJson.getInt("width")
    }

    public fun getHeight(): Int {
        return dataJson.getInt("height")
    }
}

fun TujianToady(dataJson: String):Map<String,TujianPic> {
    val todayMap = mutableMapOf<String, TujianPic>()
    val todayData = JSONArray(dataJson)
    for (i in 0 until todayData.length()) {
        val dataPic = todayData.getJSONObject(i)
        val pic = TujianPic(dataPic)
        todayMap.put(pic.getTID(),pic)
    }
    return todayMap
}