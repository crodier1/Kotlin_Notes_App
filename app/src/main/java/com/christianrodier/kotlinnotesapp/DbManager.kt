package com.christianrodier.kotlinnotesapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.widget.Toast

class DbManager {

    companion object{
        const val dbName = "MyNotes"
        const val dbTable = "Notes"
        const val colID = "ID"
        const val colTitle = "Title"
        const val colContent = "Content"
        const val dbVersion = 1
    }


    //CREATE TABLE IF NOT EXISTS MyNotes (ID INTEGER PRIMARY KEY, title TEXT, Content TEST);"
    val sqlCreateTable = "CREATE TABLE IF NOT EXISTS " + dbTable + " (" + colID + " INTEGER PRIMARY KEY," +
            colTitle + " TEXT, " + colContent + " TEST);"

    var sqlDb:SQLiteDatabase? = null

    constructor(context: Context){
        val db = DatabaseHelperNotes(context)

        sqlDb = db.writableDatabase
    }

   inner class DatabaseHelperNotes:SQLiteOpenHelper{

       var context: Context? = null

        constructor(context: Context):super(context, dbName, null, dbVersion){
            this.context = context
        }

        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCreateTable)

            Toast.makeText(this.context, "DB Created", Toast.LENGTH_LONG).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

            db!!.execSQL("DROP TABle IF EXISTS " + dbTable)

        }

    }

    fun insert(values:ContentValues):Long{

        return sqlDb!!.insert(dbTable,"",values)

    }

    fun query(projection: Array<String>, selection:String, selectionArgs: Array<String>, sortOrder:String):Cursor{
        val queryBuilder = SQLiteQueryBuilder()

        queryBuilder.tables = dbTable

        return queryBuilder.query(sqlDb, projection, selection, selectionArgs,null, null, sortOrder)
    }

    fun delete(selection: String, selectionArgs: Array<String>):Int{
        return sqlDb!!.delete(dbTable, selection, selectionArgs)
    }

    fun update(values:ContentValues, selection:String, selectionArgs: Array<String>):Int{
        return sqlDb!!.update(dbTable, values, selection,selectionArgs)
    }


}