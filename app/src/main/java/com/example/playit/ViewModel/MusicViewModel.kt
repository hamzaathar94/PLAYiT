package com.example.playit.ViewModel

import android.app.Application
import android.content.IntentSender
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playit.Model.Music
import com.example.playit.Repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MusicViewModel(private val musicRepository: MusicRepository):ViewModel(){
    val audios: MutableLiveData<List<Music>> = musicRepository.musicLiveData
    val videordb: LiveData<List<Music>> = musicRepository.getallMusic()

    fun getAudios() {
        musicRepository.getMusic()
    }

    fun getAudioLiveData(): LiveData<List<Music>> {
        musicRepository.getAudiosLiveData()
        return audios
    }

    //room
    fun insertMusic(title: String,album:String,duration:Long, path: String,musicUrl:Uri,musicId:Long) {
        GlobalScope.launch {
            val music=Music(null, title,album,duration,path,musicUrl.toString(),musicId)
           // musicRepository.insertmusic(music)
        }
    }

    fun deleteMusic(music: Music) {
        GlobalScope.launch {
            musicRepository.deleteImage(music)
        }
    }

    fun search(title: String): LiveData<List<Music>> {
        return musicRepository.search(title)
    }
    val permissionNeed: LiveData<IntentSender?> = musicRepository.permissionNeededForDelete
    fun pendingDelete(music: Music){
        GlobalScope.launch {
            musicRepository.deletePendingImage()
        }
    }

}