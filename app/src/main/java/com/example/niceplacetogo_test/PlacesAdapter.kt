package com.example.niceplacetogo_test

import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.Locale
import kotlin.math.roundToLong

class PlacesAdapter(var context: Context, var places: List<Place>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private class ViewHolder {
        lateinit var imgDescription: TextView
        lateinit var imgLongitude: TextView
        lateinit var imgLatitude: TextView
        lateinit var imgPicture: ImageView
        lateinit var imgLocation: TextView
        lateinit var imgLocationGPS: TextView

    }
    override fun getCount(): Int {
        return places.size
    }

    override fun getItem(position: Int): Any {
        return places[position]
    }

    override fun getItemId(position: Int): Long {
        return places[position].id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_view, parent, false)

            holder = ViewHolder()
            holder.imgDescription = view.findViewById(R.id.tvDescription)
            // holder.imgLongitude = view.findViewById(R.id.tvLongitude)
            // holder.imgLatitude = view.findViewById(R.id.tvLatitude)
            holder.imgLocation = view.findViewById(R.id.tvLocation)
            holder.imgLocationGPS = view.findViewById(R.id.tvLocationGPS)
            holder.imgPicture = view.findViewById(R.id.ivPicture)

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val tvDescription = holder.imgDescription
        //val tvLongitude = holder.imgLongitude
        //val tvLatitude = holder.imgLatitude
        val tvLocation = holder.imgLocation
        val tvLocationGPS = holder.imgLocationGPS
        val ivPicture = holder.imgPicture
        val place = places[position]

        tvDescription.text = place.imgDescription
        // tvLongitude.text = place.imgLongitude.toString()
        // tvLatitude.text = place.imgLatitude.toString() "
        val helplong = String.format("%.6f", place.imgLongitude)
        val helplat =  String.format("%.6f", place.imgLatitude)
        tvLocationGPS.text = "$helplong, $helplat"

        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = place!!.imgLatitude?.let {
                place.imgLongitude?.let { it1 ->
                    geocoder.getFromLocation(
                        it,
                        it1,
                        1
                    )
                }
            }
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0].getAddressLine(0)
                tvLocation.text = address
            }
        } catch (e: Exception) {
                /* invalid coordinates */
                tvLocation.text = ""
              }



        /* create convert bitmap from base64 database string */
        val imgBase64: String = place.imgBase64
        if (imgBase64.isNotEmpty())
            ivPicture.setImageBitmap(HelperTools.decodePicString(imgBase64))
        else
            ivPicture.setImageResource(HelperTools.getDefaultImageResource())

        return view
    }
}