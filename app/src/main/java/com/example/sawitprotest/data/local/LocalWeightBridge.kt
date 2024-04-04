package com.example.sawitprotest.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.sawitprotest.MyApp
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface LocalWeightBridgeApi {
    fun setEntryList(entryList: List<BridgeWeightEntryItem>)
    fun getEntryList(): List<BridgeWeightEntryItem>
}

class LocalWeightBridgeApiImpl(
    context: Context = MyApp.applicationContext(),
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE),
    private val gson : Gson = Gson()
) : LocalWeightBridgeApi {


    override fun setEntryList(entryList: List<BridgeWeightEntryItem>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(entryList)
        editor.putString("entryList", json)
        editor.apply()
    }

    override fun getEntryList(): List<BridgeWeightEntryItem> {
        val json = sharedPreferences.getString("entryList", null)
        val type = object : TypeToken<List<BridgeWeightEntryItem>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}