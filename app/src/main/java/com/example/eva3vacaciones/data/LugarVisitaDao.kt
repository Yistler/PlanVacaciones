package com.example.eva3vacaciones.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LugarVisitaDao {
    @Query("SELECT * FROM lugarvisita")
    fun getAll(): List<LugarVisita>

    @Query("SELECT * FROM lugarvisita WHERE id = :id")
    fun obtenerLugarPorId(id: Long): LugarVisita?

    @Insert
    fun agregarLugar(vararg lugarVisita: LugarVisita)

    @Delete
    fun eliminarLugar(lugarVisita: LugarVisita)

    @Update
    fun actulizarLugar(lugarVisita: LugarVisita)
}