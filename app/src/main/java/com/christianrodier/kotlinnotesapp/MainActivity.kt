package com.christianrodier.kotlinnotesapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {

    private var notesArray = arrayListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadData("%")
    }

    override fun onResume() {
        super.onResume()
        loadData("%")
    }



    private fun loadData(title: String) {

        val projections = arrayOf("ID", "Title", "Content")


        var dbManager = DbManager(this)

        val selectionArgs = arrayOf(title)

        val cursor = dbManager.query(projections, "Title like ?", selectionArgs, "Title")

        notesArray.clear()

        if (cursor.moveToFirst()){

            do {

                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Content = cursor.getString(cursor.getColumnIndex("Content"))

                notesArray.add(Note(ID, Title, Content))

            }while (cursor.moveToNext())
        }


//        notesArray.add(Note(1, "Test Title 1",  "Blah blah blah blah"))
//        notesArray.add(Note(2, "Test Title 2",  "yak yak yak yak"))
//        notesArray.add(Note(3, "Test Title 3",  "yada yada yada yada"))

        var myNoteAdapter = MyNotesAdapter(this, notesArray)
        lvNotes.adapter = myNoteAdapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        var searchView = menu!!.findItem(R.id.siNote).actionView as SearchView

        val searchMenu = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchMenu.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

               loadData("%$query%")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        searchView.setOnCloseListener {
            loadData("%")
            false
        }



        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.AddNote->{

                var intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)

    }

    inner class MyNotesAdapter: BaseAdapter{
        var context: Context? = null
        var noteArray = ArrayList<Note>()

        constructor(context: Context, noteArray: ArrayList<Note>): super(){
            this.noteArray = noteArray
            this.context = context

        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket, null)
            var myNote = noteArray[position]

            myView.tvTitle.text = myNote.title
            myView.tvContent.text = myNote.content

            myView.ivDelete.setOnClickListener {
                var dbManager = DbManager(context!!)
                val selectionArgs = arrayOf(myNote.id.toString())
                dbManager.delete("ID=?", selectionArgs)

                loadData("%")

            }

            myView.ivEdit.setOnClickListener{
                goToUpdate(myNote)
            }

            return myView
        }

        override fun getItem(position: Int): Any {
            return noteArray[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return noteArray.size

        }

    }

    fun goToUpdate(note:Note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID", note.id)
        intent.putExtra("Title", note.title)
        intent.putExtra("Content", note.content)

        startActivity(intent)
    }
}


