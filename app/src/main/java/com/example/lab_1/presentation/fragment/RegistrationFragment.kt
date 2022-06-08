package com.example.lab_1.presentation.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.lab_1.data.DB
import com.example.lab_1.data.User
import com.example.lab_1.databinding.FragmentRegistrationBinding
import com.example.lab_1.domain.OnItemClickListener
import com.example.lab_1.presentation.activity.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationFragment : Fragment(), OnItemClickListener {

    private lateinit var binding: FragmentRegistrationBinding
    private val reference by lazy { DB.getReference() }
    private val auth by lazy { Firebase.auth }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)

        setListener()
        return binding.root
    }

    private fun checkData(login: String, password: String): Boolean {
        if (login.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(login)
                .matches()
        )
            return true
        else
            Toast.makeText(context, "Incorrect login/password", Toast.LENGTH_SHORT).show()
        return false
    }

    private fun setListener() {
        binding.Registration.setOnClickListener() {
            val login = binding.Email.text.toString()
            val password = binding.Password.text.toString()
            if (checkData(login, password)) {
                auth.createUserWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        reference.child("users").child(login.hashCode().toString())
                            .setValue(User(login, mutableListOf()))
                        onItemClick(login.hashCode().toString())
                    } else
                        Toast.makeText(context, "Such account already exist", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.Login.setOnClickListener {
            val login = binding.Email.text.toString()
            val password = binding.Password.text.toString()
            if (checkData(login, password)) {
                auth.signInWithEmailAndPassword(login, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onItemClick(login.hashCode().toString())
                    } else
                        Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    override fun onItemClick(hash: String) {
        val ma: MainActivity = activity as MainActivity
        ma.onItemClick(hash)
    }
}