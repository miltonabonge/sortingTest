package com.naveen.sampleapp.ui.sort

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naveen.sampleapp.R
import com.naveen.sampleapp.model.Item

class SortItemAdapter(
    var itemsList: List<Item>
) : RecyclerView.Adapter<SortItemAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        val name = item.name
        holder.nameText.text = "Name: $name"

        holder.itemIdText.text = "Item id: ${item.listId}"
    }

    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
        val itemIdText: TextView = view.findViewById(R.id.itemIdText)
    }

}

