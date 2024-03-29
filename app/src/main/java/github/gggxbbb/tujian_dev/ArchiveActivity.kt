package github.gggxbbb.tujian_dev

import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import github.gggxbbb.tujian_dev.tools.PicsAdapter
import github.gggxbbb.tujian_dev.tools.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import kotlin.properties.Delegates

class ArchiveActivity : AppCompatActivity() {

    private lateinit var recManage: GridLayoutManager
    private lateinit var adapter: PicsAdapter
    private var datas: java.util.ArrayList<TujianPic> = java.util.ArrayList()
    private var page = 1
    private val size = 15
    private var canLoad = true
    private var columns = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //fab.setOnClickListener {
        //startActivity(Intent(this, UploadActivity::class.java))
        //}
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val tujianPic = TujianPic(JSONObject(intent.getStringExtra("pic")))
        val tID = tujianPic.getTID()
        val tNAME = tujianPic.getTNAME()

        title = tNAME

        columns = getColumns(this)

        recManage =
            GridLayoutManager(this, columns, RecyclerView.VERTICAL, false)
        main_pics.layoutManager = recManage

        loadPics(page, tID)

        class OnScroll : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (page != 0) {
                    if (!recyclerView.canScrollVertically(1)) {
                        if (canLoad) {
                            page += 1
                            loadPics(page, tID)
                            canLoad = false
                        } else {
                            Snackbar.make(main_pics, R.string.action_wait, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        main_pics.addOnScrollListener(OnScroll())

        adapter = PicsAdapter(datas, this)

        main_pics.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        //return super.onOptionsItemSelected(item)
        return true
    }

    fun loadPics(page_get: Int, tID: String) {
        Log.d("loadPics", "page: $page_get")

        Snackbar.make(main_pics, R.string.action_loading, Snackbar.LENGTH_SHORT).show()

        Http.get("https://v2.api.dailypics.cn/list/?page=$page_get&size=$size&sort=$tID",
            { _, response ->
                runOnUiThread {
                    val re = JSONObject(response.body!!.string())
                    val data: String = re.getJSONArray("result").toString()
                    for ((_, v) in tujianToady(data)) adapter.addItem(v)
                    if (page_get >= re.getInt("maxpage")) {
                        page = 0
                    }
                    onLoading.visibility = View.GONE
                    canLoad = true
                }
            },
            { _, ioException ->
                runOnUiThread {
                    page -= 1
                    Snackbar.make(main_pics, "${ioException.message}", Snackbar.LENGTH_LONG).show()
                    canLoad = true
                }
            })

    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        recManage.spanCount = getColumns(this)

        super.onConfigurationChanged(newConfig)
    }

}
