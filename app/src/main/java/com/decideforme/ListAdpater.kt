package com.decideforme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_option.view.*

class ListAdpater(private val listOfOptions: ArrayList<String>) : RecyclerView.Adapter<ListAdpater.ListViewHolder>(){
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.tvOptionTitle
        val btnDelete: ImageButton = itemView.btnDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_option, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val option = listOfOptions[position]
        holder.txtTitle.text = option
        holder.btnDelete.setOnClickListener {
            //delete from list and adapter
            listOfOptions.remove(option)
            if(position == itemCount - 1){
                notifyDataSetChanged()
            }
            else{
                notifyItemRemoved(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfOptions.size
    }
}