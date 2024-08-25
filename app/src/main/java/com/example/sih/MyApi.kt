package com.example.sih

import retrofit2.Call
import retrofit2.http.GET

interface MyApi {
    @GET("/")
    fun getDamage(): Call<List<apidataclass>>
}