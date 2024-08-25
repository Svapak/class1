package com.example.sih

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DamageActivity : AppCompatActivity() {

    private val BASE_URL = "https://detect.roboflow.com"
    private val TAG:String = "CHECK_RESPONSE"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_damage)
        getAllDamage()
    }

    private fun getAllDamage(){
        val api =Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
        api.getDamage().enqueue(object : Callback<List<apidataclass>> {
            override fun onResponse(
                call: Call<List<apidataclass>>,
                response: Response<List<apidataclass>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        for(damage in it){
                            Log.i(TAG,"onResponse: ${damage.dull}")
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<apidataclass>>, t: Throwable) {
                Log.i(TAG,"onFailure: ${t.message}")
            }

        })
    }
}