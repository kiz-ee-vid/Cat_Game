package com.example.lab_1.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lab_1.presentation.fragment.CatFragment
import com.example.lab_1.R
import com.example.lab_1.presentation.fragment.RegistrationFragment
import com.example.lab_1.databinding.ActivityMainBinding
import com.example.lab_1.domain.OnItemClickListener

class MainActivity : AppCompatActivity(), OnItemClickListener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(savedInstanceState == null)
        createFirstFragment()
    }

    private fun createFirstFragment() {
        supportFragmentManager
            .beginTransaction().replace(R.id.placeHolder, RegistrationFragment.newInstance())
            .commit()
    }

    private fun createSecondFragment(login: String) {
        val secondFragment = CatFragment.newInstance(login)
        supportFragmentManager
            .beginTransaction().replace(R.id.placeHolder, secondFragment)
            .addToBackStack("null")
            .commit()
    }

    override fun onItemClick(hash: String) {
        createSecondFragment(hash)
    }
}