package com.example.playit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.playit.Adapter.ViewPagerAdapter
import com.example.playit.Fragment.FavoriteFragment
import com.example.playit.Fragment.HomeFragment
import com.example.playit.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    private var binding:ActivityMain2Binding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding?.root)

        val adapter= ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(),"Home")
        adapter.addFragment(FavoriteFragment(),"Book Mark")
        binding!!.viewpager.adapter=adapter
        binding!!.tablayout.setupWithViewPager(binding!!.viewpager)
    }
}