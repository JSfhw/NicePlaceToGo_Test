package com.example.niceplacetogo_test

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import java.util.TimerTask


class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)


        val ims = assets.open("alor.png")
        val d = Drawable.createFromStream(ims, null)
        val ivSplash = findViewById<ImageView>(R.id.ivSplash)
        ivSplash.setImageDrawable(d)

       // val text = findViewById<EditText>(R.id.etBase64)
       // val ims2 = assets.open("alor.png")
       // val base64 : String = Base64.encodeToString(ims2.readAllBytes(), Base64.DEFAULT)


        val intent = Intent(this, ListActivity::class.java)
        Timer().schedule(object : TimerTask() {
            override fun run() {
                // startActivity(intent)
                finish()
            }
        }, 3000)

    }


}