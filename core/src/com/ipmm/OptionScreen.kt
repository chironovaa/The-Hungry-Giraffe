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
    internal lateinit var textLanduage: Texture
    internal lateinit var textOn: Texture
    internal lateinit var textOff: Texture
    internal lateinit var textOnBlack: Texture
    internal lateinit var textOffBlack: Texture
    internal lateinit var textControlText: Texture
    internal lateinit var textSoundsText: Texture
    internal lateinit var textLanguageText: Texture
    internal lateinit var textSettings: Texture

    internal lateinit var rectSoundsText: Rectangle
    internal lateinit var rectSounds: Rectangle
    internal lateinit var rectLanguageText: Rectangle
    internal lateinit var rectBackButton : Rectangle
    internal lateinit var rectLanguage: Rectangle
    internal lateinit var rectControlText: Rectangle
    internal lateinit var rectControl: Rectangle
    internal lateinit var rectSettings: Rectangle

    internal var sounds: Boolean = false
    internal var language: Boolean = true
    internal var is_action_control: Boolean = true
    internal var is_action_sounds: Boolean = false
    internal var is_action_language: Boolean = false

    internal var icon_w: Float = 200f
    internal var icon_h: Float = 200f

    class settings {
        companion object Factory {
            var swap = true
        }
    }

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
        //game.batch.draw(textWall, 0f, 0f, width.toFloat(), height.toFloat())
        game.batch.draw(textBackButton, rectBackButton.x, rectBackButton.y, rectBackButton.width, rectBackButton.height)
        game.batch.draw(textControlText , rectControlText.x, rectControlText.y, rectControlText.width, rectControlText.height)
        game.batch.draw(textSettings, rectSettings.x, rectSettings.y, rectSettings.width, rectSettings.height)

        if (settings.swap)
            if (is_action_control)
                game.batch.draw(textOn, rectControl.x, rectControl.y, rectControl.width, rectControl.height)
            else
                game.batch.draw(textOnBlack, rectControl.x, rectControl.y, rectControl.width, rectControl.height)
        else
            if (is_action_control)
                game.batch.draw(textOff, rectControl.x, rectControl.y, rectControl.width, rectControl.height)
            else
                game.batch.draw(textOffBlack, rectControl.x, rectControl.y, rectControl.width, rectControl.height)

        game.batch.draw(textSoundsText , rectSoundsText.x, rectSoundsText.y, rectSoundsText.width, rectSoundsText.height)
        if (sounds)
            if (is_action_sounds)
                game.batch.draw(textOn, rectSounds.x, rectSounds.y, rectSounds.width, rectSounds.height)
            else
                game.batch.draw(textOnBlack, rectSounds.x, rectSounds.y, rectSounds.width, rectSounds.height)
        else
            if (is_action_sounds)
                game.batch.draw(textOff, rectSounds.x, rectSounds.y, rectSounds.width, rectSounds.height)
            else
                game.batch.draw(textOffBlack, rectSounds.x, rectSounds.y, rectSounds.width, rectSounds.height)

        game.batch.draw(textLanguageText , rectLanguageText.x, rectLanguageText.y, rectLanguageText.width, rectLanguageText.height)

        if (language)
            if(is_action_language)
                 game.batch.draw(textOn, rectLanguage.x, rectLanguage.y,rectLanguage.width, rectLanguage.height)
            else
                game.batch.draw(textOnBlack, rectLanguage.x, rectLanguage.y,rectLanguage.width, rectLanguage.height)
        else
            if (is_action_sounds)
                game.batch.draw(textOff, rectLanguage.x, rectLanguage.y, rectLanguage.width, rectLanguage.height)
             else
                game.batch.draw(textOffBlack, rectLanguage.x, rectLanguage.y, rectLanguage.width, rectLanguage.height)
        game.batch.end()
        control()
        camera.update()
    }

    fun loadTextures(){
        textWall = Texture("back-option.png")
        //textWall = Texture("options.png")
        textBackButton = Texture("back-icon.png")
        textOn =Texture("control_on-option-icon.png")
        textOff =Texture("control_off-option-icon.png")
        textOnBlack =Texture("control_on-option-icon-black.png")
        textOffBlack =Texture("control_off-option-icon-black.png")
        textControlText =Texture("joystick-option-icon.png")
        textSoundsText =Texture("sounds-option-icon.png")
        textLanguageText =Texture("language-option-icon.png")
        textSettings = Texture("settings-icon.png")
        //textBack =Texture("back-option.png")
    }

    fun createRectangles(){
        rectBackButton = Rectangle(100f, height - 100f, 100f, 100f)
        rectSettings = Rectangle(150f, height - 350f, 2*icon_w, icon_h)

        rectControlText= Rectangle(0f, height - 200f - icon_h - 200f, 2*icon_w, icon_h)
        rectControl= Rectangle(width - icon_w, height - 200f - icon_h - 200f, icon_w, icon_h)

        rectSoundsText= Rectangle(0f, height - 140f - 2 * icon_h - 200f, icon_w, icon_h/2)
        rectSounds= Rectangle(width - icon_w, height - 200f - 2 * icon_h - 200f, icon_w, icon_h)

        rectLanguageText= Rectangle(0f, height - 200f - 3 * icon_h - 200f, 2*icon_w, icon_h)
        rectLanguage= Rectangle(width - icon_w, height - 200f - 3 * icon_h - 200f, icon_w, icon_h)
    }

    fun control(){ //написана отдельная функция для управления, чтобы кнопка реагировала даже в случае, когда пользователь не отпускает палец
        if(Gdx.input.isTouched(0)) {
            val touchPos = Vector3()
            touchPos.set(Gdx.input.getX(0).toFloat(), Gdx.input.getY(0).toFloat(), 0f)
            camera.unproject(touchPos) //важная функция для того, чтобы подгонять координаты приложения в разных телефонах
            if (rectBackButton.contains(touchPos.x, touchPos.y)) {
                game.screen = MainMenuScreen(game)
                Gdx.input.setInputProcessor(null)
            }
            if (is_action_control && rectControl.contains(touchPos.x, touchPos.y)) {
                settings.swap = settings.swap.not()
            }
            if (is_action_sounds && rectSounds.contains(touchPos.x, touchPos.y)) {
                sounds = sounds.not()
            }
            if (is_action_language && rectLanguage.contains(touchPos.x, touchPos.y)) {
                language = language.not()
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
