package github.gggxbbb.tujian_dev

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import github.gggxbbb.tujian_dev.java.PicsAdapter
import github.gggxbbb.tujian_dev.tools.Http
import github.gggxbbb.tujian_dev.tools.TujianPic
import github.gggxbbb.tujian_dev.tools.tujianToady
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var recManage: StaggeredGridLayoutManager
    private lateinit var adapter: PicsAdapter
    private var datas: java.util.ArrayList<TujianPic> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }

        recManage = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        main_pics.layoutManager = recManage

        onLoading.visibility = View.VISIBLE
        Snackbar.make(fab, R.string.action_loading, Snackbar.LENGTH_SHORT).show()

        Http.get("https://api.dpic.dev/today",
            { _, response ->
                val data: String = response.body()!!.string()
                for ((_, v) in tujianToady(data)) run {
                    datas.add(v)
                }
                adapter = PicsAdapter(datas, this)
                runOnUiThread {
                    main_pics.adapter = adapter
                    onLoading.visibility = View.GONE
                }
            },
            { _, ioException ->
                runOnUiThread {
                    Snackbar.make(main_pics, "${ioException.message}", Snackbar.LENGTH_LONG).show()
                }

            })

    }
}
