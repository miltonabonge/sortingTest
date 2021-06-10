package com.naveen.sampleapp.ui.grouped

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.naveen.sampleapp.R
import com.naveen.sampleapp.model.GroupedItem

class GroupedItemAdapter(
    var itemsList: List<GroupedItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            1 -> {
                val view = inflater.inflate(R.layout.grouped_list_item_title, parent, false)
                MyViewHolderTitle(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.grouped_list_item, parent, false)
                MyViewHolderItem(view)
            }
        }
    }


    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun getItemViewType(position: Int): Int {
        return itemsList[position].groupType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            1 -> {
                val viewHolderTitle = holder as MyViewHolderTitle
                val item = itemsList[position]
                item.listId?.let {
                    viewHolderTitle.nameText.text = "List Id $it"
                }
            }
            else -> {
                val viewHolderItem = holder as MyViewHolderItem
                val item = itemsList[position]
                val name = item.item?.name
                viewHolderItem.nameText.text = "Name: $name"
            }
        }

    }

    class MyViewHolderTitle(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
    }

    class MyViewHolderItem(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.nameText)
    }

}

