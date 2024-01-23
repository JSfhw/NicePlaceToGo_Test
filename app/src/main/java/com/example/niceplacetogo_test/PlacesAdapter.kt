package com.example.niceplacetogo_test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PlacesAdapter(var context: Context, var places: List<Place>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private class ViewHolder {
        lateinit var imgDescription: TextView
        lateinit var imgLongitude: TextView
        lateinit var imgLatitude: TextView
        lateinit var imgPicture: ImageView

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
            holder.imgLongitude = view.findViewById(R.id.tvLongitude)
            holder.imgLatitude = view.findViewById(R.id.tvLatitude)
            holder.imgPicture = view.findViewById(R.id.ivPicture)

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val tvDescription = holder.imgDescription
        val tvLongitude = holder.imgLongitude
        val tvLatitude = holder.imgLatitude
        val ivPicture = holder.imgPicture
        val place = places[position]

        tvDescription.text = place.imgDescription
        tvLongitude.text = place.imgLongitude
        tvLatitude.text = place.imgLatitude

        val imgBase64: String = place.imgBase64
        if (imgBase64.isNotEmpty())
            ivPicture.setImageBitmap(HelperTools.decodePicString(imgBase64))
         else
            ivPicture.setImageResource(HelperTools.getDefaultImageResource())

        return view
    }
}