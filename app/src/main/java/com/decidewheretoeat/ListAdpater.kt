package com.decidewheretoeat

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_option.view.*

class ListAdpater(private val listOfOptions: ArrayList<String>, private var editText: EditText) : RecyclerView.Adapter<ListAdpater.ListViewHolder>(){
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
        holder.txtTitle.setOnClickListener {
            editText.setText(holder.txtTitle.text)

            listOfOptions.remove(option)
            if(position == itemCount - 1){
                notifyDataSetChanged()
            }
            else{
                notifyItemRemoved(position)
            }
        }
        holder.btnDelete.setOnClickListener {
            Log.d("delete", "Deleted "+option+" at position: "+position)
            //delete from list and adapter
            listOfOptions.remove(option)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return listOfOptions.size
    }
}