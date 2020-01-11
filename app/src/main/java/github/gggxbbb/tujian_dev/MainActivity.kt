package github.gggxbbb.tujian_dev

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import github.gggxbbb.tujian_dev.tools.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recManage: StaggeredGridLayoutManager
    private lateinit var adapter: PicsAdapter
    private var datas: java.util.ArrayList<TujianPic> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation =
            if (isPad(this)) ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        checkForUpdate(this)

        //fab.setOnClickListener {
            //startActivity(Intent(this, UploadActivity::class.java))
        //}

        recManage = StaggeredGridLayoutManager(getColumns(this), StaggeredGridLayoutManager.VERTICAL)
        main_pics.layoutManager = recManage

        onLoading.visibility = View.VISIBLE
        Snackbar.make(fab, R.string.action_loading, Snackbar.LENGTH_SHORT).show()

        val sp = getSharedPreferences("catch", Context.MODE_PRIVATE)

        Http.get("https://v2.api.dailypics.cn/today",
            { _, response ->
                val data: String = response.body()!!.string()
                loadToday(data)
                val editor = sp.edit()
                editor.putString("today", data)
                editor.apply()
            },
            { _, ioException ->
                runOnUiThread {
                    val data = sp.getString("today", null)
                    if (data == null) Snackbar.make(main_pics, "${ioException.message}", Snackbar.LENGTH_LONG).show()
                    else loadToday(data)
                }
            })

    }

    private fun loadToday(data: String) {
        for ((_, v) in tujianToady(data)) datas.add(v)
        adapter = PicsAdapter(datas, this)
        runOnUiThread {
            main_pics.adapter = adapter
            onLoading.visibility = View.GONE
        }
    }

}
