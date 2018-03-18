package com.ipmm

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture

/**
 * Created by Oleg on 11.03.2018.
 */

class OptionScreen(internal val game: MainActivity) : Screen, InputProcessor {
    internal var camera: OrthographicCamera
    internal var width = 720
    internal var height = 1200
    internal var textWall: Texture

    init {
        /*заглушка, просто показывает картинку*/
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

        textWall = Texture("options.png")

    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
        Gdx.gl.glClearColor(247f, 247f, 245f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.batch.begin()
        game.batch.draw(textWall, 0f, 0f, width.toFloat(), height.toFloat())
        game.batch.end()

        camera.update()
    }

    override fun keyDown(keycode: Int): Boolean {
        if(keycode == Input.Keys.BACK){
            game.screen = MainMenuScreen(game)
        }
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
