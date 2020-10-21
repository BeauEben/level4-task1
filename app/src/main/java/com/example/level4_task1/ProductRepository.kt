package com.example.level4_task1

import android.content.Context

class ProductRepository(context: Context) {

    private val productDao: ProductDao

    init {
        val database = ShoppingListRoomDatabase.getDatabase(context)
        productDao = database!!.productDao()
    }

    suspend fun getAllProducts(): List<ShopItem> {
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(shopItem: ShopItem) {
        productDao.insertProduct(shopItem)
    }

    suspend fun deleteProduct(shopItem: ShopItem) {
        productDao.deleteProduct(shopItem)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }

}
