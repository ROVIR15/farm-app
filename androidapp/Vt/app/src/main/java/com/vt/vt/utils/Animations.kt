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
        currentAnimator?.cancel()

        expandedCircleImageView?.setImageResource(imageResId)
            ?: expandedImageView?.setImageResource(imageResId)

        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        thumbView.getGlobalVisibleRect(startBoundsInt)
        container.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

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
        if (expandedCircleImageView != null) {
            expandedCircleImageView.visibility = View.VISIBLE
            expandedCircleImageView.pivotX = 0f
        } else if (expandedImageView != null) {
            expandedImageView.visibility = View.VISIBLE
            expandedImageView.pivotX = 0f
        }

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
        val expandedView = expandedCircleImageView ?: expandedImageView

        expandedView?.setOnClickListener {
            currentAnimator?.cancel()

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