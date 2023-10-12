package rconnect.retvens.technologies.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import rconnect.retvens.technologies.R

 fun fadeInAnimation(view: View, context: Context) {
    // load the animation
    val animSlideIn: Animation = AnimationUtils.loadAnimation(
        context,
        R.anim.fade_in_animation
    )
    // start the animation
    view.startAnimation(animSlideIn)
}
fun fadeOutAnimation(view: View, context: Context) {
    // load the animation
    val animSlideIn: Animation = AnimationUtils.loadAnimation(
        context,
        R.anim.fade_out_animation
    )
    // start the animation
    view.startAnimation(animSlideIn)
}

fun bottomSlideInAnimation(view: View, context: Context) {
    // load the animation
    val animSlideIn: Animation = AnimationUtils.loadAnimation(
        context,
        R.anim.bottom_slide_in
    )
    // start the animation
    view.startAnimation(animSlideIn)
}
