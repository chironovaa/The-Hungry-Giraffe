package com.ipmm

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

class AboutScreen(internal val game: MainActivity) : Screen, InputProcessor {
    private var camera: OrthographicCamera = OrthographicCamera()
    private var width = 720
    private var height = 1200
    private lateinit var textWall: Texture
    private lateinit var textBackButton: Texture
    private lateinit var rectBackButton : Rectangle

    init {
        /*заглушка, просто показывает картинку*/
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

        loadTextures()
        createRectangles()
    }

    override fun show() = Unit

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(247f, 247f, 245f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.batch.begin()
        game.batch.draw(textWall, 0f, 0f, width.toFloat(), height.toFloat())
        game.batch.draw(textBackButton, rectBackButton.x, rectBackButton.y, rectBackButton.width, rectBackButton.height)
        game.batch.end()
        control()
        camera.update()
    }

    private fun loadTextures(){
        textWall = Texture("about.png")
        textBackButton = Texture("back-icon.png")
    }

    private fun createRectangles(){
        rectBackButton = Rectangle(100f, height - 100f, 100f, 100f)
    }

    private fun control(){ // copy of OptionScreen's control
        var SPEED = 5f;
        if(Gdx.input.isTouched(0)) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
            camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
            if (rectBackButton.contains(touchPos.x, touchPos.y)) {
                game.screen = MainMenuScreen(game)
            }
        }
    }

    override fun keyDown(keycode: Int) = false

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun keyTyped(character: Char) = false

    override fun scrolled(amount: Int) = false

    override fun keyUp(keycode: Int) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun resize(width: Int, height: Int) = Unit

    override fun pause() = Unit

    override fun resume() = Unit

    override fun hide() = Unit

    override fun dispose() = Unit

}