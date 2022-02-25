package com.example.gameoflife.api

import com.example.gameoflife.roomdb.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApiService {
    @POST("api/user")
    suspend fun addUser(@Body user: User): Response<ResponseBody>
}

