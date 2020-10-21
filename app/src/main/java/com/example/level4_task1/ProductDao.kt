package com.example.level4_task1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM shoppingTable")
    suspend fun getAllProducts(): List<ShopItem>

    @Insert
    suspend fun insertProduct(shopItem: ShopItem)

    @Delete
    suspend fun deleteProduct(shopItem: ShopItem)

    @Query("DELETE FROM shoppingTable")
    suspend fun deleteAllProducts()
}
