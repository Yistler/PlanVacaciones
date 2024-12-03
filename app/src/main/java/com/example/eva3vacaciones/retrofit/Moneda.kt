package com.example.eva3vacaciones.retrofit

import com.squareup.moshi.Json

data class ResultadoBusquedaMoneda(
    val dolar : Moneda
)

data class Moneda(
    //val tags:List<String>,
    @Json(name = "valor")
    val valorDolar:Double,
    @Json(name = "nombre")
    val nombre:String
)
