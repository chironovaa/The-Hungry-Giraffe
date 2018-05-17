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

val NECK = 1
val HEAD = 2
val BLOCK = 3
val APPLE = 4
val EXIT = 5

enum class Button {
    UPSTICKER, LEFTSTICKER, RIGHTSTICKER, DOWNSTICKER, BACK, RESUME, QUIT, OK
}

enum class Message {
    QUITMENU, WINMEESAGE, LOSEMESSAGE
}

enum class GameTexture {
    WALL, APPLE, GIRAFFEHEAD, NECK, BLOCK, EXIT
}

class GameScreen(internal val game: MainActivity, internal val level : Int, internal val swap : Boolean) : Screen, InputProcessor {

    internal var camera: OrthographicCamera
    internal var width = 720
    internal var height = 1200

    val mapW = width //Ширина и высота лабиринта (в пикселях)
    val mapH = width //Не используются, т.к. я еще не исправила drawMap()
    val n = 15 //Высота лабиринта (в клетках)
    val m = 15 //Ширина
    var headX = 0 //координаты головы жирафа (тоже в клетках)
    var headY = 0 //Можно потом заменить на point


    internal var buttonsText: ArrayList<Texture> = ArrayList<Texture>() //массив текстур для кнопок
    internal var messagesText: ArrayList<Texture> = ArrayList<Texture>() //массив текстур для сообщений (например: вы проиграли)
    internal var gameText: ArrayList<Texture> = ArrayList<Texture>() //массив текстур для игровых ассетов
    internal var buttonsRect: ArrayList<Rectangle> = ArrayList<Rectangle>() //массив ректов для кнопок
    internal var messagesRect: ArrayList<Rectangle> = ArrayList<Rectangle>()
    internal var gameRect: ArrayList<Rectangle> = ArrayList<Rectangle>()

    internal var points: Int = 0
    internal var speed_x: Int = 0
    internal var speed_y: Int = 0
    internal var dx: Int = 0 //Сдвиг по x/y
    internal var dy: Int = 0    //После некоторого значения голова сдвигается на 1, а переменные обнуляются
                                //Не разобралась как делать задержку по времени так что пока будет так. Катя.

    enum class State() {
        PAUSE, RUNNING, LOSE, WIN
    }

    internal var state = State.RUNNING

    var map: Array<Array<Int>> = Array(n, { Array(m, {0}) }) //тут храним все (шея, голова, стенки, яблоки, выход)



