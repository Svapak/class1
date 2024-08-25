package com.example.sih

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragment(AnalyzeFragment())

        val bottomNavigationView : BottomNavigationView = findViewById(R.id.BottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener {

            if(it.itemId == R.id.detect){
                replaceFragment(AnalyzeFragment())
            }
            if (it.itemId == R.id.chat){
                replaceFragment(ChatFragment())
            }
            if (it.itemId == R.id.warehouse){
                replaceFragment(NavigateFragment())
            }
            if (it.itemId == R.id.maps){
                replaceFragment(SearchFragment())
            }
            if (it.itemId == R.id.inventory){
                replaceFragment(InventoryFragment())
            }
            return@setOnItemSelectedListener true
        }

    }

    public fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu!!.add("Log Out")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.title?.equals("Log Out") == true){
            val builder = AlertDialog.Builder(this)
            builder.setTitle("LOG OUT")
            builder.setMessage("Are you sure?")
            builder.setPositiveButton("LOG OUT", DialogInterface.OnClickListener{ dialog, which->
                firebaseAuth.signOut()
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            })
            builder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which-> })
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }
        if(item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}