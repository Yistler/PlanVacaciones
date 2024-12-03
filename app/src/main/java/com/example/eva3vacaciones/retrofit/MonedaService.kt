package com.example.eva3vacaciones.retrofit

import retrofit2.http.GET
//https://mindicador.cl/api/
interface MonedaService {
    @GET("/api")
    suspend fun getValorMoneda() : ResultadoBusquedaMoneda
}