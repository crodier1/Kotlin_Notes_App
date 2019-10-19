package com.christianrodier.kotlinnotesapp

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {

    var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        var bundle: Bundle? = intent.extras


        try{

            id = bundle!!.getInt(DataBase.ID, 0)

            if (id != 0){
                etTitle.setText(bundle.getString(DataBase.Title))
                etContent.setText(bundle.getString(DataBase.Content))
            }

        } catch (ex: Exception){}



    }

    fun btnAdd(view: View){
        var dbManager = DbManager(this)

        var values = ContentValues()
        values.put(DataBase.Title, etTitle.text.toString())
        values.put(DataBase.Content, etContent.text.toString())


        if (id == 0){

            dbManager.insert(values)

            Toast.makeText(this, "Note added", Toast.LENGTH_LONG).show()

        } else {

            var selectionArgs = arrayOf(id.toString())

            dbManager.update(values, "ID=?", selectionArgs)

            Toast.makeText(this, "Note updated", Toast.LENGTH_LONG).show()
        }

        finish()
    }
}
