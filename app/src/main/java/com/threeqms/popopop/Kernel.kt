package com.threeqms.popopop

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.view.isVisible
import kotlin.math.roundToInt
import com.threeqms.popopop.KernelSimulator

class Kernel(pos : Vector2, r : Float, v : View, startVel : Vector2, startAngVel : Float, unpoppedDrawable: Drawable, poppedDrawable: Drawable) {

    companion object {
        val ELASTICITY = 0.97f
        val ACCELEROMETER_MULTIPLIER = 100f;
    }
    
    val view : View = v

    val position : Vector2 = pos
    var rotation : Float = 0f
    val radius : Float = r
    val velocity : Vector2 = startVel
    val angularVelocity : Float = startAngVel
    val popProgress : Float = 0f
    val mass : Float = 1f
    val unpopped: Drawable = unpoppedDrawable
    val poppedDrawable: Drawable = poppedDrawable

    init {
        updateView()
    }

    fun updateView()
    {
        view.findViewById<ImageView>(R.id.kernelImage).setImageDrawable(unpopped)
        view.isVisible = true;
        view.layoutParams.width = (2 * radius).roundToInt()
        view.layoutParams.height = (2 * radius).roundToInt()
        view.x = (position.x - radius)
        view.y = (position.y - radius)
        view.rotation = rotation
    }

    fun update(ks: KernelSimulator, dt : Float){
        velocity += Vector2(-ks.getAcceleration()[0] * ACCELEROMETER_MULTIPLIER, + ks.getAcceleration()[1] * ACCELEROMETER_MULTIPLIER) * dt
        if(position.x.isNaN() || position.y.isNaN() || velocity.x.isNaN() || velocity.y.isNaN()){
            position.x = 0f
            position.y = 0f
            velocity.x = 0f
            velocity.y = 0f
        }
        val newPos : Vector2 = position + velocity * dt

        var yTime : Float = 1f
        //Find the percentage of the velocity it takes to collide with the wall. Can also be negative. (Botton)
        if(velocity.y > 0){
            yTime = (ks.getMaxBounds().y - (position.y + radius)) / (newPos.y - position.y)
        }
        //(Top)
        if(velocity.y < 0){
            yTime = (ks.getMinBounds().y - (position.y - radius)) / (newPos.y - position.y)
        }

        var xTime : Float = 1f
        //Find the percentage of the velocity it takes to collide with the wall. Can also be negative. (Right)
        if(velocity.x > 0){
            xTime = (ks.getMaxBounds().x - (position.x + radius)) / (newPos.x - position.x)
        }
        //(Left)
        if(velocity.x < 0){
            xTime = (ks.getMinBounds().x - (position.x - radius)) / (newPos.x - position.x)
        }
        //Use percentage of velocity to place next to wall but not past it
        if(yTime < 1 || xTime < 1){
            position += Math.min(yTime, xTime) * velocity * dt
            if(yTime == Math.min(yTime, xTime)){
                velocity *= Vector2(1f, -1f) * ELASTICITY

            }
            if(xTime == Math.min(yTime, xTime)){
                velocity *= Vector2(-1f, 1f) * ELASTICITY
            }
        }
        else{
            position +=  velocity * dt
        }

        rotation += angularVelocity * dt
    }
}