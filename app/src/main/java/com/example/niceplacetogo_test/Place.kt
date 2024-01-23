package com.example.niceplacetogo_test

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class Place (
    //@ColumnInfo(name="title") var title: String,
    @ColumnInfo(name="imgDescription") var imgDescription: String,
    @ColumnInfo(name="imgBase64") var imgBase64: String,
    @ColumnInfo(name="imgLongitude") var imgLongitude: String,
    @ColumnInfo(name="imgLatitude") var imgLatitude: String,
    @ColumnInfo(name="imgLikes") var imgLike: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)