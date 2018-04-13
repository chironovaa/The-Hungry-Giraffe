package com.ipmm

import com.badlogic.gdx.*
import com.badlogic.gdx.Gdx.app
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
    internal var isPause = false

    internal lateinit var textWall: Texture
    internal lateinit var textUpSticker: Texture
    internal lateinit var textLeftSticker: Texture
    internal lateinit var textRightSticker: Texture
    internal lateinit var textDownSticker: Texture
    internal lateinit var textGiraffeHead : Texture
    internal lateinit var textBackButton: Texture
    internal lateinit var textApple: Texture
    internal lateinit var textQuitMenu: Texture

    internal lateinit var rectUpSticker: Rectangle
    internal lateinit var rectLeftSticker: Rectangle
    internal lateinit var rectRightSticker: Rectangle
    internal lateinit var rectDownSticker: Rectangle
    internal lateinit var rectGiraffeHead : Rectangle
    internal lateinit var rectBackButton : Rectangle
    internal lateinit var rectQuitButton : Rectangle
    internal lateinit var rectResumeButton : Rectangle

    internal var points: Int = 0

    enum class State() {
        PAUSE, RUNNING
    }

    internal var state = State.RUNNING

    val apples: Array<Array<Int>> = Array(10, { Array(10, {0}) }) //временный массив яблок


    init {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true); //нужно для перехвата механической кнопки назад
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

        apples[0][5] = 1 //положим одно яблоко
        apples[4][5] = 1 //еще одно для проверки
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
        textBackButton = Texture("back-icon.png")
        textApple = Texture("apple.png")
        textQuitMenu = Texture("quitmenu.png")
    }

    fun createRectangles(){
        rectUpSticker = Rectangle(510f, 200f, 100f, 100f)
        rectLeftSticker = Rectangle(410f, 100f, 100f, 100f)
        rectRightSticker = Rectangle(610f, 100f, 100f, 100f)
        rectDownSticker = Rectangle(510f, 0f, 100f, 100f)
        rectGiraffeHead = Rectangle(0f, 0f, 100f, 100f)
        rectBackButton = Rectangle(100f, height - 100f, 100f, 100f)
        rectQuitButton = Rectangle(260f, 500f, 100f, 100f)
        rectResumeButton = Rectangle(260f + 100f, 500f, 100f, 100f)
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(247f, 247f, 245f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.batch.begin()
        game.batch.draw(textWall, 0f, 0f, width.toFloat(), height.toFloat())
        game.batch.draw(textUpSticker, rectUpSticker.x, rectUpSticker.y, rectUpSticker.width, rectUpSticker.height)
        game.batch.draw(textLeftSticker, rectLeftSticker.x, rectLeftSticker.y, rectLeftSticker.width, rectLeftSticker.height)
        game.batch.draw(textRightSticker, rectRightSticker.x, rectRightSticker.y, rectRightSticker.width, rectRightSticker.height)
        game.batch.draw(textDownSticker, rectDownSticker.x, rectDownSticker.y, rectDownSticker.width, rectDownSticker.height)
        game.batch.draw(textGiraffeHead, rectGiraffeHead.x, rectGiraffeHead.y, rectGiraffeHead.width, rectGiraffeHead.height)
        game.batch.draw(textBackButton, rectBackButton.x, rectBackButton.y, rectBackButton.width, rectBackButton.height)
        game.font.getData().setScale(2f, 2f)
        game.font.draw(game.batch, points.toString(), 620f, 1100f)
        drawBonuses()
        game.batch.end()
        when(state){
            State.PAUSE -> {
                game.batch.begin()
                game.batch.draw(textQuitMenu, 260f, 500f, 200f, 200f)
                game.batch.end()
            }
            State.RUNNING -> {
                pickup()
            }
            else -> {

            }
        }
        control()
        camera.update()




    }

    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец
        val SPEED = 5f
        if(Gdx.input.isTouched(0)) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
            camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
            if(state == State.RUNNING){
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
                if (rectBackButton.contains(touchPos.x, touchPos.y)) {
                    game.screen = MainMenuScreen(game)
                }
            }

            if(state == State.PAUSE){
                if (rectResumeButton.contains(touchPos.x, touchPos.y)) {
                    state = State.RUNNING
                }
                if (rectQuitButton.contains(touchPos.x, touchPos.y)) {
                   println("QUIT")
                   app.exit()
                }
            }

        }
    }

    fun drawBonuses(){
        val SIZE_APPLE = 100f
        for((r, row) in apples.withIndex()){
            for(c in row.indices) {
                if (apples[r][c] == 1){
                    game.batch.draw(textApple, r * SIZE_APPLE, c * SIZE_APPLE, SIZE_APPLE, SIZE_APPLE)
                }
            }
        }
    }

    private fun pickup() {
        apples.withIndex().forEach { (r, row) ->
            for((c, cell) in row.withIndex()) if (cell == 1) {
                val touchPos = Vector3()
                touchPos.set(r * 100f + 50f, c * 100f + 50f, 0f)
                if (rectGiraffeHead.contains(touchPos.x, touchPos.y)){
                    apples[r][c] = 0
                    points++
                }
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        when (keycode){
            Input.Keys.BACK -> {
                println("Pause")
                state = State.PAUSE
                //isPause = true
            }
            Input.Keys.MENU -> println("1")
                else -> {
                    println("Down unknown key")
                }
        }
        return true
    }

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
