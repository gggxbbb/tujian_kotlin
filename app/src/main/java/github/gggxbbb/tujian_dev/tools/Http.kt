package github.gggxbbb.tujian_dev.tools

import okhttp3.*
import java.io.File
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.json.JSONObject


class Http {
    companion object {
        fun get(url: String, success: (Call, Response) -> Unit, error: (Call, IOException) -> Unit = {_,_ ->}) {
            val okHttpClient = OkHttpClient()
            val request = Request.Builder()
                .url(url)
                .get()
                .build()
            val call = okHttpClient.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    error(call,e)
                }
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    success(call,response)
                }
            })
        }
        fun upload(url :String,file:File,success: (Call, Response) -> Unit, error: (Call, IOException) -> Unit = {_,_ ->},ContentType:String="image/jpeg"){
            val okHttpClient = OkHttpClient()
            val mediaType = MediaType.parse(ContentType)
            val request = Request.Builder()
                .url(url)
                .header("Content-Type",ContentType)
                .post(RequestBody.create(mediaType, file))
                .build()
            val call = okHttpClient.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    error(call,e)
                }
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    success(call,response)
                }
            })
        }
        fun postRequestBody(url:String, data:MutableMap<Any?,Any?>, success: (Call, Response) -> Unit, error: (Call, IOException) -> Unit = {_,_ ->}){
            val json = MediaType.parse("application/json; charset=utf-8")
            val okHttpClient = OkHttpClient()
            val requestBody = RequestBody.create(json,JSONObject(data).toString())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            val call = okHttpClient.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    error(call,e)
                }
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    success(call,response)
                }
            })
        }
    }
}