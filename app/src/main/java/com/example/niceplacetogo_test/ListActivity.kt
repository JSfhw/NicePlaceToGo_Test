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

class ListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var placeDao: PlacesDao? = null
    private var adapter: PlacesAdapter? = null
    private var isDisplayCommunity : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        setSupportActionBar(findViewById(R.id.tbMain))

        // Initialize Room DB
        val db = Room.databaseBuilder(
            applicationContext,
            PlacesDatabase::class.java, "places"
        ).allowMainThreadQueries().build()
        placeDao = db.PlacesDao()

        /* insert Samples Entries when no database entries exists */
        if ( placeDao!!.getRecordCount() <= 0 ){
            insertDefaultSamples()
        }


        // Find view (list_item_views per entry) by Ids
        val lvPlaces = findViewById<ListView>(R.id.lvPlaces)
        adapter = PlacesAdapter(this, placeDao!!.getAll())
        lvPlaces.adapter = adapter
        lvPlaces.onItemClickListener = this

        /**
        val btnCommunity = findViewById<Button>(R.id.btnCommunity)
        val intent = Intent(this, CommunityActivity::class.java)
        btnCommunity.setOnClickListener{
            startActivity((intent))
        }
        **/


    }

    /* refresh display */
    override fun onResume() {
        super.onResume()

        // Update View
        adapter?.places = placeDao!!.getAll()
        adapter?.notifyDataSetChanged()
    }

    /* create menu entries */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)

        return super.onCreateOptionsMenu(menu)
    }

    /* menu items selected */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val intent = Intent(this, PlaceEditActivity::class.java)
            startActivity(intent)
        }

        /* show or hide community "sample" entries */
        if (item.itemId == R.id.community) {
            if (!isDisplayCommunity) {
                isDisplayCommunity = true
                showCommunitySample()
                Toast.makeText(this, "displaying community", Toast.LENGTH_LONG).show()
            } else {
                removeCommunitySample()
                isDisplayCommunity = false
                Toast.makeText(this, "hide community", Toast.LENGTH_LONG).show()
            }
            onResume()
        }

        return super.onOptionsItemSelected(item)
    }

    /* click on places entry start edit */
    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, id: Long) {
        val intent = Intent(this, PlaceEditActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }


    /* demonstration for displaying pictures from community */
    private fun showCommunitySample() {
        insertSample("Alor Indonesia", "alor.png",  124.379457,-8.174303,10)
        insertSample("Halmahera", "Halmahera.jpg",  128.513807,1.624558,10)
        insertSample("Turracher Höhe", "Turracherhoehe.jpg",  13.87525,46.913901,10)
    }

    /* hide community demonstration - deletes entries on database */
    private fun removeCommunitySample() {
        placeDao!!.deleteSamples()
    }


    private fun insertDefaultSamples() {
        insertSample("Protea Banks", "ProteaBanks.jpg", 30.48419,-30.83366,0 )
        insertSample("Malediven", "Malediven.jpg",  73.578195,2.103372,0)
    }

    private fun insertSample(description: String, imageName: String, longitude : Double, latitude: Double, likes: Int) {
        val ims2 = assets.open(imageName)
        //var exif = ExifInterface(ims2)
        val base64 : String = Base64.encodeToString(ims2.readAllBytes(), Base64.DEFAULT)
        placeDao!!.insertAll(Place(description, base64, longitude, latitude, likes))
    }


}