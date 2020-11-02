package com.usfoouad.mypaper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.notelayout.view.*
import layout.Notes

class NoteAdaptor(context: Context, noteList: ArrayList<Notes>) : ArrayAdapter<Notes>(context, 0, noteList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {


        var noteItem = LayoutInflater.from(context).inflate(R.layout.notelayout, parent, false)

        var note: Notes = getItem(position)
        noteItem.noteTitle.text = note.title
        noteItem.noteTime.text = note.time

        return noteItem
    }


}





