package com.ipmm


import com.badlogic.gdx.*
import com.badlogic.gdx.Gdx.app
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector3
import java.awt.SystemColor.control
import java.lang.Math.abs


/**
 * Created by Oleg on 11.03.2018.
 */

//val NECK_N = 11
//val NECK_E = 12
val NECK_S = 13
val NECK_W = 14
val NECK_NS = 15
val NECK_WE = 16
val NECK_NW = 17
val NECK_NE = 18
val NECK_SW = 19
val NECK_SE = 20

val HEAD_N = 21
val HEAD_E = 22
val HEAD_S = 23
val HEAD_W = 24

val BLOCK = 3
val APPLE = 4
val EXIT = 5

val UP = 100
val DOWN = 101
val LEFT = 102
val RIGHT = 103

enum class Button {
    UPSTICKER, LEFTSTICKER, RIGHTSTICKER, DOWNSTICKER, BODY,  BACK, RESUME, QUIT, OK
}

enum class Message {
    QUITMENU, WINMEESAGE, LOSEMESSAGE
}

enum class GameTexture {
    WALL, APPLE, GIRAFFEHEAD, NECK_NS , NECK_WE , NECK_SE ,NECK_SW , NECK_NE ,NECK_NW , BLOCK_0, BLOCK_11,BLOCK_12,BLOCK_13,BLOCK_14,BLOCK_21,BLOCK_22,BLOCK_23,BLOCK_24,BLOCK_25,BLOCK_26,BLOCK_31,BLOCK_32,BLOCK_33,BLOCK_34,BLOCK_4, EXIT, HEAD_N, HEAD_W, HEAD_S, HEAD_E
}

class GameScreen(internal val game: MainActivity, internal val level : Int, internal val swap : Boolean) : Screen, InputProcessor {

    internal var camera: OrthographicCamera
    internal var width = 720
    internal var height = 1200

    val startX = 1
    val startY = 0

