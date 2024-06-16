package com.example.myapplication.db.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.db.entity.HistoryState
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Singleton

@Singleton
class HistoryListAdapter(context: Context, private val items: List<HistoryState>) :
    ArrayAdapter<HistoryState>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.history_list_item_layout, parent, false)
        }

        val item = items[position]

        val historyItemText = itemView!!.findViewById<TextView>(R.id.history_item_text)
        val dateAddedText = itemView.findViewById<TextView>(R.id.date_added_text)

        historyItemText.text = item.item
        dateAddedText.text = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(item.dateAdded)

        return itemView
    }
}