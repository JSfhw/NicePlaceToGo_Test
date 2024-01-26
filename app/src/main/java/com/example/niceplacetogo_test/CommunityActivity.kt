package com.example.niceplacetogo_test

import android.content.ClipDescription
import android.content.Intent
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.room.Room

class CommunityActivity : AppCompatActivity() {

    private var placeDao: PlacesDao? = null
    private var adapter: PlacesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.community_list)

        setSupportActionBar(findViewById(R.id.tbMain))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        supportActionBar?.title = getString(R.string.title_community)

        // Initialize Room DB
        val db = Room.databaseBuilder(
            applicationContext,
            PlacesDatabase::class.java, "places"
        ).allowMainThreadQueries().build()
        placeDao = db.PlacesDao()

        /* insert Samples Entries when no database entries exists */
        if ( placeDao!!.getCommunityRecordCount() <= 0 ){
            insertCommunitySample()
        }

        // Find view (list_item_views per entry) by Ids
        val lvPlaces = findViewById<ListView>(R.id.lvPlaces)
        adapter = PlacesAdapter(this, placeDao!!.getCommunity())
        lvPlaces.adapter = adapter



    }

    /* refresh display */
    override fun onResume() {
        super.onResume()

        // Update View
        adapter?.places = placeDao!!.getCommunity()
        adapter?.notifyDataSetChanged()
    }

    /* create menu entries */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_community, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /* demonstration for displaying pictures from community */
    private fun insertCommunitySample() {
        insertSample("Alor Indonesia", "alor.png",  124.379457,-8.174303,10)
        insertSample("Halmahera", "Halmahera.jpg",  128.513807,1.624558,10)
        insertSample("Turracher Höhe", "Turracherhoehe.jpg",  13.87525,46.913901,10)
    }

    /* hide community demonstration - deletes entries on database */
    private fun removeCommunitySample() {
        placeDao!!.deleteSamples()
    }

    private fun insertSample(description: String, imageName: String, longitude : Double, latitude: Double, likes: Int) {
        val ims2 = assets.open(imageName)
        //var exif = ExifInterface(ims2)
        val base64 : String = Base64.encodeToString(ims2.readAllBytes(), Base64.DEFAULT)
        placeDao!!.insertAll(Place(description, base64, longitude, latitude, likes))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }

}