    val mapW = width //Ширина и высота лабиринта (в пикселях)
    val mapH = width //Не используются, т.к. я еще не исправила drawMap()
    val n = 15 //Высота лабиринта (в клетках)
    val m = 15 //Ширина
    var headX = 1 //координаты головы жирафа (тоже в клетках)
    var headY = 0 //Можно потом заменить на point
    var headS = 1.5f // размер головы
    var neckXS = 0.65f // размер шеи по отношению к высоте шеи


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
        mapCreate(level) //функция для конструирования карты
        loadTextures()
        createRectangles()
        game.direction = 0
    }

    /**
     * Загрузка уровня.
     */
    fun mapCreate(level : Int){//функция для загрузки карты, определяем уровень, достаем из файла, записываем в map
        /*тут будет код для загруки уровней. Номер уровня лежит в переменной level*/

        //общие границы, голова и выход для всех уровней.
        map[1][0] = HEAD_N
        map[14][8] = EXIT
        map[14][9] = EXIT
        map[14][10] = EXIT
        for (i in 0..11)
            map[0][i] = BLOCK
        for (i in 0..7)
            map[14][i] = BLOCK
        for(i in 1..14)
            map[i][11] = BLOCK
        for(i in 4..13)
            map[i][0] = BLOCK
        //индивидуально для каждого уровня яблоки и стены внутри области лабиринта
        when(level) {
            0 -> {
                //блоки
                for (i in 1..7)
                    map[4][i] = BLOCK
                for(i in 1..3)
                    map[7][i] = BLOCK
                for(i in 1..14)
                    map[i][11] = BLOCK
                for(i in 5..7)
                    map[i][7] = BLOCK
                for(i in 10..13)
                    map[i][4] = BLOCK
                //яблоки
                map[1][1] = APPLE
                map[1][5] = APPLE
                map[1][9] = APPLE
                map[5][9] = APPLE
                map[9][9] = APPLE
                map[13][9] = APPLE
            }
            1 -> {
                //блоки
                for(i in 1..3)
                    map[4][i] = BLOCK
                for(i in 7..11)
                    map[4][i] = BLOCK
                for(i in 7..11)
                    map[9][i] = BLOCK
                for(i in 10..13)
                    map[i][4] = BLOCK
                //яблоки
                map[1][1] = APPLE
                map[1][5] = APPLE
                map[5][5] = APPLE
                map[6][2] = APPLE
                map[7][8] = APPLE
                map[10][5] = APPLE
                map[13][9] = APPLE
            }
            2 -> {
                //блоки
                for(i in 1..7)
                    map[7][i] = BLOCK
                for(i in 6..10)
                    map[11][i] = BLOCK
                for(i in 1..3)
                    map[i][4] = BLOCK
                for(i in 4..7)
                    map[i][7] = BLOCK
                //яблоки
                map[2][2] = APPLE
                map[5][4] = APPLE
                map[2][7] = APPLE
                map[5][9] = APPLE
                map[9][3] = APPLE
                map[9][6] = APPLE
                map[12][3] = APPLE
            }
            else -> {
            }
        }
    }

    /**
     * Загрузка текстур.
     */
    fun loadTextures(){ //важен порядок, в котором мы кладем текстуры(см. enum). потом исправим
        gameText.add(GameTexture.WALL.ordinal, Texture("fon.png"))
        gameText.add(GameTexture.APPLE.ordinal, Texture("apple_red.png"))
        gameText.add(GameTexture.GIRAFFEHEAD.ordinal, Texture("giraffe.png"))
        gameText.add(GameTexture.NECK_NS.ordinal, Texture("neck_ns.png")) //вертикальная текстура шеи
        gameText.add(GameTexture.NECK_WE.ordinal, Texture("neck_we.png"))
        gameText.add(GameTexture.NECK_SE.ordinal, Texture("neck_se.png"))
        gameText.add(GameTexture.NECK_SW.ordinal, Texture("neck_sw.png"))
        gameText.add(GameTexture.NECK_NE.ordinal, Texture("neck_ne.png"))
        gameText.add(GameTexture.NECK_NW.ordinal, Texture("neck_nw.png"))

        gameText.add(GameTexture.BLOCK_0.ordinal, Texture("block.png"))
        gameText.add(GameTexture.BLOCK_11.ordinal, Texture("block_11.png"))
        gameText.add(GameTexture.BLOCK_12.ordinal, Texture("block_12.png"))
        gameText.add(GameTexture.BLOCK_13.ordinal, Texture("block_13.png"))
        gameText.add(GameTexture.BLOCK_14.ordinal, Texture("block_14.png"))
        gameText.add(GameTexture.BLOCK_21.ordinal, Texture("block_21.png"))
        gameText.add(GameTexture.BLOCK_22.ordinal, Texture("block_22.png"))
        gameText.add(GameTexture.BLOCK_23.ordinal, Texture("block_23.png"))
        gameText.add(GameTexture.BLOCK_24.ordinal, Texture("block_24.png"))
        gameText.add(GameTexture.BLOCK_25.ordinal, Texture("block_25.png"))
        gameText.add(GameTexture.BLOCK_26.ordinal, Texture("block_26.png"))
        gameText.add(GameTexture.BLOCK_31.ordinal, Texture("block_31.png"))
        gameText.add(GameTexture.BLOCK_32.ordinal, Texture("block_32.png"))
        gameText.add(GameTexture.BLOCK_33.ordinal, Texture("block_33.png"))
        gameText.add(GameTexture.BLOCK_34.ordinal, Texture("block_34.png"))
        gameText.add(GameTexture.BLOCK_4.ordinal, Texture("block_4.png"))

        gameText.add(GameTexture.EXIT.ordinal, Texture("fon.png"))
        gameText.add(GameTexture.HEAD_N.ordinal, Texture("head_n.png"))
        gameText.add(GameTexture.HEAD_W.ordinal, Texture("head_w.png"))
        gameText.add(GameTexture.HEAD_S.ordinal, Texture("head_s.png"))
        gameText.add(GameTexture.HEAD_E.ordinal, Texture("head_e.png"))
        buttonsText.add(Button.UPSTICKER.ordinal, Texture("upsticker.png"))
        buttonsText.add(Button.LEFTSTICKER.ordinal, Texture("leftsticker.png"))
        buttonsText.add(Button.RIGHTSTICKER.ordinal, Texture("rightsticker.png"))
        buttonsText.add(Button.DOWNSTICKER.ordinal, Texture("downsticker.png"))
        buttonsText.add(Button.BODY.ordinal, Texture("body.png"))
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
        buttonsRect.add(Button.BODY.ordinal, Rectangle(50f, 320f, 160f, 160f))
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
        drawFond()
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
     * Отрисовка фона
     */
    fun drawFond(){
        game.batch.draw(buttonsText.get(Button.BODY.ordinal), buttonsRect.get(Button.BODY.ordinal).x, buttonsRect.get(Button.BODY.ordinal).y,
                buttonsRect.get(Button.BODY.ordinal).width, buttonsRect.get(Button.BODY.ordinal).height)
    }

    /**
     * Отрисовка текста
     */
    fun drawText(){
        game.font.color.set(Color.BLACK)
        //отрисовка яблок, собранных на этом уровне
        game.font.getData().setScale(2f, 2f)
        game.batch.draw(gameText.get(GameTexture.APPLE.ordinal), 500f, 1075f, width.toFloat()/15, width.toFloat()/15)
        game.font.draw(game.batch, " = " + points.toString(), 550f, 1105f)

        //количество яблок, собранных всего
        game.font.getData().setScale(2f, 2f)
        game.batch.draw(gameText.get(GameTexture.APPLE.ordinal), 500f, 1150f, width.toFloat()/15, width.toFloat()/15)
        game.batch.draw(gameText.get(GameTexture.APPLE.ordinal), 450f, 1150f, width.toFloat()/15, width.toFloat()/15)
        game.font.draw(game.batch, " = " + game.Points.toString(), 550f, 1180f)
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
                game.direction = 0
                game.batch.draw(messagesText.get(Message.LOSEMESSAGE.ordinal), 260f, 500f, 200f, 200f)
            }
            State.WIN -> {
                game.direction = 0
                game.batch.draw(messagesText.get(Message.WINMEESAGE.ordinal), 260f, 500f, 200f, 200f)
            }
        /*else -> {
            /*nothing*/
        }*/
        }
    }

    /**
     * Обновление карты, проверка на столкновения и определение взаимодействия с объектами
     */
    fun updateMap(){
        val x = headX
        val y = headY
        if ((x <0)||(y <0)||(x > m-1)||(y > n-1) || (x + 1 <0)||(y + 1 <0)||(x + 1> m-1)||(y+1 > n-1) )
            state = State.LOSE

        if (state != State.LOSE){
            when(map[x][y]){
                BLOCK -> {
                    state = State.LOSE
                }
                in 10..20 -> {
                    state = State.LOSE
                }
                APPLE -> {
                    points++
                }
                /*EXIT -> {
                    state = State.WIN
                }*/
            }

            when(map[x + 1][y])
            {
                EXIT -> {
                    if (speed_x > 0)
                        state = State.WIN
                }
            }

            if (!isHEAD(map[x][y]) && state != State.LOSE){
                if ((x > 0)&&isHEAD(map[x - 1][y])){
                    map[x][y] = HEAD_E
                    if ((x-1 == startX)&&(y == startY)){
                        map[x-1][y] = NECK_SE
                    }
                    else
                        when(map[x-1][y]){
                            HEAD_N->map[x-1][y] = NECK_SE
                            HEAD_E->map[x-1][y] = NECK_WE
                            HEAD_S->map[x-1][y] = NECK_NE
                        }
                }
                else{
                    if ((x < n-1)&&isHEAD(map[x + 1][y])){
                        map[x][y] = HEAD_W
                        when(map[x+1][y]){
                            HEAD_N->map[x+1][y] = NECK_SW
                            HEAD_W->map[x+1][y] = NECK_WE
                            HEAD_S->map[x+1][y] = NECK_NW
                        }
                    }
                    else{
                        if ((y > 0)&&isHEAD(map[x][y-1])){
                            map[x][y] = HEAD_N
                            if ((x == startX)&&(y-1 == startY)){
                                map[x][y-1] = NECK_NS
                                print("1")
                            }
                            else
                                when(map[x][y-1]){
                                    HEAD_N->map[x][y-1] = NECK_NS
                                    HEAD_W->map[x][y-1] = NECK_NE
                                    HEAD_E->map[x][y-1] = NECK_NW
                                }
                        }
                        else{
                            if ((y < m-1)&&isHEAD(map[x][y+1])){
                                map[x][y] = HEAD_S
                                when(map[x][y+1]){
                                    HEAD_E->map[x][y+1] = NECK_SW
                                    HEAD_W->map[x][y+1] = NECK_SE
                                    HEAD_S->map[x][y+1] = NECK_NS
                                }
                            }
                            else{
                                map[x][y] = HEAD_S
                            }

                        }
                    }
                }
            }
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
                if (isHEAD(map[x][y]))
                    drawHead(x,y,w,h)
                if (map[x][y] == BLOCK)
                    drawBlock(x,y,w,h)
                    //game.batch.draw(gameText.get(GameTexture.BLOCK.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
                if (map[x][y] == EXIT)
                    game.batch.draw(gameText.get(GameTexture.EXIT.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
                //if (map[x][y] == NECK)
                //   game.batch.draw(gameText.get(GameTexture.NECK.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15)+ (h-w), w/15, w/15)
                if (map[x][y] == APPLE)
                    game.batch.draw(gameText.get(GameTexture.APPLE.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15)+ (h-w), w/15, w/15)
                // if (map[x][y] == HEAD)
                //    game.batch.draw(gameText.get(GameTexture.GIRAFFEHEAD.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
                if (isNECK(map[x][y]))
                    drawNeck(x,y,w,h)
            }
        }
    }

    fun drawHead(x: Int, y: Int, w: Float, h: Float){
        //val x = xx+2
        when(map[x][y]){

            HEAD_N->game.batch.draw(gameText.get(GameTexture.HEAD_N.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15*headS, w/15*headS)
            HEAD_E->game.batch.draw(gameText.get(GameTexture.HEAD_E.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15*headS, w/15*headS)
            HEAD_S->game.batch.draw(gameText.get(GameTexture.HEAD_S.ordinal), (x.toFloat()+1/2)*(w/15), (y.toFloat()-1f/2f)*(w/15) + (h-w), w/15*headS,w/15*headS)
            HEAD_W->game.batch.draw(gameText.get(GameTexture.HEAD_W.ordinal), (x.toFloat()-1f/2f)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15*headS, w/15*headS)
        }
    }

    fun drawNeck(x: Int, y: Int, w: Float, h: Float){
        var t = gameText.get(GameTexture.NECK_NS.ordinal)
        var sizeX = neckXS;
        var sizeY = 1f;
        var sizedX = 1 - neckXS;
        var sizedY = 0f;
           when(map[x][y]) {
               NECK_NS -> {
                   t = gameText.get(GameTexture.NECK_NS.ordinal)
                   sizeX = neckXS
                   sizeY = 1f
                   sizedX = 1 - neckXS;
                   sizedY = 0f;
               }
               NECK_WE -> {
                   t = gameText.get(GameTexture.NECK_WE.ordinal)
                   sizeY = neckXS
                   sizeX = 1f
                   sizedY = 1 - neckXS;
                   sizedX = 0f;
               }
               NECK_SE -> {
                   t = gameText.get(GameTexture.NECK_SE.ordinal)
                   sizeY = neckXS + (1 - neckXS) / 2;
                   sizeX = 1f
                   sizedY = 0f
                   sizedX = 1 - neckXS;
               }
               NECK_SW -> {
                   t = gameText.get(GameTexture.NECK_SW.ordinal)
                   sizeX = neckXS + (1 - neckXS) / 2;
                   sizeY = 1f
                   sizedX = 0f;
                   sizedY = -(1 - neckXS);
               }
               NECK_NE -> {
                   t = gameText.get(GameTexture.NECK_NE.ordinal)
                   sizeX = 1f;
                   sizeY = neckXS + (1 - neckXS) / 2;
                   sizedX = 1 - neckXS;
                   sizedY = 1 - neckXS;
               }
               NECK_NW -> {
                   t = gameText.get(GameTexture.NECK_NW.ordinal)
                   sizeX = neckXS + (1 - neckXS) / 2
                   sizeY = neckXS + (1 - neckXS) / 2
                   sizedX = 0f;
                   sizedY = 1 - neckXS;
               }
               else -> {
                   print("$x $y ")
                   println(map[x][y])
               }
           }
        game.batch.draw(t, (x.toFloat()+1/2  + sizedX / 2f)*(w/15), (y.toFloat()+1/2 + sizedY / 2f)*(w/15) + (h-w), w/15*sizeX, w/15*sizeY)
    }


    /**
     * Функция для управления жирафом (свап, стикеры)
     */
    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец

        val swapSize = 30

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
            if(state == State.RUNNING && !swap){
                if (buttonsRect.get(Button.UPSTICKER.ordinal).contains(touchPos.x, touchPos.y) && game.direction != DOWN) {
                    speed_y = 1
                    speed_x = 0
                    game.direction = UP
                }
                if (buttonsRect.get(Button.DOWNSTICKER.ordinal).contains(touchPos.x, touchPos.y) && game.direction != UP) {
                    speed_y = -1
                    speed_x = 0
                    game.direction = DOWN
                }
                if (buttonsRect.get(Button.LEFTSTICKER.ordinal).contains(touchPos.x, touchPos.y) && game.direction != RIGHT) {
                    speed_y = 0
                    speed_x = -1
                    game.direction = LEFT
                }
                if (buttonsRect.get(Button.RIGHTSTICKER.ordinal).contains(touchPos.x, touchPos.y) && game.direction != LEFT) {
                    speed_y = 0
                    speed_x = 1
                    game.direction = RIGHT
                }
                if (buttonsRect.get(Button.BACK.ordinal).contains(touchPos.x, touchPos.y)) {
                    game.screen = LevelScreen(game)
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
                        game.screen = GameScreen(game, level, swap);
                        Gdx.input.setInputProcessor(null)
                    }
                }
                State.WIN -> {
                    if (buttonsRect.get(Button.OK.ordinal).contains(touchPos.x, touchPos.y)) {
                        game.Points += points
                        game.winLevel = level + 1
                        if(level < 2)
                            game.screen = GameScreen(game, level + 1, swap);
                        else game.screen = LevelScreen(game);
                        Gdx.input.setInputProcessor(null)
                    }
                }
            }
        }
        if(state == State.RUNNING) { //что такое 10?
            dy += speed_y
            dx += speed_x
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

    fun isHEAD(x: Int):Boolean{
        if (x in 20..30)
            return true
        else
            return false
    }
    fun isNECK(x: Int):Boolean{
        if (x in 10..20)
            return true
        else
            return false
    }

    fun drawBlock(x: Int, y: Int, w:Float, h:Float){
        var t = gameText.get(GameTexture.BLOCK_0.ordinal)
        when(x){
            0-> when(y){
                0->{
                    when(map[x+1][y]){
                        BLOCK-> when (map[x][y+1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_24.ordinal)
                            else ->  t = gameText.get(GameTexture.BLOCK_11.ordinal)
                        }
                        else -> when (map[x][y+1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_14.ordinal)
                        }
                    }
                }
                m-1->{
                    when(map[x+1][y]){
                        BLOCK-> when (map[x][y-1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_21.ordinal)
                            else ->  t = gameText.get(GameTexture.BLOCK_12.ordinal)
                        }
                        else -> when (map[x][y-1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_11.ordinal)
                        }
                    }
                }
                else->{
                    when(map[x+1][y]){
                        BLOCK -> when (map[x][y-1]){
                            BLOCK -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_34.ordinal)
                                else -> t = gameText.get(GameTexture.BLOCK_21.ordinal)
                            }
                            else -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_24.ordinal)
                                else -> t = gameText.get(GameTexture.BLOCK_11.ordinal)
                            }
                        }
                        else -> when (map[x][y-1]){
                            BLOCK -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_26.ordinal)
                                else -> t = gameText.get(GameTexture.BLOCK_12.ordinal)
                            }
                            else -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_14.ordinal)
                            }
                        }
                    }
                }
            }
            n-1 -> {
                when(y){
                    0->when(map[x-1][y]){
                        BLOCK-> when (map[x][y+1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_23.ordinal)
                            else ->  t = gameText.get(GameTexture.BLOCK_13.ordinal)
                        }
                        else -> when (map[x][y+1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_14.ordinal)
                        }
                    }
                    m-1->when(map[x-1][y]){
                        BLOCK-> when (map[x][y-1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_22.ordinal)
                            else ->  t = gameText.get(GameTexture.BLOCK_13.ordinal)
                        }
                        else -> when (map[x][y+1]){
                            BLOCK -> t = gameText.get(GameTexture.BLOCK_12.ordinal)
                        }
                    }
                    else -> when(map[x-1][y]){
                        BLOCK -> when (map[x][y-1]){
                            BLOCK -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_32.ordinal)
                                else -> t = gameText.get(GameTexture.BLOCK_22.ordinal)
                            }
                            else -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_23.ordinal)
                                else -> t = gameText.get(GameTexture.BLOCK_13.ordinal)
                            }
                        }
                        else -> when (map[x][y-1]){
                            BLOCK -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_26.ordinal)
                                else -> t = gameText.get(GameTexture.BLOCK_12.ordinal)
                            }
                            else -> when(map[x][y+1]){
                                BLOCK -> t = gameText.get(GameTexture.BLOCK_23.ordinal)
                            }
                        }
                    }
                }
            }
            else->{
                when(y){
                    0->{
                        when(map[x][y+1]){
                            BLOCK -> when(map[x+1][y]){
                                BLOCK -> when(map[x-1][y]){
                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_33.ordinal)
                                    else -> t = gameText.get(GameTexture.BLOCK_24.ordinal)
                                }
                                else -> when(map[x-1][y]){
                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_23.ordinal)
                                    else -> t = gameText.get(GameTexture.BLOCK_14.ordinal)
                                }
                            }
                            else -> when(map[x+1][y]){
                                BLOCK -> when(map[x-1][y]){
                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_25.ordinal)
                                    else -> t = gameText.get(GameTexture.BLOCK_11.ordinal)
                                }
                                else -> when(map[x-1][y]){
                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_13.ordinal)
                                }
                            }
                        }
                    }
                    m-1->{

                    }
                    else->{
                        when (map[x-1][y]){
                            BLOCK -> {
                                when (map[x+1][y]){
                                    BLOCK ->{
                                        when (map[x][y+1]){
                                            BLOCK -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_4.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_33.ordinal)
                                                }
                                            }
                                            else -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_31.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_25.ordinal)
                                                }
                                            }
                                        }
                                    }
                                    else -> {
                                        when (map[x][y+1]){
                                            BLOCK -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_32.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_23.ordinal)
                                                }
                                            }
                                            else -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_22.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_13.ordinal)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else -> {
                                when (map[x+1][y]){
                                    BLOCK ->{
                                        when (map[x][y+1]){
                                            BLOCK -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_34.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_24.ordinal)
                                                }
                                            }
                                            else -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_21.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_11.ordinal)
                                                }
                                            }
                                        }
                                    }
                                    else -> {
                                        when (map[x][y+1]){
                                            BLOCK -> {
                                                when (map[x][y-1]) {
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_26.ordinal)
                                                    else -> t = gameText.get(GameTexture.BLOCK_14.ordinal)
                                                }
                                            }
                                            else -> {
                                                when (map[x][y-1]){
                                                    BLOCK -> t = gameText.get(GameTexture.BLOCK_12.ordinal)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        game.batch.draw(t, (x.toFloat()+1/2)*(w/15), (y.toFloat()+1/2)*(w/15) + (h-w), w/15, w/15)
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