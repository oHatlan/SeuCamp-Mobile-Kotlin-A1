package com.example.seucamp.data.api

import com.example.seucamp.model.ViaCepResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {

    @GET("ws/{cep}/json/")
    suspend fun buscarCep(
        @Path("cep") cep: String
    ): ViaCepResponse
}