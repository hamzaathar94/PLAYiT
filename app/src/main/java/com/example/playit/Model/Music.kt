package com.example.playit.Model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI
import java.net.URL
import java.util.concurrent.TimeUnit

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = true) var id:Int?,
                 var title:String,
                 var album:String,
                // var artist:String,
                 var duration: Long = 0,
                 var path: String,
                 var contentUri: String,
                 var musicId:Long
)
fun formatDuration(duration: Long):String{
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes* TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

