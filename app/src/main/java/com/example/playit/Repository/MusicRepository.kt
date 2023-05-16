package com.example.playit.Repository

import android.app.Application
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.Context
import android.content.IntentSender
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playit.Model.Music
import com.example.playit.RoomDB.MusicDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicRepository(val context: Context,private val musicDatabase: MusicDatabase) {
    val musicLiveData=MutableLiveData<List<Music>>()

    fun getMusic(){
        val audios= mutableListOf<Music>()
        val sortOrder="${MediaStore.Audio.Media.DATE_ADDED}DESC"
        val cursor=context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        null,null,null,null)
        if (cursor!=null && cursor.moveToFirst()){
            do {
                val title=
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
                val album=
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                val artist=
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                val duration=
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                val path=
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                val id=
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toLong()
                )
                audios.add(Music(null,title,album,duration.toLong(),path,contentUri.toString(),id.toLong()))
            }while (cursor.moveToNext())
            cursor.close()
        }
        musicLiveData.postValue(audios)
    }

    fun getAudiosLiveData(): LiveData<List<Music>> {
        return musicLiveData
    }

    //
    fun getallMusic(): LiveData<List<Music>> {
        return musicDatabase.getMusicDao().getAllMusic()
    }


    suspend fun insertmusic(music: Music) {
        musicDatabase.getMusicDao().insert(music)
    }

    fun deleteMusic(music: Music) {
        musicDatabase.getMusicDao().deletemusic(music)
    }

    fun search(name: String): LiveData<List<Music>> {
        return musicDatabase.getMusicDao().search(name)
    }

    private suspend fun performDeleteImage(music: Music) {
        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.delete(
                    music.contentUri.toUri(),
                    "${MediaStore.Audio.Media._ID} = ?",
                    arrayOf(music.id.toString()
                    )
                )
            } catch (securityException: SecurityException) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val recoverableSecurityException =
                        securityException as? RecoverableSecurityException
                            ?: throw securityException
                    pendingDeleteImage = music
                    _permissionNeededForDelete.postValue(
                        recoverableSecurityException.userAction.actionIntent.intentSender
                    )
                } else {
                    throw securityException
                }
            }
        }
    }
    fun deleteImage(music: Music) {
        GlobalScope.launch {
            performDeleteImage(music)
        }
    }
    private var pendingDeleteImage: Music? = null
    private val _permissionNeededForDelete = MutableLiveData<IntentSender?>()
    val permissionNeededForDelete: LiveData<IntentSender?> = _permissionNeededForDelete
    fun deletePendingImage() {
        pendingDeleteImage?.let { music ->
            pendingDeleteImage = null
            deleteImage(music)
        }
    }
}