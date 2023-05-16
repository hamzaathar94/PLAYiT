package com.example.playit.Fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.RecoverableSecurityException
import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playit.Adapter.MusicAdapter
import com.example.playit.Fectory.MediaFectory
import com.example.playit.Interface.onAudioClick
import com.example.playit.Model.Music
import com.example.playit.Model.formatDuration
import com.example.playit.R
import com.example.playit.Repository.MusicRepository
import com.example.playit.RoomDB.MusicDatabase
import com.example.playit.SharedPreference.MyPreference
import com.example.playit.ViewModel.MusicViewModel
import com.example.playit.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class HomeFragment : Fragment(), onAudioClick {
    private var binding: FragmentHomeBinding? = null
    private var recyclerView: RecyclerView? = null
    private var musicViewModel: MusicViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // in here you can do logic when backPress is clicked
                requireActivity().finish()
                onDestroy()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            FragmentHomeBinding.inflate(LayoutInflater.from(requireContext()), container, false)
        setHasOptionsMenu(true)
        val myPreference= MyPreference(requireContext())
        //requestPermissions()
        val v=myPreference.getString("name","")
        if (v=="linear"){
            recyclerView = binding?.recyclerview
            val db = MusicDatabase.getDatabase(requireContext())
            try {
                val musicRepository = MusicRepository(requireContext(), db)
                musicViewModel = ViewModelProvider(
                    this,
                    MediaFectory(musicRepository)
                ).get(MusicViewModel::class.java)
                musicViewModel?.getAudios()
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                musicViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
                    recyclerView?.adapter = MusicAdapter(requireContext(), it, this)
                })
                findNavController().navigate(R.id.homeFragment)

            } catch (e: java.lang.Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("ooo", e.message.toString())
            }
        }
        else if (v=="grid"){
            recyclerView = binding?.recyclerview
            val db = MusicDatabase.getDatabase(requireContext())
            try {
                val musicRepository = MusicRepository(requireContext(), db)
                musicViewModel = ViewModelProvider(
                    this,
                    MediaFectory(musicRepository)
                ).get(MusicViewModel::class.java)
                musicViewModel?.getAudios()
                recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
                musicViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
                    recyclerView?.adapter = MusicAdapter(requireContext(), it, this)
                })

            } catch (e: java.lang.Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("ooo", e.message.toString())
            }
        }
        else{
            recyclerView = binding?.recyclerview
            val db = MusicDatabase.getDatabase(requireContext())
            try {
                val musicRepository = MusicRepository(requireContext(), db)
                musicViewModel = ViewModelProvider(
                    this,
                    MediaFectory(musicRepository)
                ).get(MusicViewModel::class.java)
                musicViewModel?.getAudios()
                recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
                musicViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
                    recyclerView?.adapter = MusicAdapter(requireContext(), it, this)
                })

            } catch (e: java.lang.Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("ooo", e.message.toString())
            }
        }


        return binding?.root
    }

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    //check permission
//    private fun requestPermissions() {
//        val permissions = arrayOf(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(permissions, PERMISSION_REQUEST_CODE)
//        }
//    }


    override fun onAudioItemClick(position: Int, music: Music) {
        try {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle("Delete Alert")
                .setMessage("Are you sure you want to delete:${music.title}?")
                .setPositiveButton("OK") { dialog, which ->
                    try {
                        musicViewModel?.deleteMusic(music)
                    }catch (e:Exception){
                        Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                    }
//                    val filePath = music.path.toString()
//                    val isDeleted = deleteFileFromStorage2(filePath)
//                    if (isDeleted) {
//                        Toast.makeText(requireContext(), "${music.title} is Deleted", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "Error Occurred", Toast.LENGTH_SHORT).show()
//                    }
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Do something when the Cancel button is clicked
                }
                .show()

            // Toast.makeText(requireContext(), "I'm click", Toast.LENGTH_SHORT).show()
//            val bundle=Bundle()
//            bundle.putString("title",music.title)
//            bundle.putString("album",music.album)
//            bundle.putString("duration",music.duration.toString())
//            bundle.putString("path",music.path)
//            bundle.putString("position",position.toString())
//            findNavController().navigate(R.id.playerFragment,bundle)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
//    private suspend fun performDeleteImage(music: Music) {
//        withContext(Dispatchers.IO) {
//            try {
//                getApplication<Application>().contentResolver.delete(
//                    music.path,
//                    "${MediaStore.Images.Media._ID} = ?",
//                    arrayOf(music.id.toString()
//                    )
//                )
//            } catch (e:Exception) {
//                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


    override fun onAudioItemLongClick(position: Int, music: Music) {
       // Toast.makeText(requireContext(), "${music.title}", Toast.LENGTH_SHORT).show()
        try {
            val path=music.path
            val share = Intent(Intent.ACTION_SEND)
            share.type = "audio/*"
//                        val path = imagesUri?.let { it1 -> File(it1).absolutePath }
            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
            startActivity(Intent.createChooser(share, "Share via"))
            //                       Toast.makeText(applicationContext, "Share is not implemented", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAudioFavImageClick(position: Int, music: Music) {
        try {
            try {
                val title = music.title
                val album = music.album
                val duration = music.duration
                val path = music.path
                val d = formatDuration(duration)
                val musicUri=music.contentUri
                val musicId=music.musicId
                Log.d("mmm", "title:" + title)
                Log.d("mmm", "album:" + album)
                Log.d("mmm", "duration:" + duration)
                Log.d("mmm", "path:" + path)
                musicViewModel?.insertMusic(title, album, duration, path,musicUri.toUri(),musicId)
                if (musicViewModel == null) {
                    Toast.makeText(requireContext(), "Empty", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Successful", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }catch (e:Exception){
            Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if (id==R.id.linear){
            recyclerView = binding?.recyclerview
            val db = MusicDatabase.getDatabase(requireContext())
            try {
                val musicRepository = MusicRepository(requireContext(), db)
                musicViewModel = ViewModelProvider(
                    this,
                    MediaFectory(musicRepository)
                ).get(MusicViewModel::class.java)
                musicViewModel?.getAudios()
                recyclerView?.layoutManager = LinearLayoutManager(requireContext())
                musicViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
                    recyclerView?.adapter = MusicAdapter(requireContext(), it, this)
                })
                val name="linear"
                val myPreference= MyPreference(requireContext())
                myPreference.putString("name",name)

            } catch (e: java.lang.Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("ooo", e.message.toString())
            }
        }
        if (id==R.id.grid){
            recyclerView = binding?.recyclerview
            val db = MusicDatabase.getDatabase(requireContext())
            try {
                val musicRepository = MusicRepository(requireContext(), db)
                musicViewModel = ViewModelProvider(
                    this,
                    MediaFectory(musicRepository)
                ).get(MusicViewModel::class.java)
                musicViewModel?.getAudios()
                recyclerView?.layoutManager = GridLayoutManager(requireContext(), 2)
                musicViewModel?.getAudioLiveData()?.observe(requireActivity(), Observer {
                    recyclerView?.adapter = MusicAdapter(requireContext(), it, this)
                })
                val name="grid"
                val myPreference= MyPreference(requireContext())
                myPreference.putString("name",name)

            } catch (e: java.lang.Exception) {
                Toast.makeText(requireContext(), "${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("ooo", e.message.toString())
            }
        }

        return true
    }
}