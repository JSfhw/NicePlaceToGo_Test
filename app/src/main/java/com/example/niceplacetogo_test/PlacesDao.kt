package com.example.niceplacetogo_test

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlacesDao {

    @Query("DELETE FROM places WHERE imgLikes > 0")
    fun deleteSamples()

    @Query("SELECT * FROM places WHERE imgLikes > 0")
    fun getCommunity(): List<Place>

    @Query("SELECT COUNT(*) FROM places WHERE imgLikes > 0")
    fun getCommunityRecordCount(): Double


    @Query("SELECT COUNT(*) FROM places WHERE imgLikes = 0")
    fun getRecordCount(): Double


    @Query("SELECT * FROM places WHERE imgLikes = 0")
    fun getAll(): List<Place>

    @Query("SELECT imgDescription FROM places WHERE imgLikes = 0")
    fun getDescription(): String

    @Insert
    fun insertAll(vararg places: Place)

    @Delete
    fun delete(place: Place)

    @Update
    fun update(place: Place)

    @Query("SELECT * FROM places WHERE id IS (:id)")
    fun loadAllByIds(id: Int): List<Place>
}