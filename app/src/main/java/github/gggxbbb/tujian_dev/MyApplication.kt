@file:Suppress("unused")
package github.gggxbbb.tujian_dev

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate


class MyApplication : Application() {

    override fun onCreate() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        super.onCreate()
    }

}