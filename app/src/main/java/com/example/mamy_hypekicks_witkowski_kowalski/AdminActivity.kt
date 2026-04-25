package com.example.mamy_hypekicks_witkowski_kowalski

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mamy_hypekicks_witkowski_kowalski.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAdminBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        binding.btnBackToShop.setOnClickListener {
            finish()
        }
    }
}