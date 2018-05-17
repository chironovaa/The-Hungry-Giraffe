package com.ipmm

import com.badlogic.gdx.Application
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.*
import java.io.File
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import java.io.InputStream
import java.lang.Object
//import android.content.res.AssetManager


class LabConverter {
    data class Cells( val position : Array<Int>, val status : Int, val walls : Map<String, Boolean>)

    data class Size( val size : Array<Int>)

    private fun convert(jsonFile: InputStream) {
        val mapper = jacksonObjectMapper()
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true)

        val cells = mapper.readValue<Cells>(jsonFile)
        println("Conversion finished !")
    }

    fun start(path: String) {
        /*val assetManager : AssetManager = AssetManager()
        val file : InputStream = assetManager.load("lab11.json", )
        if(file != null) {
           val jsonFile: InputStream = file
           convert(jsonFile)
        }*/

        //val path = context.getFilesDir()

    }
}