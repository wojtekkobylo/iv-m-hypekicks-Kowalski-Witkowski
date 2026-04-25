package com.example.mamy_hypekicks_witkowski_kowalski

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mamy_hypekicks_witkowski_kowalski.Models.Shoe
import com.example.mamy_hypekicks_witkowski_kowalski.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val shoe = intent.getSerializableExtra("SHOE_EXTRA") as? Shoe

        shoe?.let {
            binding.detailName.text = "${it.brand} ${it.modelName}"
            binding.detailPrice.text = "Cena: ${it.resellPrice} PLN"
            binding.detailYear.text = "Rok wydania: ${it.releaseYear}"

            Glide.with(this)
                .load(it.imageUrl)
                .placeholder(android.R.drawable.progress_horizontal)
                .into(binding.detailImage)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}