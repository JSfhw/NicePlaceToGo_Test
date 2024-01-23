package com.example.niceplacetogo_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.tbMain))


        // Find views by ID
        val btnShoot = findViewById<Button>(R.id.btnShoot)

        // Rotate Icon
        val ivCamera = findViewById<ImageView>(R.id.ivCamera)
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        ivCamera.startAnimation(rotation)

        // Set OnClickListener
        btnShoot.setOnClickListener {
            // Show Signed-In-Message
            Toast.makeText(this, getString(R.string.click), Toast.LENGTH_LONG).show()

            // Open ListActivity
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
    }

}