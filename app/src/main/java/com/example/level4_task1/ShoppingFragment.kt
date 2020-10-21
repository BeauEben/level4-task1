package com.example.level4_task1

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.icu.util.CurrencyAmount
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_shopping.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingFragment : Fragment() {

    private val shopItems = arrayListOf<ShopItem>()
    private val  shoppingListAdapter = ShoppingListAdapter(shopItems)

    private lateinit var productRepository: ProductRepository

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productRepository = ProductRepository(requireContext())

        getShoppingListFromDatabase()

        viewInit()

        fabAddProduct.setOnClickListener{
            showAddProductDialog()
        }

        fabDeleteAll.setOnClickListener{
            removeAllProducts()
        }
    }

    private fun viewInit(){
        rvShoppingList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvShoppingList.adapter = shoppingListAdapter
        rvShoppingList.setHasFixedSize(true)
        rvShoppingList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        createItemTouchHelper().attachToRecyclerView(rvShoppingList)

    }

    private fun getShoppingListFromDatabase(){
        mainScope.launch {
            val shopItems = withContext(Dispatchers.IO){
                productRepository.getAllProducts()
            }
                productRepository.getAllProducts()
                this@ShoppingFragment.shopItems.clear()
                this@ShoppingFragment.shopItems.addAll(shopItems)
                this@ShoppingFragment.shoppingListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("InflateParams")
    private fun showAddProductDialog(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_product_title_dialog))
        val dialogLayout = layoutInflater.inflate(R.layout.add_product_dialog, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.txt_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.txt_amount)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_btn) {_: DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun  addProduct(txtProductName: EditText, txtAmount: EditText){
        if (validateFields(txtProductName, txtAmount)){
            mainScope.launch {
                val product = ShopItem(
                    productName = txtProductName.text.toString(),
                    productQuantity = txtAmount.text.toString().toInt()
                )
                withContext(Dispatchers.IO){
                    productRepository.insertProduct(product)
                }
                getShoppingListFromDatabase()
            }
        }
    }

    private fun validateFields(txtProductName: EditText
                               , txtAmount: EditText
    ): Boolean {
        return if (txtProductName.text.toString().isNotBlank()
            && txtAmount.text.toString().isNotBlank()
        ) {
            true
        } else {
            Toast.makeText(activity, "Please fill in the fields", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val productToDelete = shopItems[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun removeAllProducts(){
        mainScope.launch {
            withContext(Dispatchers.IO){
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()

            
        }
    }
}