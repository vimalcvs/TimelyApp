package com.crushtech.timelyapp.data

import android.content.Context
import android.graphics.Color
import android.view.View

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.crushtech.quicky_alarmapp.adapter.ItemAdapter
import com.crushtech.timelyapp.R


import java.util.ArrayList

class CustomBottomBar(
    private val context: Context,
    view: View,
    private val itemSelectorInterface: ItemAdapter.ItemSelectorInterface
) {
    private val ITEM_LIMIT = 5
    private var custom_bottom_bar_parent: CardView? = null
    private var custom_recycler_view: RecyclerView? = null
    private var custom_divider: View? = null
    private var items: ArrayList<CustomBottomItem>? = null
    var defaultBackground = "#FFFFFF"

    var defaultTint = "#000000"

    init {
        setType(view)
    }

    private fun setType(view: View) {
        custom_bottom_bar_parent = view.findViewById(R.id.custom_bottom_bar_parent)
        custom_recycler_view = view.findViewById(R.id.custom_recycler_view)
        custom_divider = view.findViewById(R.id.custom_divider)
        items = ArrayList()
    }

    //Add menu items
    fun addItem(item: CustomBottomItem) {
        if (items!!.size <= ITEM_LIMIT - 1) {
            items!!.add(item)
        }
    }

    //Change methods
    fun changeBackground(color: String) {
        custom_bottom_bar_parent!!.setCardBackgroundColor(Color.parseColor(color))
    }

    fun changeDividerColor(color: String) {
        custom_divider!!.setBackgroundColor(Color.parseColor(color))
    }

    fun hideDivider() {
        custom_divider!!.visibility = View.GONE
    }

    //Add Adapter
    private fun setAdapter(defaultOpenIndex: Int) {
        val simpleAdapter = ItemAdapter(defaultOpenIndex, items!!, itemSelectorInterface)
        simpleAdapter.setDefaultBackground(defaultBackground)
        simpleAdapter.setDefaultTint(defaultTint)
        custom_recycler_view!!.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        custom_recycler_view!!.adapter = simpleAdapter
    }

    //Apply
    fun apply(defaultOpenIndex: Int) {
        setAdapter(defaultOpenIndex)
    }
}
