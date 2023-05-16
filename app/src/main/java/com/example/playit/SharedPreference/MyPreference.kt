package com.example.playit.SharedPreference

import android.content.Context
import android.content.SharedPreferences

class MyPreference(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("Data",Context.MODE_PRIVATE)


    fun putString(key:String,value:String){
        sharedPreferences.edit().putString(key,value).apply()
    }

    fun getString(key: String,value: String):String?{
        return sharedPreferences.getString(key,value)
    }
}