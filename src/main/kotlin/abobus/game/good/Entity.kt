package abobus.game.good

import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import kotlin.math.atan2

class Entity(val position: Vector2 = Vector2(),
             val size: Vector2 = Vector2(),
             var rotation: Float = 0F,
             val velocity: Vector2 = Vector2(),
             var texture: Texture) {

    val centerPos = Vector2()

    fun render(spriteBatch: SpriteBatch) {
        spriteBatch.draw(texture,
            position.x, position.y,
            size.x / 2F, size.y / 2F,
            size.x, size.y,
            1F, 1F,
            rotation,
            0, 0,
            texture.width, texture.height,
            false,  false)
    }

    fun update() {
        centerPos.set(position.x + size.x / 2F, position.y + size.y / 2F)
        position.add(velocity)
    }

    fun moveAt(other: Vector2) {
        velocity.set(other.x - centerPos.x, other.y - centerPos.y)
        velocity.nor()
        velocity.scl(SPEED * 0.2F)
    }

    fun lookAt(other: Vector2) {
        rotation = atan2(
            other.x - centerPos.x,
            centerPos.y - other.y)
            .toDegrees() + 180F
    }



    inner class Controller: InputAdapter() {

        override fun keyDown(keycode: Int): Boolean {
            when (keycode) {
                A -> velocity.x -= SPEED
                D -> velocity.x += SPEED
                W -> velocity.y += SPEED
                S -> velocity.y -= SPEED
            }
            return false
        }

        override fun keyUp(keycode: Int): Boolean {
            when (keycode) {
                A -> velocity.x += SPEED
                D -> velocity.x -= SPEED
                W -> velocity.y -= SPEED
                S -> velocity.y += SPEED
            }
            return false
        }
    }

    companion object {
        const val SPEED = 0.2F

        fun create(texture: Texture, x: Float = 0F, y: Float = 0F, w: Float = 5F, h: Float = 5F): Entity =
            Entity(Vector2(x, y), Vector2(w, h), texture = texture)

        private const val ANGLE_CONVERT: Float = (180.0f / Math.PI).toFloat()

        fun Float.toRadians(): Float = this / ANGLE_CONVERT

        fun Float.toDegrees(): Float = this * ANGLE_CONVERT
    }
}