package abobus.game.good

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.StretchViewport
import org.lwjgl.opengl.GL20
import java.security.SecureRandom

lateinit var bulletTexture: Texture

class GoodGame: ApplicationAdapter() {

    private lateinit var worldViewport: StretchViewport
    private lateinit var spriteBatch: SpriteBatch
    private lateinit var shapeRenderer: ShapeRenderer

    private val mousePos = Vector2()
    private val mousePos3 = Vector3()

    private lateinit var tankTexture: Texture
    private lateinit var niggaTankTexture: Texture

    private lateinit var player: Entity
    private val randomShit = ArrayList<Entity>()
    private val bullets = ArrayList<Entity>()
    private val random = SecureRandom(SecureRandom.getSeed(4))

    private fun updateMousePosition() {
        mousePos3.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0F)
        worldViewport.camera.unproject(mousePos3)
        mousePos.set(mousePos3.x, mousePos3.y)
    }

    override fun create() {
        Gdx.gl.glClearColor(0.18f, 0.18f, 0.18f, 1f)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        worldViewport = StretchViewport(100F, 100F)
        spriteBatch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        tankTexture = Texture("src/main/resources/icons/tank.png")
        niggaTankTexture = Texture("src/main/resources/icons/tank_enemy.png")
        bulletTexture = Texture("src/main/resources/icons/bullet.png")
        player = Entity.create(tankTexture)
        Gdx.input.inputProcessor = player.Controller(bulletTexture, bullets)

        for (i in 0 until 80) {
            val x = random.nextFloat(-200F, 200F)
            val y = random.nextFloat(-200F, 200F)
            randomShit.add(Entity.create(niggaTankTexture, x, y, 8F, 8F))
        }
    }

    private fun update() {
        updateMousePosition()
        player.update()
        worldViewport.camera.position.set(player.centerPos.x, player.centerPos.y, 0F)
        val bulletIterator = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet = bulletIterator.next()
            bullet.update()
            val p = bullet.centerPos
            val v = bullet.velocity

            if (p.x < -199F || p.x > 199F) v.x *= -1
            if (p.y < -199F || p.y > 199F) v.y *= -1
        }
        randomShit.forEach {
            it.moveAt(player.centerPos)
            it.update()
            it.lookAt(player.centerPos)
        }
        player.lookAt(mousePos)
    }

    override fun render() {
        update()
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        spriteBatch.projectionMatrix = worldViewport.camera.combined
        shapeRenderer.projectionMatrix = worldViewport.camera.combined
        worldViewport.apply()
        shapeRenderer.setColor(1F, 0F, 0F, 1F)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        shapeRenderer.rect(-200F, -200F, 400F, 400F)
        shapeRenderer.end()
        spriteBatch.begin()
        bullets.forEach { it.render(spriteBatch) }
        randomShit.forEach { it.render(spriteBatch) }
        player.render(spriteBatch)
        if (player.position.x > 195f) player.position.x = 195f
        if (player.position.x < -200f) player.position.x = -200f
        if (player.position.y > 195f) player.position.y = 195f
        if (player.position.y < -200f) player.position.y = -200f
        spriteBatch.end()
    }

    override fun resize(width: Int, height: Int) {
        val worldRatio = height / width.toFloat()
        if (height > width) worldViewport.setWorldSize(100F / worldRatio, 100F) else {
            val worldHeight = 150F * worldRatio
            worldViewport.setWorldSize(150F, worldHeight)
        }
        worldViewport.update(width, height, false)
    }

    override fun dispose() {
        tankTexture.dispose()
        niggaTankTexture.dispose()
        bulletTexture.dispose()
        shapeRenderer.dispose()
    }

}