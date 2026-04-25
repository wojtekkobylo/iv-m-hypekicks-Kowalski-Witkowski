package com.example.mamy_hypekicks_witkowski_kowalski

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mamy_hypekicks_witkowski_kowalski.Models.Shoe
import com.example.mamy_hypekicks_witkowski_kowalski.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val fullList = mutableListOf<Shoe>()
    private val filteredList = mutableListOf<Shoe>()
    private lateinit var adapter: ShoeGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.searchView.isIconified = false
        binding.searchView.clearFocus()



        adapter = ShoeGridAdapter(this, filteredList)
        binding.shoesGridView.adapter = adapter

        loadShoes()

        setupSearch()
        binding.btnGoToAdmin.setOnClickListener {
            val intent = android.content.Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        binding.shoesGridView.setOnItemClickListener { _, _, position, _ ->
            val selectedShoe = filteredList[position]
            val intent = android.content.Intent(this, DetailsActivity::class.java)
            intent.putExtra("SHOE_EXTRA", selectedShoe)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadShoes()
    }

    private fun loadShoes() {

        val db = FirebaseFirestore.getInstance()

        db.collection("sneakers")
            .get()
            .addOnSuccessListener { result ->

                fullList.clear()

                for (doc in result) {
                    val shoe = doc.toObject(Shoe::class.java)
                    val shoeWithId = shoe.copy(id = doc.id)
                    fullList.add(shoeWithId)
                }

                applyFilter("")
            }
    }

    // 🔍 WYSZUKIWANIE
    private fun setupSearch() {

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                applyFilter(newText ?: "")
                return true
            }
        })
    }

    // 🎯 FILTROWANIE LISTY
    private fun applyFilter(text: String) {

        val query = text.lowercase()

        filteredList.clear()

        if (query.isEmpty()) {
            filteredList.addAll(fullList)
        } else {
            for (shoe in fullList) {
                if (
                    shoe.brand.lowercase().contains(query) ||
                    shoe.modelName.lowercase().contains(query)
                ) {
                    filteredList.add(shoe)
                }
            }
        }

        adapter.notifyDataSetChanged()
    }
}