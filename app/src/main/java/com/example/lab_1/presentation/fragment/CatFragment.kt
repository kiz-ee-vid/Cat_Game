package com.example.lab_1.presentation.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab_1.R
import com.example.lab_1.presentation.RecordsAdapter
import com.example.lab_1.data.DB
import com.example.lab_1.data.Pair
import com.example.lab_1.data.User
import com.example.lab_1.databinding.FragmentCatfragmentBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class CatFragment : Fragment() {
    lateinit var binding: FragmentCatfragmentBinding
    private val reference by lazy { DB.getReference() }
    private val user by lazy { User() }
    private lateinit var listView: RecyclerView
    private lateinit var infoDialog: Dialog
    private lateinit var recordDialog: Dialog
    private lateinit var recordsAdapter: RecordsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatfragmentBinding.inflate(layoutInflater)
        init()
        initHandlers()
        return binding.root
    }

    private fun init() {
        binding.textView2.text = saciety.toString()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        requireArguments().getString("hash")?.let { getUser(it) }
        makeMenu()
        makeInfoDialog()
        makeRecordDialog()
        recordsAdapter = RecordsAdapter(user.list)
        listView.adapter = recordsAdapter
        listView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getUser(hash: String) {
        reference.child("users").child(hash).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val temp = snapshot.getValue(User::class.java)!!
                user.list.clear()
                user.list.addAll(temp.list)
                user.login = temp.login
            }

            override fun onCancelled(error: DatabaseError) {
                println(error.message)
            }
        })
    }

    private fun makeMenu() {
        setHasOptionsMenu(true)
        val ma: AppCompatActivity = activity as AppCompatActivity
        ma.setSupportActionBar(binding.toolbar)
        ma.supportActionBar?.title = "Lab1"
    }

    private fun makeInfoDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(R.layout.info_page)
        builder.setTitle("Info Page")
        builder.setPositiveButton("Ok") { infoDialog, _ ->
            infoDialog.dismiss()
        }
        infoDialog = builder.create()
    }

    private fun makeRecordDialog() {
        listView = RecyclerView(requireContext())
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setTitle("Records")
        builder.setPositiveButton("Ok") { recordDialog, _ ->
            recordDialog.dismiss()
        }
        builder.setView(listView)
        recordDialog = builder.create()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info ->
                infoDialog.show()
            R.id.share -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, "Hey, my score is $saciety")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share To:"))
            }
            R.id.records -> {
                recordsAdapter.notifyDataSetChanged()
                recordDialog.show()
            }
            R.id.save -> {
                user.list.add(
                    Pair(
                        saciety,
                        SimpleDateFormat(DATE_FORMAT).format(Date().time).toString()
                    )
                )
                reference.child("users").child(user.login.hashCode().toString()).child("list").setValue(user.list)
                Toast.makeText(context, "Result saved", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun initHandlers() {
        binding.Feed.setOnClickListener() {
            ++saciety
            binding.textView2.text = saciety.toString()
            if (saciety % 15 == 0) {
                val animation: Animation = AnimationUtils.loadAnimation(
                    activity?.applicationContext, R.anim.crazy_cat
                )
                binding.imageView.startAnimation(animation)
            }
        }
    }

    companion object {
        private var saciety = 0
        const val DATE_FORMAT = "dd MM yyyy, HH:mm:ss"
        fun newInstance(item: String): CatFragment {
            val fragment = CatFragment()
            val args = Bundle()
            args.putString("hash", item)
            fragment.arguments = args
            return fragment
        }
    }
}