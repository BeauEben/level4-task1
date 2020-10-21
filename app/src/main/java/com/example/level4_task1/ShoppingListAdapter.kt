package com.example.level4_task1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_shop.view.*

class ShoppingListAdapter (private val shopItems: List<ShopItem>) : RecyclerView.Adapter<ShoppingListAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun databind(shopItem: ShopItem){
            itemView.tvQuantity.text = shopItem.productQuantity.toString()
            itemView.tvName.text = shopItem.productName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
             LayoutInflater.from(parent.context).inflate(R.layout.item_shop, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return shopItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(shopItems[position])
    }
}