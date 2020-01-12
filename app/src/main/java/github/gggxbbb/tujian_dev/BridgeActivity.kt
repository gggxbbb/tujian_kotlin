package github.gggxbbb.tujian_dev

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import github.gggxbbb.tujian_dev.tools.Http
import kotlinx.android.synthetic.main.activity_bridge.*
import org.json.JSONObject

class BridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bridge)

        val iData = intent.data
        if (iData?.scheme.equals("tujian")) {
            when (iData?.host) {
                "p" -> {
                    Http.get("https://v2.api.dailypics.cn/member?id=${iData.path?.replace("/", "")}",
                        { _, response ->
                            val data: String = response.body!!.string()
                            val pic = JSONObject(data)
                            if (pic.has("error_code")) {
                                info_text.setText(R.string.action_found_failes)
                                runOnUiThread {
                                    onLoading.visibility = View.GONE
                                }
                            } else {
                                val intent = Intent(this, DetailActivity::class.java)
                                intent.putExtra("pic", data)
                                runOnUiThread {
                                    this.startActivity(intent)
                                    this.finish()
                                }
                            }
                        },
                        { _, ioException ->
                            runOnUiThread {
                                info_text.text = ioException.message
                                onLoading.visibility = View.GONE
                            }
                        })
                }
            }
        }

    }
}