    init {
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true); //нужно для перехвата механической кнопки назад
        camera = OrthographicCamera()
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
        mapCreate() //функция для конструирования карты
        loadTextures()
        createRectangles()
    }

    /**
     * Загрузка уровня.
     */
    fun mapCreate(){//функция для загрузки карты, определяем уровень, достаем из файла, записываем в map
        /*тут будет код для загруки уровней. Номер уровня лежит в переменной level*/
        map[0][0] = HEAD
        map[0][2] = APPLE
        map[7][2] = APPLE
        map[9][3] = APPLE
        map[4][4] = BLOCK
        map[4][5] = BLOCK
        map[6][8] = BLOCK
        map[2][3] = BLOCK
        map[9][2] = BLOCK
        map[14][14] = EXIT
    }

    /**
     * Загрузка текстур.
     */
    fun loadTextures(){ //важен порядок, в котором мы кладем текстуры(см. enum). потом исправим
        gameText.add(GameTexture.WALL.ordinal, Texture("game.png"))
        gameText.add(GameTexture.APPLE.ordinal, Texture("apple.png"))
        gameText.add(GameTexture.GIRAFFEHEAD.ordinal, Texture("giraffe.png"))
        gameText.add(GameTexture.NECK.ordinal, Texture("neck.png"))
        gameText.add(GameTexture.BLOCK.ordinal, Texture("block.png"))
        gameText.add(GameTexture.EXIT.ordinal, Texture("exit.png"))
        buttonsText.add(Button.UPSTICKER.ordinal, Texture("upsticker.png"))
        buttonsText.add(Button.LEFTSTICKER.ordinal, Texture("leftsticker.png"))
        buttonsText.add(Button.RIGHTSTICKER.ordinal, Texture("rightsticker.png"))
        buttonsText.add(Button.DOWNSTICKER.ordinal, Texture("downsticker.png"))
        buttonsText.add(Button.BACK.ordinal, Texture("back-icon.png"))
        messagesText.add(Message.QUITMENU.ordinal, Texture("quitmenu.png"))
        messagesText.add(Message.WINMEESAGE.ordinal, Texture("win.png"))
        messagesText.add(Message.LOSEMESSAGE.ordinal, Texture("lose.png"))

    }

    /**
     * Создание физических объектов
     */
    fun createRectangles(){
        buttonsRect.add(Button.UPSTICKER.ordinal, Rectangle(480f, 200f, 150f, 150f))
        buttonsRect.add(Button.LEFTSTICKER.ordinal, Rectangle(380f, 100f, 150f, 150f))
        buttonsRect.add(Button.RIGHTSTICKER.ordinal, Rectangle(580f, 100f, 150f, 150f))
        buttonsRect.add(Button.DOWNSTICKER.ordinal, Rectangle(480f, 0f, 150f, 150f))
        buttonsRect.add(Button.BACK.ordinal, Rectangle(0f, height - 100f, 100f, 100f))
        buttonsRect.add(Button.RESUME.ordinal, Rectangle(260f + 100f, 500f, 100f, 100f))
        buttonsRect.add(Button.QUIT.ordinal, Rectangle(260f, 500f, 100f, 100f))
        buttonsRect.add(Button.OK.ordinal, Rectangle(260f + 50f, 500f, 100f, 100f))
    }



    override fun show() {

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(247f, 247f, 245f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        game.batch.begin()

        game.batch.draw(gameText.get(GameTexture.WALL.ordinal), 0f, 0f, width.toFloat(), height.toFloat())
        updateMap()
        drawMap()
        if (!swap) { // Приявзка к настрйкам
            drawStickers()
        }
        drawButtons()
        drawText()
        drawMessage()

        game.batch.end()

        control()
        camera.update()
    }

    /**
     * Отрисовка стикеров
     */
    fun drawStickers(){
        game.batch.draw(buttonsText.get(Button.UPSTICKER.ordinal), buttonsRect.get(Button.UPSTICKER.ordinal).x, buttonsRect.get(Button.UPSTICKER.ordinal).y,
                buttonsRect.get(Button.UPSTICKER.ordinal).width, buttonsRect.get(Button.UPSTICKER.ordinal).height)
        game.batch.draw(buttonsText.get(Button.LEFTSTICKER.ordinal), buttonsRect.get(Button.LEFTSTICKER.ordinal).x, buttonsRect.get(Button.LEFTSTICKER.ordinal).y,
                buttonsRect.get(Button.LEFTSTICKER.ordinal).width, buttonsRect.get(Button.LEFTSTICKER.ordinal).height)
        game.batch.draw(buttonsText.get(Button.RIGHTSTICKER.ordinal), buttonsRect.get(Button.RIGHTSTICKER.ordinal).x, buttonsRect.get(Button.RIGHTSTICKER.ordinal).y,
                buttonsRect.get(Button.RIGHTSTICKER.ordinal).width, buttonsRect.get(Button.RIGHTSTICKER.ordinal).height)
        game.batch.draw(buttonsText.get(Button.DOWNSTICKER.ordinal), buttonsRect.get(Button.DOWNSTICKER.ordinal).x, buttonsRect.get(Button.DOWNSTICKER.ordinal).y,
                buttonsRect.get(Button.DOWNSTICKER.ordinal).width, buttonsRect.get(Button.DOWNSTICKER.ordinal).height)
    }

    /**
     * Отрисовка кнопок
     */
    fun drawButtons(){
        game.batch.draw(buttonsText.get(Button.BACK.ordinal), buttonsRect.get(Button.BACK.ordinal).x, buttonsRect.get(Button.BACK.ordinal).y,
                buttonsRect.get(Button.BACK.ordinal).width, buttonsRect.get(Button.BACK.ordinal).height)
    }

    /**
     * Отрисовка текста
     */
    fun drawText(){
        game.font.getData().setScale(2f, 2f)
        game.font.draw(game.batch, points.toString(), 620f, 1100f)
    }

    /**
     * Отрисовка сообщений
     */
    fun drawMessage(){
        when(state){
            State.PAUSE -> {
                game.batch.draw(messagesText.get(Message.QUITMENU.ordinal), 260f, 500f, 200f, 200f)
            }
            State.RUNNING -> {
                //pickup()
            }
            State.LOSE -> {
                game.batch.draw(messagesText.get(Message.LOSEMESSAGE.ordinal), 260f, 500f, 200f, 200f)
            }
            State.WIN -> {
                game.batch.draw(messagesText.get(Message.WINMEESAGE.ordinal), 260f, 500f, 200f, 200f)
            }
            else -> {
                /*nothing*/
            }
        }
    }

    /**
     * Обновление карты, проверка на столкновения и определение взаимодействия с объектами
     */
    fun updateMap(){
        val x = headX
        val y = headY
        if ((x <0)||(y <0)||(x > m-1)||(y > n-1))
            state = State.LOSE

        if (state != State.LOSE){
            when(map[x][y]){
                BLOCK -> {
                    state = State.LOSE
                }
                NECK -> {
                    state = State.LOSE
                }
                APPLE -> {
                    points++
                }
                EXIT -> {
                    state = State.WIN
                }
            }
            if ((x > 0)&&(map[x - 1][y] == HEAD)) //Меняем клетку где голова была раньше
                map[x-1][y] = NECK
            if ((x < n - 1)&&(map[x + 1][y] == HEAD))
                map[x+1][y] = NECK
            if ((y > 0)&&(map[x][y - 1] == HEAD))
                map[x][y-1] = NECK
            if ((y < m - 1)&&(map[x][y + 1] == HEAD))
                map[x][y + 1] = NECK
            map[x][y] = HEAD
        }

    }

    /**
     * Отрисовка карты
     */
    fun drawMap(){  //Нужно будет переделать, пока не квадратный лабиринт отобразится криво
        val w = width.toFloat()
        val h = height.toFloat()
        for (x in 0 until n){
            for (y in 0 until m){
                if (map[x][y] == BLOCK)
                    game.batch.draw(gameText.get(GameTexture.BLOCK.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
                if (map[x][y] == EXIT)
                    game.batch.draw(gameText.get(GameTexture.EXIT.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
                if (map[x][y] == NECK)
                    game.batch.draw(gameText.get(GameTexture.NECK.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15)+ (h-w), w/15, w/15)
                if (map[x][y] == APPLE)
                    game.batch.draw(gameText.get(GameTexture.APPLE.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15)+ (h-w), w/15, w/15)
                if (map[x][y] == HEAD)
                    game.batch.draw(gameText.get(GameTexture.GIRAFFEHEAD.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
            }
        }
    }


    /**
     * Функция для управления жирафом (свап, стикеры)
     */
    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец

        var swapSize = 50

        if(swap) {
            if (Gdx.input.isTouched() && Gdx.input.getDeltaX() > swapSize) {
                speed_x = 1
                speed_y = 0
            }
            if (Gdx.input.isTouched() && Gdx.input.getDeltaX() < -swapSize) {
                speed_x = -1
                speed_y = 0
            }
            if (Gdx.input.isTouched() && Gdx.input.getDeltaY() > swapSize) {
                speed_x = 0
                speed_y = -1
            }
            if (Gdx.input.isTouched() && Gdx.input.getDeltaY() < -swapSize) {
                speed_x = 0
                speed_y = 1
            }
        }
        if(Gdx.input.isTouched(0)) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
            camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
            if(state == State.RUNNING){
                if (buttonsRect.get(Button.UPSTICKER.ordinal).contains(touchPos.x, touchPos.y)) {
                    speed_y = 1
                    speed_x = 0
                }
                if (buttonsRect.get(Button.DOWNSTICKER.ordinal).contains(touchPos.x, touchPos.y)) {
                    speed_y = -1
                    speed_x = 0
                }
                if (buttonsRect.get(Button.LEFTSTICKER.ordinal).contains(touchPos.x, touchPos.y)) {
                    speed_y = 0
                    speed_x = -1
                }
                if (buttonsRect.get(Button.RIGHTSTICKER.ordinal).contains(touchPos.x, touchPos.y)) {
                    speed_y = 0
                    speed_x = 1
                }
                if (buttonsRect.get(Button.BACK.ordinal).contains(touchPos.x, touchPos.y)) {
                    game.screen = MainMenuScreen(game)
                    Gdx.input.setInputProcessor(null) //фикс бага с невидимыми кнопками
                }
            }

            when(state) {
                State.PAUSE -> {
                    if (buttonsRect.get(Button.RESUME.ordinal).contains(touchPos.x, touchPos.y)) {
                        state = State.RUNNING
                    }
                    if (buttonsRect.get(Button.QUIT.ordinal).contains(touchPos.x, touchPos.y)) {
                        println("QUIT")
                        app.exit()
                    }
                }
                State.RUNNING -> {

                }
                State.LOSE -> {
                    if (buttonsRect.get(Button.OK.ordinal).contains(touchPos.x, touchPos.y)) {
                        game.screen = MainMenuScreen(game)
                        Gdx.input.setInputProcessor(null)
                    }
                }
                State.WIN -> {
                    if (buttonsRect.get(Button.OK.ordinal).contains(touchPos.x, touchPos.y)) {
                        game.screen = MainMenuScreen(game)
                        Gdx.input.setInputProcessor(null)
                    }
                }
                else -> {

                }
            }
        }
        if(state == State.RUNNING) { //что такое 10?
            dy += speed_y
            dx += speed_x
            print(dx)
            println(dy)
            if (abs(dx) > 10){
                headX += dx / 10
                dx = 0
                if ((headX < 0)||(headX> m-1)) //выход за грань реальности
                    state = State.LOSE
            }
            if (abs(dy) > 10){
                headY += dy / 10
                dy = 0
                if ((headY < 0)||(headY> n-1))
                    state = State.LOSE
            }
        }
    }


    /**
     * Проверка на нажатие механических кнопок телефона
     */
    override fun keyDown(keycode: Int): Boolean {
        when (keycode){
            Input.Keys.BACK -> {
                println("Pause")
                state = State.PAUSE
                //isPause = true
            }
            Input.Keys.MENU -> {
                println("1")
            }
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
