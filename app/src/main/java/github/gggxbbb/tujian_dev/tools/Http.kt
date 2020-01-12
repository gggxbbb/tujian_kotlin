package github.gggxbbb.tujian_dev.tools

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class Http {
    companion object {
        private val okHttpClient = OkHttpClient()
        fun get(url: String, success: (Call, Response) -> Unit, error: (Call, IOException) -> Unit = { _, _ ->}) {
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
            val mediaType = ContentType.toMediaTypeOrNull()
            val request = Request.Builder()
                .url(url)
                .header("Content-Type",ContentType)
                .post(file.asRequestBody(mediaType))
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
            val json = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = JSONObject(data).toString().toRequestBody(json)
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