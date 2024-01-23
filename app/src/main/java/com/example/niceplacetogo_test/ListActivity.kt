package com.example.niceplacetogo_test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.room.Room

class ListActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private var placeDao: PlacesDao? = null
    private var adapter: PlacesAdapter? = null

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

        // Find view by Ids
        val lvPlaces = findViewById<ListView>(R.id.lvPlaces)
        adapter = PlacesAdapter(this, placeDao!!.getAll())
        lvPlaces.adapter = adapter
        lvPlaces.onItemClickListener = this
    }

    override fun onResume() {
        super.onResume()

        // Update View
        adapter?.places = placeDao!!.getAll()
        adapter?.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add) {
            val intent = Intent(this, PlaceEditActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, id: Long) {
        val intent = Intent(this, PlaceEditActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}