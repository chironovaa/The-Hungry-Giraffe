package com.ipmm

interface AndroidActivity {
    fun addProperty(name: String, value: String)
    fun getProperty(name: String): String
    fun showToast(text: String)
}