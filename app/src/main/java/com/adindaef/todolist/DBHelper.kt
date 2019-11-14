package com.adindaef.todolist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DBHelper(context: Context): ManagedSQLiteOpenHelper(context,"todo.db", null,1) {
    companion object{
        private var instance:DBHelper? = null
        @Synchronized
        fun getInstance(context: Context):DBHelper{
            if (instance == null){
                instance = DBHelper(context.applicationContext)
            }
            return instance as DBHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(Model.TABLE_NAME, true,
            Model.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            Model.NAMA to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(Model.TABLE_NAME, true)
    }
}

val Context.database : DBHelper
get() = DBHelper.getInstance(applicationContext)