package com.example.niceplacetogo_test

import android.content.DialogInterface
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import kotlin.io.encoding.Base64

class PlaceEditActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    private var placeDao: PlacesDao? = null
    private var place: Place? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_edit)

        // Set toolbar
        setSupportActionBar(findViewById(R.id.tbEdit))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        // Find views by Id
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val etLongitude = findViewById<EditText>(R.id.etLongitude)
        val etLatitude = findViewById<EditText>(R.id.etLatitude)
        val etBase64 = findViewById<EditText>(R.id.etBase64)
        val etPicture = findViewById<ImageView>(R.id.etPicture)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Initialize Room DB
        val db = Room.databaseBuilder(
            applicationContext,
            PlacesDatabase::class.java, "places"
        ).allowMainThreadQueries().build()
        placeDao = db.PlacesDao()

        // Get place id from Intent
        val id = intent.getLongExtra("id", -1)
        if (id >= 0) {
            place = placeDao!!.loadAllByIds(id.toInt())[0]
            etDescription?.setText(place?.imgDescription)
            etLongitude?.setText(place?.imgLongitude)
            etLatitude?.setText(place?.imgLatitude)

            var imgBase64: String;
            imgBase64 = if (place != null) place!!.imgBase64 else ""
            if ( imgBase64.length > 0 )
            {
                etPicture.setImageBitmap(HelperTools.decodePicString(imgBase64))
                etBase64?.setText( place?.imgBase64 )
            } else
            {
                etPicture.setImageResource(HelperTools.getDefaultImageResource())
                etBase64?.setText("")
            }

        }

        // Set OnClickListener
        btnSave.setOnClickListener{
            val description = etDescription?.text.toString()
            val longitude = etLongitude?.text.toString()
            val latitude = etLatitude?.text.toString()
            val imgBase64 = etBase64?.text.toString()
            /* TODO: image to base64 */

            if (place != null) {
                place!!.imgDescription = description
                place!!.imgLongitude = longitude
                place!!.imgLatitude = latitude
                place!!.imgBase64 = imgBase64
                placeDao?.update(place!!)
            } else {
                placeDao!!.insertAll(Place(description,imgBase64,longitude,latitude,0))
            }

            // Show toast for user
            // Toast.makeText(this, placeDao!!.getAll().toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(this, placeDao!!.getDescription() + " saved", Toast.LENGTH_LONG).show()

            finish()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.delete -> showDeleteDialog()
            //R.id.save -> savePlace()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.delete_message))
            .setPositiveButton(getString(R.string.yes), this)
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    override fun onClick(p0: DialogInterface?, p1: Int) {
        place?.let {
            placeDao?.delete(it)

            // Display Toast
            Toast.makeText(this, R.string.delete_message, Toast.LENGTH_LONG).show()

            finish()
        }
    }




}