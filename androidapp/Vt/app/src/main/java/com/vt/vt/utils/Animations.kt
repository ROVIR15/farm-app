package com.vt.vt.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import de.hdodenhof.circleimageview.CircleImageView

object Animations {
    private var currentAnimator: Animator? = null
    fun zoomImageFromThumb(
        thumbView: View,
        imageResId: Int,
        shortAnimationDuration: Int,
        container: ConstraintLayout,
        expandedCircleImageView: CircleImageView?,
        expandedImageView: ImageView?,
    ) {
        // If there's an animation in progress, cancel it immediately and
        // proceed with this one.
        currentAnimator?.cancel()

        // Load the high-resolution "zoomed-in" image.
        expandedCircleImageView?.setImageResource(imageResId)
            ?: expandedImageView?.setImageResource(imageResId)

        // Calculate the starting and ending bounds for the zoomed-in image.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the
        // container view. Set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBoundsInt)
        container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        // Using the "center crop" technique, adjust the start bounds to be the
        // same aspect ratio as the final bounds. This prevents unwanted
        // stretching during the animation. Calculate the start scaling factor.
        // The end scaling factor is always 1.0.
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally.
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically.
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it positions the zoomed-in view in the place of the
        // thumbnail.
        thumbView.alpha = 0f

        animateZoomToLargeImage(
            startBounds,
            finalBounds,
            startScale,
            shortAnimationDuration,
            expandedCircleImageView,
            expandedImageView
        )

        setDismissLargeImageAnimation(
            thumbView,
            startBounds,
            startScale,
            shortAnimationDuration,
            expandedCircleImageView,
            expandedImageView,
        )
    }

    private fun animateZoomToLargeImage(
        startBounds: RectF,
        finalBounds: RectF,
        startScale: Float,
        shortAnimationDuration: Int,
        expandedCircleImageView: CircleImageView?,
        expandedImageView: ImageView?
    ) {
//        var currentAnimator = animator
        if (expandedCircleImageView != null) {
            expandedCircleImageView.visibility = View.VISIBLE
            expandedCircleImageView.pivotX = 0f
        } else if (expandedImageView != null) {
            expandedImageView.visibility = View.VISIBLE
            expandedImageView.pivotX = 0f
        }

        // Construct and run the parallel animation of the four translation and
        // scale properties: X, Y, SCALE_X, and SCALE_Y.
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expandedImageView ?: expandedCircleImageView,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        expandedImageView ?: expandedCircleImageView,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(
                    ObjectAnimator.ofFloat(
                        expandedImageView ?: expandedCircleImageView,
                        View.SCALE_X,
                        startScale,
                        1f
                    )
                )
                with(
                    ObjectAnimator.ofFloat(
                        expandedImageView ?: expandedCircleImageView,
                        View.SCALE_Y,
                        startScale,
                        1f
                    )
                )
            }
            duration = shortAnimationDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    currentAnimator = null
                }
            })
            start()
        }
    }

    private fun setDismissLargeImageAnimation(
        thumbView: View,
        startBounds: RectF,
        startScale: Float,
        shortAnimationDuration: Int,
        expandedCircleImageView: CircleImageView?,
        expandedImageView: ImageView?,
    ) {
//        var currentAnimator = animator
        val expandedView = expandedCircleImageView ?: expandedImageView
        // When the zoomed-in image is tapped, it zooms down to the original
        // bounds and shows the thumbnail instead of the expanded image.

        expandedView?.setOnClickListener {
            currentAnimator?.cancel()

            // Animate the four positioning and sizing properties in parallel,
            // back to their original values.
            currentAnimator = AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        expandedView,
                        View.X,
                        startBounds.left
                    )
                ).apply {
                    with(ObjectAnimator.ofFloat(expandedView, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(expandedView, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(expandedView, View.SCALE_Y, startScale))
                }
                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedView.visibility = View.GONE
                        currentAnimator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        thumbView.alpha = 1f
                        expandedView.visibility = View.GONE
                        currentAnimator = null
                    }
                })
                start()
            }
        }
    }
}