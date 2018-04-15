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
    internal lateinit var textWin: Texture
    internal lateinit var textLose: Texture
    internal lateinit var textWall0000: Texture
    internal lateinit var textWall0001: Texture
    internal lateinit var textWall0010: Texture
    internal lateinit var textWall0011: Texture
    internal lateinit var textWall0100: Texture
    internal lateinit var textWall0110: Texture
    internal lateinit var textWall0111: Texture
    internal lateinit var textWall1000: Texture
    internal lateinit var textWall1001: Texture
    internal lateinit var textWall1011: Texture
    internal lateinit var textWall1100: Texture
    internal lateinit var textWall1101: Texture
    internal lateinit var textWall1110: Texture

    internal lateinit var rectUpSticker: Rectangle
    internal lateinit var rectLeftSticker: Rectangle
    internal lateinit var rectRightSticker: Rectangle
    internal lateinit var rectDownSticker: Rectangle
    internal lateinit var rectGiraffeHead : Rectangle
    internal lateinit var rectBackButton : Rectangle
    internal lateinit var rectQuitButton : Rectangle
    internal lateinit var rectResumeButton : Rectangle
    internal lateinit var rectOkButton : Rectangle

    internal var points: Int = 0

    enum class State() {
        PAUSE, RUNNING, LOSE, WIN
    }

    internal var state = State.RUNNING

    val apples: Array<Array<Int>> = Array(10, { Array(10, {0}) }) //временный массив яблок
    val blocks: Array<Array<Int>> = Array(10, { Array(10, {0}) })


    init {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true); //нужно для перехвата механической кнопки назад
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

        apples[0][5] = 1 //положим одно яблоко
        apples[4][5] = 1 //еще одно для проверки

        for((r, row) in blocks.withIndex()) {
            for (c in row.indices) {
                blocks[r][c] = 0
            }
        }

        for((r, row) in blocks.withIndex()){
            for(c in row.indices) {
                if(r == 0)
                    blocks[r][c] = 1
                if(r == 9)
                    blocks[r][c] = 2
                if(c == 0)
                    blocks[r][c] = 3
                if(c == 9)
                    blocks[r][c] = 4
                if((c == 0) && (r == 0))
                    blocks[r][c] = 5
                if((c == 0) && (r == 9))
                    blocks[r][c] = 6
                if((c == 9) && (r == 0))
                    blocks[r][c] = 7
                if((c == 9) && (r == 9))
                    blocks[r][c] = 8

            }
        }

        blocks[5][2] = 4
        blocks[3][2] = 8
        blocks[1][0] = 1
        blocks[1][1] = 1
        blocks[1][2] = 1
        blocks[1][3] = 1

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
        textWin = Texture("win.png")
        textLose = Texture("lose.png")
        textWall0000 = Texture("walls\\0000.png")
        textWall0001 = Texture("walls\\0001.png")
        textWall0010 = Texture("walls\\0010.png")
        textWall0011 = Texture("walls\\0011.png")
        textWall0100 = Texture("walls\\0100.png")
        textWall0110 = Texture("walls\\0110.png")
        textWall0111 = Texture("walls\\0111.png")
        textWall1000 = Texture("walls\\1000.png")
        textWall1001 = Texture("walls\\1001.png")
        textWall1011 = Texture("walls\\1011.png")
        textWall1100 = Texture("walls\\1100.png")
        textWall1101 = Texture("walls\\1101.png")
        textWall1110 = Texture("walls\\1110.png")
    }

    fun createRectangles(){
        rectUpSticker = Rectangle(510f, 200f, 100f, 100f)
        rectLeftSticker = Rectangle(410f, 100f, 100f, 100f)
        rectRightSticker = Rectangle(610f, 100f, 100f, 100f)
        rectDownSticker = Rectangle(510f, 0f, 100f, 100f)
        rectGiraffeHead = Rectangle(10f, 10f, 60f, 100f)
        rectBackButton = Rectangle(100f, height - 100f, 100f, 100f)
        rectQuitButton = Rectangle(260f, 500f, 100f, 100f)
        rectResumeButton = Rectangle(260f + 100f, 500f, 100f, 100f)
        rectOkButton = Rectangle(260f + 50f, 500f, 100f, 100f)
    }

    override fun show() {

    }

    override fun render(delta: Float) {
        //Gdx.gl.glClearColor(247f, 247f, 245f, 0f)
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.batch.begin()
        game.batch.draw(textWall, 0f, 0f, width.toFloat(), height.toFloat())
        drawLab()
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
                crash()
                if(points == 2){
                    state = State.WIN
                }
            }
            State.LOSE -> {
                game.batch.begin()
                game.batch.draw(textLose, 260f, 500f, 200f, 200f)
                game.batch.end()
            }
            State.WIN -> {
                game.batch.begin()
                game.batch.draw(textWin, 260f, 500f, 200f, 200f)
                game.batch.end()
            }
            else -> {

            }
        }
        control()
        camera.update()




    }

    fun loadLab(){

    }

    fun drawLab(){
        for((r, row) in blocks.withIndex()) {
            for (c in row.indices) {
                when(blocks[r][c]){
                    0 -> {
                        game.batch.draw(textWall0000, 72f * r, 120f * c, 72f, 120f)
                    }
                    1 -> {
                        game.batch.draw(textWall0001, 72f * r, 120f * c, 72f, 120f)
                    }
                    2 -> {
                        game.batch.draw(textWall0100, 72f * r, 120f * c, 72f, 120f)
                    }
                    3 -> {
                        game.batch.draw(textWall0010, 72f * r, 120f * c, 72f, 120f)
                    }
                    4 -> {
                        game.batch.draw(textWall1000, 72f * r, 120f * c, 72f, 120f)
                    }
                    5 -> {
                        game.batch.draw(textWall0011, 72f * r, 120f * c, 72f, 120f)
                    }
                    6 -> {
                        game.batch.draw(textWall0110, 72f * r, 120f * c, 72f, 120f)
                    }
                    7 -> {
                        game.batch.draw(textWall1001, 72f * r, 120f * c, 72f, 120f)
                    }
                    8 -> {
                        game.batch.draw(textWall1100, 72f * r, 120f * c, 72f, 120f)
                    }

                }
            }
        }
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
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectDownSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.y - SPEED > 0) {
                        rectGiraffeHead.setY(rectGiraffeHead.y - SPEED)
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectLeftSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.x - SPEED > 0) {
                        rectGiraffeHead.setX(rectGiraffeHead.x - SPEED)
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectRightSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.x + SPEED + rectGiraffeHead.width < width) {
                        rectGiraffeHead.setX(rectGiraffeHead.x + SPEED)
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectBackButton.contains(touchPos.x, touchPos.y)) {
                    game.screen = MainMenuScreen(game)
                }
            }

            when(state) {
                State.PAUSE -> {
                    if (rectResumeButton.contains(touchPos.x, touchPos.y)) {
                        state = State.RUNNING
                    }
                    if (rectQuitButton.contains(touchPos.x, touchPos.y)) {
                        println("QUIT")
                        app.exit()
                    }
                }
                State.RUNNING -> {

                }
                State.LOSE -> {
                    if (rectOkButton.contains(touchPos.x, touchPos.y)) {
                        game.screen = MainMenuScreen(game)
                    }
                }
                State.WIN -> {
                    if (rectOkButton.contains(touchPos.x, touchPos.y)) {
                        game.screen = MainMenuScreen(game)
                    }
                }
                else -> {

                }
            }
        }
    }

    fun drawBonuses(){
        val SIZE_APPLE = 100f
        for((r, row) in apples.withIndex()){
            for(c in row.indices) {
                if (apples[r][c] == 1){
                    game.batch.draw(textApple, r * 72f, c * 120f, 72f, 120f)
                }
            }
        }
    }

    private fun pickup() {
        apples.withIndex().forEach { (r, row) ->
            for((c, cell) in row.withIndex()) if (cell == 1) {
                val touchPos = Vector3()
                touchPos.set(r * 72f + 36f, c * 120f + 60f, 0f)
                if (rectGiraffeHead.contains(touchPos.x, touchPos.y)){
                    apples[r][c] = 0
                    points++
                }
            }
        }
    }

    private fun crash() {
        for((r, row) in blocks.withIndex()) {
            for (c in row.indices) {
                val touchPos = Vector3(-100f, -100f, -100f)
                val touchPos2 = Vector3(-100f, -100f, -100f)
                if(blocks[r][c] == 1) {
                    touchPos.set(r * 72f, c * 120f + 60f, 0f)
                }
                if(blocks[r][c] == 2) {
                    touchPos.set(r * 72f + 72f, c * 120f + 60f, 0f)
                }
                if(blocks[r][c] == 3) {
                    touchPos.set(r * 72f + 36f, c * 120f, 0f)
                }
                if(blocks[r][c] == 4) {
                    touchPos.set(r * 72f + 36f, c * 120f + 120f, 0f)
                }
                if(blocks[r][c] == 5) {
                    touchPos.set(r * 72f + 36f, c * 120f, 0f)
                    touchPos2.set(r * 72f, c * 120f + 60f, 0f)
                }
                if(blocks[r][c] == 6) {
                    touchPos.set(r * 72f + 72f, c * 120f + 60f, 0f)
                    touchPos2.set(r * 72f + 36f, c * 120f, 0f)
                }
                if(blocks[r][c] == 7) {
                    touchPos.set(r * 72f + 36f, c * 120f + 120f, 0f)
                    touchPos2.set(r * 72f, c * 120f + 60f, 0f)
                }
                if(blocks[r][c] == 8) {
                    touchPos.set(r * 72f + 72f, c * 120f + 60f, 0f)
                    touchPos2.set(r * 72f + 36f, c * 120f + 120f, 0f)
                }
                if (rectGiraffeHead.contains(touchPos.x, touchPos.y) || rectGiraffeHead.contains(touchPos2.x, touchPos2.y)) {
                    state = State.LOSE
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
