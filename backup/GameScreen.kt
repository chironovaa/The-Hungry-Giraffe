package com.ipmm

import com.badlogic.gdx.*
import com.badlogic.gdx.Gdx.app
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import java.lang.Math.abs


/**
 * Created by Oleg on 11.03.2018.
 */

class GameScreen(internal val game: MainActivity, internal val level : Int, internal val swap : Boolean) : Screen, InputProcessor {

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
    internal lateinit var textNeck: Texture

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
	 internal var speed_x: Int = 0
    internal var speed_y: Int = 0

    enum class State() {
        PAUSE, RUNNING, LOSE, WIN
    }

    internal var state = State.RUNNING

    val apples: Array<Array<Int>> = Array(15, { Array(15, {0}) }) //временный массив яблок
    val blocks: Array<IntArray> = Array(15, { IntArray(15) })
    var neckX: MutableList<Float> = mutableListOf() //потом переделать в point, чтобы был один массив
    var neckY: MutableList<Float> = mutableListOf();


    init {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true); //нужно для перехвата механической кнопки назад
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())


        for((r, row) in blocks.withIndex()) {
            for (c in row.indices) {
                blocks[r][c] = 0
            }
        }

        //рамка по краям
        /*for((r, row) in blocks.withIndex()){
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
                    blocks[r][c] = 4

            }
        }*/

        when(level){
            0 -> {

//временное решение, потом заменить на адекватное
                apples[0][7] = 1 //положим одно яблоко
                apples[1][12] = 1 //еще одно для проверки
                apples[2][0] = 1
                apples[2][3] = 1
                apples[3][8] = 1
                apples[4][14] = 1
                apples[4][11] = 1
                apples[7][0] = 1
                apples[7][10] = 1
                apples[8][8] = 1
                apples[9][2] = 1
                apples[11][13] = 1
                apples[11][9] = 1
                apples[12][0] = 1
                apples[12][5] = 1
                apples[13][10] = 1
                apples[14][7] = 1

                val col0: IntArray = intArrayOf(3, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0)
                val col1: IntArray = intArrayOf(5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col2: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col3: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col4: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col5: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col6: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col7: IntArray = intArrayOf(6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col8: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col9: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col10: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col11: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col12: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col13: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 2, 0)
                val col14: IntArray = intArrayOf(3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 6, 2, 2, 7)
                blocks[0] = col0
                blocks[1] = col1
                blocks[2] = col2
                blocks[3] = col3
                blocks[4] = col4
                blocks[5] = col5
                blocks[6] = col6
                blocks[7] = col7
                blocks[8] = col8
                blocks[9] = col9
                blocks[10] = col10
                blocks[11] = col11
                blocks[12] = col12
                blocks[13] = col13
                blocks[14] = col14

            }
            1 -> {

                //временное решение, потом заменить на адекватное
                apples[0][7] = 1 //положим одно яблоко
                apples[1][12] = 1 //еще одно для проверки
                apples[2][0] = 1
                apples[2][3] = 1
                apples[3][8] = 1
                apples[4][14] = 1
                apples[4][11] = 1
                apples[7][0] = 1
                apples[7][10] = 1
                apples[8][8] = 1
                apples[9][2] = 1
                apples[11][13] = 1
                apples[11][9] = 1
                apples[12][0] = 1
                apples[12][5] = 1
                apples[13][10] = 1
                apples[14][7] = 1

                val col0: IntArray = intArrayOf(0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2)
                val col1: IntArray = intArrayOf(5, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3)
                val col2: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col3: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col4: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col5: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col6: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col7: IntArray = intArrayOf(6, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col8: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col9: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col10: IntArray = intArrayOf(3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col11: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col12: IntArray = intArrayOf(3, 4, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3)
                val col13: IntArray = intArrayOf(3, 4, 0, 6, 2, 2, 2, 2, 2, 2, 2, 2, 3, 0, 3)
                val col14: IntArray = intArrayOf(3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 7)
                blocks[0] = col0
                blocks[1] = col1
                blocks[2] = col2
                blocks[3] = col3
                blocks[4] = col4
                blocks[5] = col5
                blocks[6] = col6
                blocks[7] = col7
                blocks[8] = col8
                blocks[9] = col9
                blocks[10] = col10
                blocks[11] = col11
                blocks[12] = col12
                blocks[13] = col13
                blocks[14] = col14

            }
            2 -> {

                //временное решение, потом заменить на адекватное
                apples[0][7] = 1 //положим одно яблоко
                apples[1][12] = 1 //еще одно для проверки
                apples[2][0] = 1
                apples[2][3] = 1
                apples[3][8] = 1
                apples[4][14] = 1
                apples[4][11] = 1
                apples[7][0] = 1
                apples[7][10] = 1
                apples[8][8] = 1
                apples[9][2] = 1
                apples[11][13] = 1
                apples[11][9] = 1
                apples[12][0] = 1
                apples[12][5] = 1
                apples[13][10] = 1
                apples[14][7] = 1

                val col0: IntArray = intArrayOf(0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 8)
                val col1: IntArray = intArrayOf(3, 2, 2, 2, 0, 2, 0, 2, 2, 2, 2, 2, 2, 2, 0)
                val col2: IntArray = intArrayOf(3, 2, 0, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 8, 5)
                val col3: IntArray = intArrayOf(6, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
                val col4: IntArray = intArrayOf(3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
                val col5: IntArray = intArrayOf(3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
                val col6: IntArray = intArrayOf(3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7)
                val col7: IntArray = intArrayOf(6, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 8)
                val col8: IntArray = intArrayOf(3, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2)
                val col9: IntArray = intArrayOf(3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col10: IntArray = intArrayOf(3, 4, 0, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0)
                val col11: IntArray = intArrayOf(3, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col12: IntArray = intArrayOf(3, 4, 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0)
                val col13: IntArray = intArrayOf(3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                val col14: IntArray = intArrayOf(3, 6, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,70)
                blocks[0] = col0
                blocks[1] = col1
                blocks[2] = col2
                blocks[3] = col3
                blocks[4] = col4
                blocks[5] = col5
                blocks[6] = col6
                blocks[7] = col7
                blocks[8] = col8
                blocks[9] = col9
                blocks[10] = col10
                blocks[11] = col11
                blocks[12] = col12
                blocks[13] = col13
                blocks[14] = col14

            }
            else -> {

            }
        }

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
        textNeck = Texture("neck.png")
    }

    fun createRectangles(){
        rectUpSticker = Rectangle(510f, 200f, 100f, 100f)
        rectLeftSticker = Rectangle(410f, 100f, 100f, 100f)
        rectRightSticker = Rectangle(610f, 100f, 100f, 100f)
        rectDownSticker = Rectangle(510f, 0f, 100f, 100f)
        rectGiraffeHead = Rectangle(5f, 490f, 30f, 30f)
        rectBackButton = Rectangle(0f, height - 100f, 100f, 100f)
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
        if (!swap) { // Приявзка к настрйкам
                        game.batch.draw(textUpSticker, rectUpSticker.x, rectUpSticker.y, rectUpSticker.width, rectUpSticker.height)
                        game.batch.draw(textLeftSticker, rectLeftSticker.x, rectLeftSticker.y, rectLeftSticker.width, rectLeftSticker.height)
                        game.batch.draw(textRightSticker, rectRightSticker.x, rectRightSticker.y, rectRightSticker.width, rectRightSticker.height)
                        game.batch.draw(textDownSticker, rectDownSticker.x, rectDownSticker.y, rectDownSticker.width, rectDownSticker.height)
                   }

        game.batch.draw(textGiraffeHead, rectGiraffeHead.x, rectGiraffeHead.y, rectGiraffeHead.width, rectGiraffeHead.height)
        updateNeck()
        drawNeck()
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
                //if(points == 2){
                    //state = State.WIN
                //}
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
                        game.batch.draw(textWall0000, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    1 -> {
                        game.batch.draw(textWall0001, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    2 -> {
                        game.batch.draw(textWall0100, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    3 -> {
                        game.batch.draw(textWall0010, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    4 -> {
                        game.batch.draw(textWall1000, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    5 -> {
                        game.batch.draw(textWall0011, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    6 -> {
                        game.batch.draw(textWall0110, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    7 -> {
                        game.batch.draw(textWall1001, 48f * r, 48f * c + 480f, 48f, 48f)
                    }
                    8 -> {
                        game.batch.draw(textWall1100, 48f * r, 48f * c + 480f, 48f, 48f)
                    }

                }
            }
        }
    }

    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец
        val SPEED = 5f

        if(swap) {
            if (Gdx.input.isTouched() && Gdx.input.getDeltaX() > 50) {
                speed_x = 3
                speed_y = 0
            }
            if (Gdx.input.isTouched() && Gdx.input.getDeltaX() < -50) {
                speed_x = -3
                speed_y = 0
            }
            if (Gdx.input.isTouched() && Gdx.input.getDeltaY() > 50) {
                speed_x = 0
                speed_y = -3
            }
            if (Gdx.input.isTouched() && Gdx.input.getDeltaY() < -50) {
                speed_x = 0
                speed_y = 3
            }
        }
        if(Gdx.input.isTouched(0)) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
            camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
            if(state == State.RUNNING){
                if (rectUpSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.y + SPEED + rectGiraffeHead.height < height) {
                        if(!swap) {
                            speed_x = 0
                            speed_y = 3
                        }
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectDownSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.y - SPEED > 480f) {
                        if(!swap) {
                            speed_x = 0
                            speed_y = -3
                        }
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectLeftSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.x - SPEED > 0f) {
                        if(!swap) {
                            speed_x = -3
                            speed_y = 0
                        }
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectRightSticker.contains(touchPos.x, touchPos.y)) {
                    if(rectGiraffeHead.x + SPEED + rectGiraffeHead.width < width) {
                        if(!swap) {
                            speed_x = 3
                            speed_y = 0
                        }
                    } else {
                        state = State.LOSE
                    }
                }
                if (rectBackButton.contains(touchPos.x, touchPos.y)) {
                    game.screen = MainMenuScreen(game)
                    Gdx.input.setInputProcessor(null)
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
                        Gdx.input.setInputProcessor(null)
                    }
                }
                State.WIN -> {
                    if (rectOkButton.contains(touchPos.x, touchPos.y)) {
                        game.screen = MainMenuScreen(game)
                        Gdx.input.setInputProcessor(null)
                    }
                }
                else -> {

                }
            }
        }
        if(state == State.RUNNING) {
            rectGiraffeHead.setY(rectGiraffeHead.y + speed_y)
            rectGiraffeHead.setX(rectGiraffeHead.x + speed_x)
        }
    }

    fun drawBonuses(){
        val SIZE_APPLE = 100f
        for((r, row) in apples.withIndex()){
            for(c in row.indices) {
                if (apples[r][c] == 1){
                    game.batch.draw(textApple, r * 48f, c * 48f  + 480f, 48f, 48f)
                }
            }
        }
    }

    private fun pickup() {
        apples.withIndex().forEach { (r, row) ->
            for((c, cell) in row.withIndex()) if (cell == 1) {
                val touchPos = Vector3()
                touchPos.set(r * 48f + 24f, c * 48f + 24f + 480f, 0f)
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
                val touchPos3 = Vector3(-100f, -100f, -100f)
                val touchPos4 = Vector3(-100f, -100f, -100f)
                if(blocks[r][c] == 1) {
                    touchPos.set(r * 48f, c * 48f + 12f + 480f, 0f)
                    touchPos3.set(r * 48f, c * 48f + 32f + 480f, 0f)
                }
                if(blocks[r][c] == 2) {
                    touchPos.set(r * 48f + 48f, c * 48f + 12f + 480f, 0f)
                    touchPos3.set(r * 48f + 48f, c * 48f + 32f + 480f, 0f)
                }
                if(blocks[r][c] == 3) {
                    touchPos.set(r * 48f + 12f, c * 48f + 480f, 0f)
                    touchPos3.set(r * 48f + 32f, c * 48f + 480f, 0f)
                }
                if(blocks[r][c] == 4) {
                    touchPos.set(r * 48f + 12f, c * 48f + 48f + 480f, 0f)
                    touchPos3.set(r * 48f + 32f, c * 48f + 48f + 480f, 0f)
                }
                if(blocks[r][c] == 5) {
                    touchPos.set(r * 48f + 12f, c * 48f + 480f, 0f)
                    touchPos2.set(r * 48f, c * 48f + 12f + 480f, 0f)
                    touchPos3.set(r * 48f, c * 48f + 32f + 480f, 0f)
                    touchPos4.set(r * 48f + 32f, c * 48f + 480f, 0f)
                }
                if(blocks[r][c] == 6) {
                    touchPos.set(r * 48f + 48f, c * 48f + 12f + 480f, 0f)
                    touchPos2.set(r * 48f + 12f, c * 48f + 480f, 0f)
                    touchPos3.set(r * 48f + 48f, c * 48f + 32f + 480f, 0f)
                    touchPos4.set(r * 48f + 32f, c * 48f + 480f, 0f)
                }
                if(blocks[r][c] == 7) {
                    touchPos.set(r * 48f + 12f, c * 48f + 48f + 480f, 0f)
                    touchPos2.set(r * 48f, c * 48f + 12f + 480f, 0f)
                    touchPos3.set(r * 48f + 32f, c * 48f + 48f + 480f, 0f)
                    touchPos4.set(r * 48f, c * 48f + 32f + 480f, 0f)
                }
                if(blocks[r][c] == 8) {
                    touchPos.set(r * 48f + 48f, c * 48f + 12f + 480f, 0f)
                    touchPos2.set(r * 48f + 12f, c * 48f + 48f + 480f, 0f)
                    touchPos3.set(r * 48f + 48f, c * 48f + 32f + 480f, 0f)
                    touchPos4.set(r * 48f + 32f, c * 48f + 48f + 480f, 0f)
                }
                if (rectGiraffeHead.contains(touchPos.x, touchPos.y) || rectGiraffeHead.contains(touchPos2.x, touchPos2.y)
                        || rectGiraffeHead.contains(touchPos3.x, touchPos3.y) || rectGiraffeHead.contains(touchPos4.x, touchPos4.y)) {
                    state = State.LOSE
                }
                if (rectGiraffeHead.contains(720f, 1200f) || rectGiraffeHead.contains(720f, 1200f - 12f) || rectGiraffeHead.contains(720f, 1200f - 12f - 24f)
                        || rectGiraffeHead.contains(720f, 1200f - 12f - 24f - 24f)) {
                    state = State.WIN
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

    fun updateNeck(){
        val n = neckX.size
        if (n <= 2){
            neckX.add(rectGiraffeHead.x)
            neckY.add(rectGiraffeHead.y)
        }
        else{
            if (neckX.get(n-1) == rectGiraffeHead.x){
                if (neckX.get(n - 2) == rectGiraffeHead.x){
                    neckY.set(n - 1, rectGiraffeHead.y)
                }
                else{
                    neckX.add(rectGiraffeHead.x)
                    neckY.add(rectGiraffeHead.y)
                }
            }
            else{
                if ((neckY.get(n-1) == rectGiraffeHead.y)&&(neckX.get(n-1) != rectGiraffeHead.x)){
                    if (neckY.get(n - 2) == rectGiraffeHead.y){
                        neckX.set(n - 1, rectGiraffeHead.x)
                    }
                    else{
                        neckX.add(rectGiraffeHead.x)
                        neckY.add(rectGiraffeHead.y)
                    }
                }
            }
        }

    }

    fun drawNeck() {
        val size = neckX.size
        var i = 0
        while (i < size - 1) {
            val w = abs(neckX.get(i) - neckX.get(i + 1)) + rectGiraffeHead.width / 2 - 40.0f
            val h = abs(neckY.get(i) - neckY.get(i + 1)) + rectGiraffeHead.height / 2 - 40.0f
            val x = (neckX.get(i) + neckX.get(i + 1)) / 2 + rectGiraffeHead.width / 2 - w / 2
            val y = (neckY.get(i) + neckY.get(i + 1)) / 2 + rectGiraffeHead.height / 2 - h / 2
            game.batch.draw(textNeck, x, y, w, h)
            i++
        }
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
