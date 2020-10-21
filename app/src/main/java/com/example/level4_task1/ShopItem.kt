package com.example.level4_task1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shoppingTable")
data class ShopItem (

    @ColumnInfo(name = "shopItem")
    var productName: String,

    @ColumnInfo(name = "Quantity")
    var productQuantity: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
)