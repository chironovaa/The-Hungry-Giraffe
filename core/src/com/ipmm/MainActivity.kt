package com.ipmm

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.BitmapFont

class MainActivity : Game() {
    internal lateinit var batch: SpriteBatch
    internal lateinit var font: BitmapFont
    var Points: Int = 0

    override fun create() {
        Points = 0
        batch = SpriteBatch()
        // libGDX по умолчанию использует Arial шрифт.
        font = BitmapFont()
        this.setScreen(MainMenuScreen(this))
    }

    override fun render() {
        super.render() // важно!
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
    }
}