package com.example.myapplication

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.adapter_iem_layout.view.*

class CustomAdapter(internal var context: Context, internal var itemList:ArrayList<CardViewItem>): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType:Int): ViewHolder{
        var itemView = LayoutInflater.from(parent.context).inflate(R.layout.adapter_iem_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.itemView
        val childView = cardView.childView
        val item = itemList[position]

        cardView.date.setText(item.date)
        cardView.period.setText(item.period)
        cardView.toDo.setText(item.toDo)

        //카드뷰 클릭하면 수정,삭제 버튼 나타나게 하기
        cardView.setOnClickListener{
            if(childView.visibility == View.GONE) {
                //TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
                childView.visibility = View.VISIBLE
            } else {
                //TransitionManager.beginDelayedTransition(cardView, AutoTransition())
                childView.visibility = View.GONE
            }
        }

        //수정 버튼을 클릭하면 일정 수정
        cardView.modifyBtn.setOnClickListener {

        }

        //삭제 버튼을 클릭하면 일정 삭제
        cardView.deleteBtn.setOnClickListener {
            removeItem(position);
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    //일정 제거
    fun removeItem(position: Int) {
        itemList.removeAt(position)
        notifyDataSetChanged()
    }

    //일정 추가
    fun addItem(item: CardViewItem){
        itemList.add(item)
        notifyItemInserted(itemList.lastIndex)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init{
            var date = itemView.findViewById<TextView>(R.id.date)
            var period = itemView.findViewById<TextView>(R.id.period)
            var toDo = itemView.findViewById<TextView>(R.id.toDo)
            /*itemView.setOnClickListener {
                list[adapterPosition].selected = !list[adapterPosition].selected
            }*/
        }
    }
}