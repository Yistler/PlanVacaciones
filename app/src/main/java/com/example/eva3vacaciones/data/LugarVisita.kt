package com.example.eva3vacaciones.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LugarVisita(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "nombre_lugar") val nombreLugar: String,
    @ColumnInfo(name = "imagen_ref") val imagenRef: String?,
    @ColumnInfo(name = "latitud") val latitud: Double,
    @ColumnInfo(name = "longitud") val longitud: Double,
    @ColumnInfo(name = "orden_visita") val ordenVisita: Int,
    @ColumnInfo(name = "costoAlojamiento") val costoAlojamiento: Double,
    @ColumnInfo(name = "costroTraslado") val CostoTraslado: Double,
    @ColumnInfo(name = "comentarios") val comentarios: String?
)
