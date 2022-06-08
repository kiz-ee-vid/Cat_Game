package com.example.laba_1_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.laba_1_test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createFirstFragment()
    }

    private fun createFirstFragment() {
        supportFragmentManager
            .beginTransaction().replace(R.id.placeHolder, RegistrationFragment(this){
                createSecondFragment(it)
            })
            .commit()
    }

    private fun createSecondFragment(login: String) {
        val secondFragment = CatFragment(login)
        supportFragmentManager
            .beginTransaction().replace(R.id.placeHolder, secondFragment)
            .addToBackStack("null")
            .commit()
    }
}