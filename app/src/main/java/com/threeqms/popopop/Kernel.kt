package com.threeqms.popopop

import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import kotlin.math.roundToInt
import com.threeqms.popopop.KernelSimulator
import java.util.Vector

class Kernel(pos : Vector2, r : Float, v : View, startVel : Vector2, startAngVel : Float, popTime : Float, unpoppedDrawable: Drawable, poppedDrawable: Drawable, mp: MediaPlayer) {

    companion object {
        val ELASTICITY = 0.97f
        val ACCELEROMETER_MULTIPLIER = 100f;
        val POP_PROGRESS_PER_UNIT_MOVED = 0.005f;
        val POP_PROGRESS_PER_COLLISION_FORCE = 0.001f;
        val POP_VELOCITY = 500f
    }
    
    val view : View = v

    val position : Vector2 = pos
    var rotation : Float = 0f
    val radius : Float = r
    val velocity : Vector2 = startVel
    val angularVelocity : Float = startAngVel
    var popProgress : Float = popTime
    val mass : Float = 1f
    val unpoppedDrawable: Drawable = unpoppedDrawable
    val poppedDrawable: Drawable = poppedDrawable
    val mediaPlayer: MediaPlayer = mp

    init {
        updateView()
        view.findViewById<ImageView>(R.id.kernelImage).setImageDrawable(unpoppedDrawable)
    }

    fun updateView()
    {
        view.isVisible = true;
        view.layoutParams.width = (2 * radius).roundToInt()
        view.layoutParams.height = (2 * radius).roundToInt()
        view.x = (position.x - radius)
        view.y = (position.y - radius)
        view.rotation = rotation
    }

    fun update(ks: KernelSimulator, dt : Float){
        velocity += Vector2(-ks.getAcceleration()[0] * ACCELEROMETER_MULTIPLIER, + ks.getAcceleration()[1] * ACCELEROMETER_MULTIPLIER) * dt
//        if(position.x.isNaN() || position.y.isNaN() || velocity.x.isNaN() || velocity.y.isNaN()){
//            position.x = 0f
//            position.y = 0f
//            velocity.x = 0f
//            velocity.y = 0f
//        }
        val newPos : Vector2 = position + velocity * dt
        val tempProgress = popProgress

        var yTime : Float = 1f
        //Find the percentage of the velocity it takes to collide with the wall. Can also be negative. (Botton)
        if(velocity.y > 0 && newPos.y - position.y != 0f){
            yTime = (ks.getMaxBounds().y - (position.y + radius)) / (newPos.y - position.y)
        }
        //(Top)
        if(velocity.y < 0 && newPos.y - position.y != 0f){
            yTime = (ks.getMinBounds().y - (position.y - radius)) / (newPos.y - position.y)
        }

        var xTime : Float = 1f
        //Find the percentage of the velocity it takes to collide with the wall. Can also be negative. (Right)
        if(velocity.x > 0 && newPos.x - position.x != 0f){
            xTime = (ks.getMaxBounds().x - (position.x + radius)) / (newPos.x - position.x)
        }
        //(Left)
        if(velocity.x < 0 && newPos.x - position.x != 0f){
            xTime = (ks.getMinBounds().x - (position.x - radius)) / (newPos.x - position.x)
        }
        //Use percentage of velocity to place next to wall but not past it
        if(yTime < 1 || xTime < 1){
            updatePosition(Math.min(yTime, xTime) * velocity * dt)
            val oldVelocity : Vector2 = Vector2(velocity.x, velocity.y)

            if(yTime == Math.min(yTime, xTime)){
                velocity *= Vector2(1f, -1f) * ELASTICITY

            }
            if(xTime == Math.min(yTime, xTime)){
                velocity *= Vector2(-1f, 1f) * ELASTICITY
            }
            popProgress -= (velocity - oldVelocity).magnitude() * POP_PROGRESS_PER_COLLISION_FORCE
        }
        else{
            updatePosition(velocity * dt)
        }
        if(tempProgress > 0 && popProgress <= 0){
            pop()
        }
        rotation += angularVelocity * dt
    }

    fun updatePosition(delta : Vector2){
        position += delta
        popProgress -= delta.magnitude() * POP_PROGRESS_PER_UNIT_MOVED
    }
    fun pop(){
        view.findViewById<ImageView>(R.id.kernelImage).setImageDrawable(poppedDrawable)
        velocity += Vector2(1f, 0f).rotated(Math.random().toFloat() * 360) * POP_VELOCITY
        mediaPlayer.setVolume(1 + Math.random().toFloat() * 2f, 1 + Math.random().toFloat() * 2f)
        mediaPlayer.start()
    }
}