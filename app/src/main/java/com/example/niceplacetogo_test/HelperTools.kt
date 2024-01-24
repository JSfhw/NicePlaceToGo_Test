package com.example.niceplacetogo_test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


class HelperTools {

    /* static methods */
    companion object {
        fun decodePicString(encodedString: String): Bitmap {

            val imageBytes = Base64.decode(encodedString, Base64.DEFAULT)

            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        }

        fun encodePicString(bm:Bitmap): String {
            val byteArray = ByteArrayOutputStream()
            bm.compress(Bitmap.CompressFormat.JPEG, 100, byteArray)
            return Base64.encodeToString(byteArray.toByteArray(), Base64.DEFAULT)
        }

        fun getDefaultImageResource(): Int {
            return R.drawable.ic_photo_camera
        }
    }

}