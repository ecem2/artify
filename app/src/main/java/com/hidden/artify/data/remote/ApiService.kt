package com.hidden.artify.data.remote

import com.hidden.artify.data.model.EditResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {

    @GET
    fun downloadFile(@Url fileUrl: String): Call<ResponseBody>

    @POST
    suspend fun editImage(
        @Body body: RequestBody
    ): Response<EditResponse>
}