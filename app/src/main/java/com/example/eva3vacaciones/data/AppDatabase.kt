package com.example.eva3vacaciones.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LugarVisita::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lugarVisitaDao() : LugarVisitaDao

    //Singleton
    companion object {
        //Volatile asegura que sea actulizada la propiedad atómicamente
        @Volatile
        private var BASE_DATOS : AppDatabase? = null

        fun getInstance(contexto: Context) : AppDatabase{
            //Synchronized previene el acceso de multiples threads de manera simnultanea
            return BASE_DATOS ?: synchronized(this){
                Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    "LugaresVisitaDB.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { BASE_DATOS = it }
            }
        }
    }

}