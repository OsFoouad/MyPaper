package com.usfoouad.mypaper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.adddialog.view.*
import kotlinx.android.synthetic.main.edit_note.view.*
import layout.Notes
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    var myref: DatabaseReference? = null
    var notesArr: ArrayList<Notes>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var database = FirebaseDatabase.getInstance()
        myref = database.getReference("Notes")

        notesArr = ArrayList()

        // home screen button
        showAddDialog.setOnClickListener {

            showAddNewNoteDialog()

        }

        // ListViewClick
        notesListview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            var mynote = notesArr?.get(position)!!

            var intent = Intent(this, MSG::class.java)

            intent.putExtra("title", mynote.title)
            intent.putExtra("time", mynote.time)
            intent.putExtra("msg", mynote.content)
            startActivity(intent)

        }


        // longClick
        notesListview.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->

            val alertBuild = AlertDialog.Builder(this)
            val editView = layoutInflater.inflate(R.layout.edit_note, null)
            var alertDialog = alertBuild.create()
            alertDialog.setView(editView)
            alertDialog.show()


            var myynote = notesArr?.get(position)!!
            editView.EditTitle.setText(myynote.title)
            editView.EditContent.setText(myynote.content)


            editView.Edit.setOnClickListener {

                var title = editView.EditTitle.text.toString()
                var content = editView.EditContent.text.toString()
                var updatedNote = Notes(myynote.id!!, title, content, timeGetter())


                myref!!.child(myynote.id!!).setValue(updatedNote)

                alertDialog.dismiss()
                Toast.makeText(this, " Note Edited ✓", Toast.LENGTH_SHORT).show()


            }


            editView.Delete.setOnClickListener {

                myref!!.child(myynote.id!!).removeValue()
                alertDialog.dismiss()
                Toast.makeText(this, " Note Deleted ✓", Toast.LENGTH_SHORT).show()

            }



            true
        }


    }


    override fun onStart() {
        super.onStart()


        FirebaseGettingData()

    }


    private fun FirebaseGettingData() {
        myref!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                notesArr?.clear()

                for (n in p0.children) {
                    var restorednoted = n.getValue(Notes::class.java)
                    notesArr?.add(0, restorednoted!!)
                }

                var adaptor = NoteAdaptor(applicationContext, notesArr!!)
                notesListview.adapter = adaptor
            }
        })
    }

    private fun showAddNewNoteDialog() {


        var alertBuilder = AlertDialog.Builder(this)
        var newNoteDialog = layoutInflater.inflate(R.layout.adddialog, null)
        alertBuilder.setView(newNoteDialog)
        var alertDialog = alertBuilder.create()
        alertDialog.show()

        newNoteDialog.AddNewNoteBtn.setOnClickListener {

            var title = newNoteDialog.noteTitleEditText.text.toString()
            var content = newNoteDialog.noteContentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {

                var id = myref!!.push().key
                var myNote = Notes(id, title, content, timeGetter())
                myref!!.child(id!!).setValue(myNote)

                alertDialog.dismiss()


                Toast.makeText(this, "Added Successfully ✓", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(this, "Title or Content can't be Empty ✘", Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun timeGetter(): String {
        var calender = Calendar.getInstance()
        val format = SimpleDateFormat("EEE hh : mm a ")
        return format.format(calender.time)

    }

}

