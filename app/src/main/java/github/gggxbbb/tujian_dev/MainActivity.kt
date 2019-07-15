package github.gggxbbb.tujian_dev

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.kittinunf.fuel.Fuel
import github.gggxbbb.tujian_dev.java.PicsAdapter
import github.gggxbbb.tujian_dev.tools.TujianPic
import github.gggxbbb.tujian_dev.tools.TujianToady

class MainActivity : AppCompatActivity() {
    private lateinit var list:RecyclerView
    private lateinit var recManage: StaggeredGridLayoutManager
    private lateinit var adapter: PicsAdapter
    private var datas:java.util.ArrayList<TujianPic> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = findViewById(R.id.main_pics)

        recManage = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        list.layoutManager = recManage

        Fuel.get("https://api.dpic.dev/today").responseString { request, response, result ->
            //返回参数
            result.fold({ d ->
                val data:String = result.get()
                for ((k,v) in TujianToady(data)) run{
                    datas.add(v)
                }
                adapter = PicsAdapter(datas,this)
                list.adapter = adapter
            }, { err ->
                print(err.toString())
            })
        }

    }
}
