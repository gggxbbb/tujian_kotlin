package github.gggxbbb.tujian_dev

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.kittinunf.fuel.Fuel
import com.google.android.material.snackbar.Snackbar
import github.gggxbbb.tujian_dev.java.PicsAdapter
import github.gggxbbb.tujian_dev.tools.TujianPic
import github.gggxbbb.tujian_dev.tools.tujianToady
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recManage: StaggeredGridLayoutManager
    private lateinit var adapter: PicsAdapter
    private var datas: java.util.ArrayList<TujianPic> = java.util.ArrayList()
    private val appPermission =
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

        }

        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, appPermission, 0)
        }

        recManage = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        main_pics.layoutManager = recManage

        onLoading.visibility = View.VISIBLE
        Fuel.get("https://api.dpic.dev/today").responseString { _, _, result ->
            result.fold({
                val data: String = result.get()
                for ((_, v) in tujianToady(data)) run {
                    datas.add(v)
                }
                adapter = PicsAdapter(datas, this)
                main_pics.adapter = adapter
                onLoading.visibility = View.GONE
            }, { err ->
                Snackbar.make(findViewById(R.id.main_pics), "${err.message}", Snackbar.LENGTH_LONG).show()
            })
        }

    }
}
