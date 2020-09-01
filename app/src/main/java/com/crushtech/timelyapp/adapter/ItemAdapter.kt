package com.crushtech.quicky_alarmapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.crushtech.timelyapp.R
import com.crushtech.timelyapp.data.CustomBottomItem

import java.util.ArrayList

class ItemAdapter(
    defaultOpenIndex: Int,
    private val items: ArrayList<CustomBottomItem>,
    private val itemSelectorInterface: ItemSelectorInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MAX_TITLE_LENGTH = 8
    private var defaultBackground: String? = null
    private var defaultTint: String? = null

    init {
        setDefaultOpen(defaultOpenIndex)
        itemSelectorInterface.itemSelect(defaultOpenIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        setIcon((holder as ItemViewHolder).item_icon, items[position].itemIconId)
        setTitle(holder.item_title, items[position].itemTitle!!)
        setSelectedItemStyle(
            holder.item_parent,
            holder.item_title,
            holder.item_icon,
            items[position].isOpen,
            items[position].itemBackgroundColor!!,
            items[position].itemTintColor!!
        )
        setOnClickItem(holder.click_parent, position, items[position].itemId)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun setIcon(imageView: ImageView, iconId: Int) {
        imageView.setImageResource(iconId)
    }

    @SuppressLint("SetTextI18n")
    private fun setTitle(textView: TextView, text: String) {
        if (text.length > MAX_TITLE_LENGTH) {
            textView.text = text.substring(0, MAX_TITLE_LENGTH) + "…"
        } else {
            textView.text = text
        }
    }

    @SuppressLint("ResourceType")
    private fun setSelectedItemStyle(
        parent: CardView,
        title: TextView,
        icon: ImageView,
        isOpen: Boolean,
        parentColor: String,
        tintColor: String
    ) {
        if (isOpen) {
            title.visibility = View.VISIBLE
            parent.setCardBackgroundColor(Color.parseColor(parentColor))
            title.setTextColor(Color.parseColor(tintColor))
            icon.setColorFilter(
                Color.parseColor(tintColor),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        } else {
            title.visibility = View.GONE
            parent.setCardBackgroundColor(Color.parseColor(defaultBackground))
            title.setTextColor(Color.parseColor(defaultTint))
            icon.setColorFilter(
                Color.parseColor(defaultTint),
                android.graphics.PorterDuff.Mode.SRC_IN
            )
        }
    }

    private fun setOnClickItem(parent: RelativeLayout, position: Int, itemID: Int) {
        parent.setOnClickListener {
            //It closes all items first, and then opens the selected item.
            changeCloseData()
            items[position].isOpen = true
            notifyDataSetChanged()
            itemSelectorInterface.itemSelect(itemID)
        }
    }

    //When you select a new item, you close all of them. This way, only one item will always be open.
    private fun changeCloseData() {
        for (i in items.indices) {
            items[i].isOpen = false
        }
    }

    //Sets the default open item.
    private fun setDefaultOpen(index: Int) {
        if (index > -1 && index <= items.size - 1) {
            items[index].isOpen = true
        } else {
            if (items.isNotEmpty()) {
                items[0].isOpen = true
            }
        }
    }
    private fun getDefaultBackground(): String = defaultBackground.toString()

    fun setDefaultBackground(defaultBackground: String) {
        this.defaultBackground = defaultBackground
    }

    private fun getDefaultTint(): String =defaultTint.toString()

    fun setDefaultTint(defaultTint: String) {
        this.defaultTint = defaultTint
    }

    interface ItemSelectorInterface {
        fun itemSelect(selectedID: Int)
    }
}
