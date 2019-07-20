package github.gggxbbb.tujian_dev

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import github.gggxbbb.tujian_dev.java.PicsAdapter
import github.gggxbbb.tujian_dev.tools.Http
import github.gggxbbb.tujian_dev.tools.TujianPic
import github.gggxbbb.tujian_dev.tools.tujianToady
import kotlinx.android.synthetic.main.activity_archive.*
import kotlinx.android.synthetic.main.content_archive.*
import org.json.JSONObject

class ArchiveActivity : AppCompatActivity() {

    private lateinit var recManage: StaggeredGridLayoutManager
    private lateinit var adapter: PicsAdapter
    private var datas: java.util.ArrayList<TujianPic> = java.util.ArrayList()
    private var page = 1
    private val size = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_archive)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        val tujianPic = TujianPic(JSONObject(intent.getStringExtra("pic")))
        val tID = tujianPic.getTID()
        val tNAME = tujianPic.getTNAME(this)

        title = tNAME

        recManage = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        main_pics.layoutManager = recManage

        loadPics(page, tID)

        class OnScroll : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (page != 0) {
                    if (!recyclerView.canScrollVertically(1)) {
                        page += 1
                        loadPics(page, tID)
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
        Snackbar.make(fab, R.string.action_loading, Snackbar.LENGTH_SHORT).show()

        Http.get("https://api.dpic.dev/list/?page=$page_get&size=$size&sort=$tID",
            { _, response ->
                runOnUiThread {
                    val re = JSONObject(response.body()!!.string())
                    val data: String = re.getJSONArray("result").toString()
                    for ((_, v) in tujianToady(data)) run {
                        adapter.addItem(v)
                    }
                    if (page_get >= re.getInt("maxpage")) {
                        page = 0
                    }
                    onLoading.visibility = View.GONE
                }
            },
            { _, ioException ->
                runOnUiThread {
                    page -= 1
                    Snackbar.make(fab, "${ioException.message}", Snackbar.LENGTH_LONG).show()
                }
            })

    }

}
