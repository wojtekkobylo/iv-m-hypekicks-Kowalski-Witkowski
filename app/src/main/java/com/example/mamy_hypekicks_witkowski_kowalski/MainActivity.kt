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

        val db = FirebaseFirestore.getInstance()

        adapter = ShoeGridAdapter(this, filteredList)
        binding.shoesGridView.adapter = adapter

        val TAG = "FIRESTORE_DEBUG"

        Log.d(TAG, "Start pobierania danych...")

        // 🔥 POPRAWIONE: jedno pobranie, bez pętli
        db.collection("sneakers")
            .get()
            .addOnSuccessListener { result ->

                fullList.clear()

                Log.d(TAG, "Pobrano dokumentów: ${result.size()}")

                if (result.isEmpty) {
                    Log.d(TAG, "Kolekcja jest pusta ❗")
                }

                for (doc in result) {
                    val shoe = doc.toObject(Shoe::class.java)
                    fullList.add(shoe)

                    Log.d(TAG, "Shoe: ${shoe.brand} ${shoe.modelName}")
                }

                applyFilter("") // pokaż wszystko na start
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Błąd Firestore!", e)
            }

        setupSearch()
        binding.btnGoToAdmin.setOnClickListener {
            val intent = android.content.Intent(this, AdminActivity::class.java)
            startActivity(intent)
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