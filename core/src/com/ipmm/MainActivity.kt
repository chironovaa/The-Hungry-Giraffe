package com.ipmm

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont

class MainActivity(iActivity: AndroidActivity) : Game() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var font: BitmapFont
    internal var Points: Int = 0
    internal var direction: Int = 0
    var winLevel: Int = 0
    var activity = iActivity

    override fun create() {
        Points = activity.getProperty("points").toInt()
        winLevel = 0
        direction = 0
        batch = SpriteBatch()
        // libGDX по умолчанию использует Arial шрифт.
        font = BitmapFont()
        this.setScreen(MainMenuScreen(this, activity))
    }

    override fun render() {
        super.render() // важно!
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}