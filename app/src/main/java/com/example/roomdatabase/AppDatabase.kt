package com.example.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Student :: class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun studentDao() : StudentDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getDatabase(context : Context): AppDatabase{
            val temInstance= INSTANCE
            if (temInstance != null){

                return temInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "App_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }



}