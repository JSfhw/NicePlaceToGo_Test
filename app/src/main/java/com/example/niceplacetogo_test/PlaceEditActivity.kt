package com.example.niceplacetogo_test

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.room.Room

class PlaceEditActivity : AppCompatActivity(), DialogInterface.OnClickListener {

    private var placeDao: PlacesDao? = null
    private var place: Place? = null
    private var currentLat: Double = Double.MIN_VALUE
    private var currentLong: Double = Double.MIN_VALUE
    private var noteLat: Double = Double.MIN_VALUE
    private var noteLong: Double = Double.MIN_VALUE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_edit)

        // Set toolbar
        setSupportActionBar(findViewById(R.id.tbEdit))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        // Find views by Id
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val ivPicture = findViewById<ImageView>(R.id.etPicture)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnGPS = findViewById<Button>(R.id.btnGPS)
        val btnShoot = findViewById<Button>(R.id.btnShoot)

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
            noteLat = place!!.imgLongitude
            noteLong = place!!.imgLatitude

            updateLocation(place!!.imgLatitude, place!!.imgLongitude)

            var imgBase64: String;
            imgBase64 = if (place != null) place!!.imgBase64 else ""
            if ( imgBase64.length > 0 )
            {
                ivPicture.setImageBitmap(HelperTools.decodePicString(imgBase64))
            } else
            {
                ivPicture.setImageResource(HelperTools.getDefaultImageResource())
            }

        }

        registerLocationListener()

        // initialize Media Library Picker
        val contract = ActivityResultContracts.PickVisualMedia()
        val imagePicker = registerForActivityResult(contract) { uri ->
            if (uri != null) {
                ivPicture.setImageURI(uri)
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show()
            }
        }

        btnShoot.setOnClickListener{
            imagePicker.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        }

        // Set OnClickListener
        btnSave.setOnClickListener{
            val description = etDescription?.text.toString()
            val imgBase64 = HelperTools.encodePicString(ivPicture.drawToBitmap())

            if (place != null) {
                place!!.imgDescription = description
                place!!.imgLongitude = noteLong
                place!!.imgLatitude = noteLat
                place!!.imgBase64 = imgBase64
                placeDao?.update(place!!)
            } else {
                placeDao!!.insertAll(Place(description,imgBase64,noteLong,noteLat,0))
            }

            // Show toast for user
            // Toast.makeText(this, placeDao!!.getAll().toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(this, placeDao!!.getDescription() + " saved", Toast.LENGTH_LONG).show()

            finish()
        }

        btnGPS.setOnClickListener{
            if(currentLat != Double.MIN_VALUE && currentLong != Double.MIN_VALUE) {
                updateLocation(currentLat, currentLong)
                Toast.makeText(this, "Location updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No GPS information available.", Toast.LENGTH_SHORT).show()
            }
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


    private fun registerLocationListener() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 2)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f) { p0: Location ->
                currentLat = p0.latitude
                currentLong = p0.longitude

                if(noteLat == Double.MIN_VALUE || noteLong == Double.MIN_VALUE) {
                    updateLocation(currentLat, currentLong)
                }

            }
        }
    }

    private fun updateLocation(lat:Double, long:Double) {
        if(lat == Double.MIN_VALUE || long == Double.MIN_VALUE) return
        noteLat = lat
        noteLong = long
        val etLatitude = findViewById<EditText>(R.id.etLatitude)
        val etLongitude = findViewById<EditText>(R.id.etLongitude)
        etLongitude?.setText(noteLong.toString())
        etLatitude?.setText(noteLat.toString())
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.size > 0 && grantResults[0] >= 0) {
            registerLocationListener()
        }
    }


}
