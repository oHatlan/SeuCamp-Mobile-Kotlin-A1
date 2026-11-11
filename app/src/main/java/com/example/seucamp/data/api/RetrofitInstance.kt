package com.example.seucamp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: ViaCepService by lazy {
        Retrofit.Builder()
            .baseUrl("https://viacep.com.br/")
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ViaCepService::class.java)
    }
}