package com.example.niceplacetogo_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        // Rotate Icon
        val ivLogo = findViewById<ImageView>(R.id.ivLogo)
        val rotation = AnimationUtils.loadAnimation(this, R.anim.rotate)
        ivLogo.startAnimation(rotation)


        val intent = Intent(this, ListActivity::class.java)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(intent)
            }
        }, 3000)

    }


}