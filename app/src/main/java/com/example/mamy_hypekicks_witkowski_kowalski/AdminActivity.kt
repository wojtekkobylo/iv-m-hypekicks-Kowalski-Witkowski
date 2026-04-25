package com.example.mamy_hypekicks_witkowski_kowalski

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mamy_hypekicks_witkowski_kowalski.Models.Shoe
import com.example.mamy_hypekicks_witkowski_kowalski.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAdminBinding

    private val fullList = mutableListOf<Shoe>()
    private val filteredList = mutableListOf<Shoe>()
    private lateinit var adapter: ShoeGridAdminAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.searchAdminView.isIconified = false
        binding.searchAdminView.clearFocus()

        binding.btnAddShoe.setOnClickListener {
            addShoeToFirestore()
        }

        binding.btnBackToShop.setOnClickListener {
            finish()

        }


        val db = FirebaseFirestore.getInstance()

        adapter = ShoeGridAdminAdapter(this, filteredList) { shoe ->

            FirebaseFirestore.getInstance()
                .collection("sneakers")
                .document(shoe.id)
                .delete()
                .addOnSuccessListener {

                    fullList.removeIf { it.id == shoe.id }
                    applyFilter("")
                }

            adapter.notifyDataSetChanged()

        }


        binding.shoesAdminGridView.adapter = adapter

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
                    val shoeWithId = shoe.copy(id = doc.id)

                    fullList.add(shoeWithId)
                }

                applyFilter("") // pokaż wszystko na start
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Błąd Firestore!", e)
            }

        setupSearch()
    }

    private fun setupSearch() {

        binding.searchAdminView.setOnQueryTextListener(object :
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

    private fun addShoeToFirestore() {

        val brand = binding.etBrand.text.toString().trim()
        val model = binding.etModel.text.toString().trim()
        val year = binding.etYear.text.toString().trim()
        val price = binding.etPrice.text.toString().trim()
        val image = binding.etImage.text.toString().trim()

        if (brand.isEmpty() || model.isEmpty() || year.isEmpty() || price.isEmpty() || image.isEmpty()) {
            return
        }

        val shoe = hashMapOf(
            "brand" to brand,
            "modelName" to model,
            "year" to year.toInt(),
            "resellPrice" to price.toInt(),
            "imageUrl" to image
        )

        FirebaseFirestore.getInstance()
            .collection("sneakers")
            .add(shoe)
            .addOnSuccessListener {

                // dodaj lokalnie do listy
                val newShoe = Shoe(
                    brand = brand,
                    modelName = model,
                    releaseYear = year.toInt(),
                    resellPrice = price.toInt(),
                    imageUrl = image
                )

                fullList.add(newShoe)
                applyFilter("") // odśwież grid

                clearForm()
            }
            .addOnFailureListener {
                Log.e("FIRESTORE", "Błąd dodawania", it)
            }
    }

    private fun clearForm() {
        binding.etBrand.text.clear()
        binding.etModel.text.clear()
        binding.etYear.text.clear()
        binding.etPrice.text.clear()
        binding.etImage.text.clear()
    }
}