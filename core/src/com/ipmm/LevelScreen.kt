package com.ipmm

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class LevelScreen(internal val game: MainActivity) : Screen, GestureDetector.GestureListener, InputProcessor {
    internal var camera = OrthographicCamera()
    internal var texButtonLevel1: Texture
    internal var texButtonLevel2: Texture
    internal var texButtonLevel3: Texture
    internal var texButtonLevels: Texture
    internal var textBackButton: Texture
    //internal var texLogo: Texture
    internal var rectBackButton : Rectangle
    internal var level1Rect: Rectangle
    internal var level2Rect: Rectangle
    internal var level3Rect: Rectangle
    internal var levelsRect: Rectangle
    internal var logoRect: Rectangle
    internal var width = 720
    internal var height = 1200

    init {
        camera.setToOrtho(false, width.toFloat(), height.toFloat())

        texButtonLevel1 = Texture("level1.png")
        texButtonLevel2 = Texture("level2_1.png")
        texButtonLevel3 = Texture("level3_1.png")
        texButtonLevels = Texture("levels-icon.png")
        textBackButton = Texture("back-icon.png")
        //texLogo = Texture("logo.png")
        level1Rect = Rectangle(36f, 406f, 200f, 200f)
        level2Rect = Rectangle(500f, 406f, 200f, 200f)
        level3Rect = Rectangle(275f, 250f, 200f, 200f)
        levelsRect = Rectangle(225f, 700f,250f,250f)
        logoRect = Rectangle(104f, 500f, 512f, 512f)
        rectBackButton = Rectangle(100f, height - 100f, 100f, 100f)
    }

    override fun render(delta: Float) {
        Gdx.input.inputProcessor = this //нужно для работы кнопок телефона. например, кнопки назад.
        Gdx.input.isCatchBackKey = true
        Gdx.gl.glClearColor(247f, 247f, 245f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        camera.update()
        game.batch.projectionMatrix = camera.combined

        game.batch.begin()
        game.font.data.setScale(4f, 4f)
        //game.batch.draw(texLogo, logoRect.x, logoRect.y, logoRect.width, logoRect.height)
        if (game.winLevel >= 1)
            texButtonLevel2 = Texture("level2.png")
        if (game.winLevel >= 2)
            texButtonLevel3 = Texture("level3.png")
        game.batch.draw(textBackButton, rectBackButton.x, rectBackButton.y, rectBackButton.width, rectBackButton.height)
        game.batch.draw(texButtonLevel1, level1Rect.x, level1Rect.y, level1Rect.width, level1Rect.height)
        game.batch.draw(texButtonLevel3, level2Rect.x, level2Rect.y, level2Rect.width, level2Rect.height)
        game.batch.draw(texButtonLevel2, level3Rect.x, level3Rect.y, level3Rect.width, level3Rect.height)
        game.batch.draw(texButtonLevels, levelsRect.x, levelsRect.y, levelsRect.width, levelsRect.height)
        game.batch.end()

    }

    override fun keyDown(keycode: Int): Boolean {
        if(keycode == Input.Keys.BACK){
            Gdx.app.exit()
        }
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int) = false

    override fun mouseMoved(screenX: Int, screenY: Int) = false

    override fun keyTyped(character: Char) = false

    override fun scrolled(amount: Int) = false

    override fun keyUp(keycode: Int) = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int) = false

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val touchPos = Vector3()
        touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
        camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
        if (rectBackButton.contains(touchPos.x, touchPos.y)) {
            game.screen = MainMenuScreen(game)
        }
        if (level1Rect.contains(touchPos.x, touchPos.y)) {
            var gs : GameScreen = GameScreen(game, 0, OptionScreen.settings.swap.not())
            game.screen = gs
            Gdx.input.setInputProcessor(null)
            dispose()
        }
        if (level2Rect.contains(touchPos.x, touchPos.y) && game.winLevel >= 2) {
            var gs : GameScreen = GameScreen(game, 2, OptionScreen.settings.swap.not())
            game.screen = gs
            Gdx.input.setInputProcessor(null)
            dispose()
        }
        if (level3Rect.contains(touchPos.x, touchPos.y) && game.winLevel >= 1) {
            var gs : GameScreen = GameScreen(game, 1, OptionScreen.settings.swap.not())
            game.screen = gs
            Gdx.input.setInputProcessor(null)
            dispose()
        }
        return false
    }

    override fun resize(width: Int, height: Int) = Unit

    override fun show() {
        Gdx.input.inputProcessor = GestureDetector(this)
    }

    override fun hide() = Unit

    override fun pause() = Unit

    override fun resume() = Unit

    override fun dispose() = Unit

    override fun touchDown(x: Float, y: Float, pointer: Int, button: Int) = false

    override fun tap(x: Float, y: Float, count: Int, button: Int) = false

    override fun longPress(x: Float, y: Float) = false

    override fun fling(velocityX: Float, velocityY: Float, button: Int) = false

    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float) = false

    override fun panStop(x: Float, y: Float, pointer: Int, button: Int) = false

    override fun zoom(initialDistance: Float, distance: Float) = false

    override fun pinch(initialPointer1: Vector2, initialPointer2: Vector2, pointer1: Vector2, pointer2: Vector2) = false

    override fun pinchStop() = Unit

    companion object {
        var pMines = 30
    }
}