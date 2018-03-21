package com.ipmm

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3

/**
 * Created by Oleg on 11.03.2018.
 */

class OptionScreen(internal val game: MainActivity) : Screen, InputProcessor {
    internal var camera: OrthographicCamera
    internal var width = 720
    internal var height = 1200
    internal lateinit var textWall: Texture
    internal lateinit var textBackButton: Texture
    internal lateinit var rectBackButton : Rectangle

    init {
        /*заглушка, просто показывает картинку*/
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

       loadTextures()
        createRectangles()

    }

    override fun show() {

    }

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

    fun loadTextures(){
        textWall = Texture("options.png")
        textBackButton = Texture("back-icon.png")
    }

    fun createRectangles(){
        rectBackButton = Rectangle(100f, height - 100f, 100f, 100f)
    }

    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец
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

    override fun keyDown(keycode: Int): Boolean {
        return false;
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false;
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false;
    }

    override fun keyTyped(character: Char): Boolean {
        return false;
    }

    override fun scrolled(amount: Int): Boolean {
        return false;
    }

    override fun keyUp(keycode: Int): Boolean {
        return false;
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        return false;
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false;
    }



    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {

    }
}
