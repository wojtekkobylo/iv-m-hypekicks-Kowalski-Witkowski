package com.example.mamy_hypekicks_witkowski_kowalski

import android.os.Bundle
import android.util.Log
import android.widget.GridView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mamy_hypekicks_witkowski_kowalski.Models.Shoe
import com.example.mamy_hypekicks_witkowski_kowalski.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gridView: GridView = binding.shoesGridView

        val db = FirebaseFirestore.getInstance()

        val list = mutableListOf<Shoe>()
        val adapter = ShoeGridAdapter(this, list)

        gridView.adapter = adapter

        val TAG = "FIRESTORE_DEBUG"

        Log.d(TAG, "Rozpoczynam pobieranie danych...")

        val shoes = listOf(
            Shoe("Nike", "Air Jordan 1", 2015, 2500, "https://...")
        )

        for (shoe in shoes) {
            db.collection("sneakers")
                .add(shoe)

            db.collection("sneakers")
                .get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, "Pobrano dokumentów: ${result.size()}")

                    list.clear()

                    if (result.isEmpty) {
                        Log.d(TAG, "Kolekcja jest pusta ❗")
                    }

                    for (doc in result) {
                        val shoe = doc.toObject(Shoe::class.java)

                        Log.d(TAG, "Shoe: ${shoe.brand} ${shoe.modelName}")

                        list.add(shoe)
                    }

                    adapter.notifyDataSetChanged()
                    Log.d(TAG, "Adapter zaktualizowany")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Błąd pobierania Firestore!", e)
                }
        }


    }

    fun seedShoes() {
        val db = FirebaseFirestore.getInstance()

        val shoes = listOf(
            Shoe("Nike", "Air Jordan 1 Retro High OG Chicago", 2015, 2500, "https://i.postimg.cc/example1.jpg"),
            Shoe("Adidas", "Yeezy Boost 350 V2 Zebra", 2017, 1800, "https://i.postimg.cc/example2.jpg"),
            Shoe("Nike", "Dunk Low Panda", 2021, 800, "https://i.postimg.cc/example3.jpg"),
            Shoe("New Balance", "550 White Green", 2020, 600, "https://i.postimg.cc/example4.jpg"),
            Shoe("Nike", "Air Force 1 Off-White", 2018, 3200, "https://i.postimg.cc/example5.jpg")
        )

        for (shoe in shoes) {
            db.collection("sneakers")
                .add(shoe)
                .addOnSuccessListener {
                    println("Dodano: ${shoe.modelName}")
                }
                .addOnFailureListener {
                    println("Błąd: ${shoe.modelName}")
                }
        }
    }
}