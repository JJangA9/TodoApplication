package com.example.myapplication

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var cardView:View
    var period:TextView
    var date:TextView
    var toDo:TextView
    var childView:View
    var modifyBtn: Button
    var deleteBtn: Button

    init {
        cardView = itemView.findViewById(R.id.cardView)
        period = itemView.findViewById(R.id.period) as TextView
        date = itemView.findViewById(R.id.date) as TextView
        toDo = itemView.findViewById(R.id.toDo) as TextView
        childView = itemView.findViewById(R.id.childView) as View
        modifyBtn = itemView.findViewById(R.id.modifyBtn) as Button
        deleteBtn = itemView.findViewById(R.id.deleteBtn) as Button
    }

}