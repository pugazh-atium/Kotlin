package com.example.kotlinapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MuAdapter(private val context : Activity , private val arrayList: ArrayList<Articles>) :ArrayAdapter<Articles>(context , R.layout.list_item , arrayList) {
 @SuppressLint("ViewHolder")
 override fun getView(position :Int, convertView :View?, parent: ViewGroup) : View {
  val inflater : LayoutInflater = LayoutInflater.from(context)
  val  view : View = inflater.inflate(R.layout.list_item , null)
  val title : TextView = view.findViewById(R.id.title)
  val description :TextView = view.findViewById(R.id.description)
  val author : TextView = view.findViewById(R.id.author)

   title.text = arrayList[position].title
   description.text = arrayList[position].description
   author.text = arrayList[position].author

  return view
 }
}