package com.hidden.artify.extensions

import android.util.Log
import com.hidden.artify.BuildConfig
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

fun OkHttpClient.makePostRequest(
    requestBody: RequestBody,
    callback: (JSONArray) -> Unit
) {

    //todo try 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
    var jsonArray: JSONArray
    val apiKey = "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"
    val request = Request.Builder()
        .url(BuildConfig.BASE_URL_GENERATION)
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer $apiKey")
        .post(requestBody)
        .build()

    this.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("salimmm", "ON FAILURE ${e.localizedMessage}")
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            val jsonObject = JSONObject(body.toString())
            Log.e("salimmm", "jsonObject === $jsonObject")
            return try {
                jsonArray = jsonObject.getJSONArray("data")
                Log.e("salimmm", "jsonArray === $jsonObject")
                callback(jsonArray)
            } catch (e: Exception) {
                //callback("jsonArray")
            }
        }
    })
}