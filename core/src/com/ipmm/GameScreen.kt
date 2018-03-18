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

class GameScreen(internal val game: MainActivity) : Screen, InputProcessor {

    internal var camera: OrthographicCamera
    internal var width = 720
    internal var height = 1200
    internal lateinit var textWall: Texture
    internal lateinit var textUpSticker: Texture
    internal lateinit var textLeftSticker: Texture
    internal lateinit var textRightSticker: Texture
    internal lateinit var textDownSticker: Texture
    internal lateinit var rectUpSticker: Rectangle
    internal lateinit var rectLeftSticker: Rectangle
    internal lateinit var rectRightSticker: Rectangle
    internal lateinit var rectDownSticker: Rectangle
    internal lateinit var textGiraffeHead : Texture
    internal lateinit var rectGiraffeHead : Rectangle


    init {
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

        loadTextures()
        createRectangles()

    }

    fun loadTextures(){
        textWall = Texture("game.png")
        textUpSticker = Texture("upsticker.png")
        textLeftSticker = Texture("leftsticker.png")
        textRightSticker = Texture("rightsticker.png")
        textDownSticker = Texture("downsticker.png")
        textGiraffeHead = Texture("giraffe.png")
    }

    fun createRectangles(){
        rectUpSticker = Rectangle(510f, 200f, 100f, 100f)
        rectLeftSticker = Rectangle(410f, 100f, 100f, 100f)
        rectRightSticker = Rectangle(610f, 100f, 100f, 100f)
        rectDownSticker = Rectangle(510f, 0f, 100f, 100f)
        rectGiraffeHead = Rectangle(0f, 0f, 100f, 100f)
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
        game.batch.draw(textUpSticker, rectUpSticker.x, rectUpSticker.y, rectUpSticker.width, rectUpSticker.height)
        game.batch.draw(textLeftSticker, rectLeftSticker.x, rectLeftSticker.y, rectLeftSticker.width, rectLeftSticker.height)
        game.batch.draw(textRightSticker, rectRightSticker.x, rectRightSticker.y, rectRightSticker.width, rectRightSticker.height)
        game.batch.draw(textDownSticker, rectDownSticker.x, rectDownSticker.y, rectDownSticker.width, rectDownSticker.height)
        game.batch.draw(textGiraffeHead, rectGiraffeHead.x, rectGiraffeHead.y, rectGiraffeHead.width, rectGiraffeHead.height)
        game.batch.end()
        control();
        camera.update()




    }

    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец
        var SPEED = 5f;
        if(Gdx.input.isTouched(0)) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
            camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
            if (rectUpSticker.contains(touchPos.x, touchPos.y)) {
                if(rectGiraffeHead.y + SPEED + rectGiraffeHead.height < height) {
                    rectGiraffeHead.setY(rectGiraffeHead.y + SPEED)
                }
            }
            if (rectDownSticker.contains(touchPos.x, touchPos.y)) {
                if(rectGiraffeHead.y - SPEED > 0) {
                    rectGiraffeHead.setY(rectGiraffeHead.y - SPEED)
                }
            }
            if (rectLeftSticker.contains(touchPos.x, touchPos.y)) {
                if(rectGiraffeHead.x - SPEED > 0) {
                    rectGiraffeHead.setX(rectGiraffeHead.x - SPEED)
                }
            }
            if (rectRightSticker.contains(touchPos.x, touchPos.y)) {
                if(rectGiraffeHead.x + SPEED + rectGiraffeHead.width < width) {
                    rectGiraffeHead.setX(rectGiraffeHead.x + SPEED)
                }
            }
        }
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
