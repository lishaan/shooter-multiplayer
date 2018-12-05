package game

import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.canvas.GraphicsContext

import akka.actor.ActorRef

object Player {
    def generateID(): String = (scala.util.Random.alphanumeric.take(10).mkString)
}

/** A Player object controlled by the user that inherits the traits: [[Drawable]], [[Moveable]] and [[Shootable]].
 *
 *  @constructor create a new instance of a Player object by the given player name
 *  @param playerName the name of the Player
 */
class Player (private val _ref: ActorRef) extends Drawable with Moveable with Shootable with Damageable {
	private val _name: String = "Player"
	private var _kills: Int = 0

	val _position: Position = new Position(Global.gameWidth/2, Global.gameHeight/2, 0)
	val _health: Health = new Health(100)
	var _size: Double = Global.size("Player")
	val _color: Color = Global.color("Player")
	var _speed: Double = Global.speed("Player")
    var _rotationSpeed: Double = 4

	def shootBullet: Unit = (_bullets +:= new Bullet(this.position))

	def move = println("Error: Parameter (direction: String) required")

	/** Moves the player at the given direction 
     *  @param direction the direction as a String
	 */
	def move(direction: String): Unit = {
		speed = Global.speed("Player")
		size = Global.size("Player")

		// TODO: Bounds
		// val outOfBounds: Boolean = (
		// 	((position.x+size < Global.gameWidth) && (position.y+size < Global.gameHeight))
		// 	||
		// 	((position.y-size > 0) && (position.x-size > 0))
		// )

		// println(outOfBounds)

		// // Normalized Angle from -90 to 90
		// val na: Double = (Math.sin(position.r) * 90)

		direction match {
			case "Forward"  => position.moveForward(speed)
			case "Backward" => position.moveBackward(speed)
			case _ =>
		}
	}

    def rotateLeft: Unit  = position.rotateLeft(rotationSpeed)
    def rotateRight: Unit = position.rotateRight(rotationSpeed)

	def draw(drawer: GraphicsContext): Unit = {
		// Draw health bar
		drawer.fill = Color.Blue
		drawer.fillRect(position.x-size, position.y+size+(size/2), size*2, size/4)

		drawer.fill = Color.Red
		drawer.fillRect(position.x-size, position.y+size+(size/2), (size*2)*health.percentage, size/4)

		// Draw bullets
		bullets.foreach(_.draw(drawer))

		// Draws at center
		drawer.fill = color
		drawer.fillOval(position.x-size, position.y-size, size*2, size*2)

		// Draw gun
		val x = position.x - (size/4)
		val y = position.y - (size/4)
		val r = position.r

		val gun_x: Double = x + ((size) * Math.cos(r))
		val gun_y: Double = y + ((size) * Math.sin(r))

		drawer.fill = Color.web("ff0000")
		drawer.fillOval(gun_x, gun_y, size/2, size/2)
	}

	def ref = _ref
	def name = _name
	def kills = _kills

	/** Increments the player's kills. */
	def incrementKills = { _kills = _kills + 1 }

	// def copy(player: Player): Unit = { this = player }
}