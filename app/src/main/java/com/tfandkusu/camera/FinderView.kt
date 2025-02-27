package com.tfandkusu.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class FinderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {

    private val paint = Paint()

    private var width = 0

    private var height = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        width = w
        height = h
    }

    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val dp = resources.displayMetrics.density
        paint.color = ContextCompat.getColor(context, R.color.white)
        paint.isAntiAlias = true

        // 左上
        rect.left = 0f
        rect.top = 0f
        rect.right = LENGTH_DP * dp
        rect.bottom = WIDTH_DP * dp
        canvas.drawRect(rect, paint)
        rect.left = 0f
        rect.top = 0f
        rect.right = WIDTH_DP * dp
        rect.bottom = LENGTH_DP * dp
        canvas.drawRect(rect, paint)

        // 右上
        rect.left = width - LENGTH_DP * dp
        rect.top = 0f
        rect.right = width.toFloat()
        rect.bottom = WIDTH_DP * dp
        canvas.drawRect(rect, paint)
        rect.left = width - WIDTH_DP * dp
        rect.top = 0f
        rect.right = width.toFloat()
        rect.bottom = LENGTH_DP * dp
        canvas.drawRect(rect, paint)

        // 左下
        rect.left = 0f
        rect.top = height - WIDTH_DP * dp
        rect.right = LENGTH_DP * dp
        rect.bottom = height.toFloat()
        canvas.drawRect(rect, paint)
        rect.left = 0f
        rect.top = height - LENGTH_DP * dp
        rect.right = WIDTH_DP * dp
        rect.bottom = height.toFloat()
        canvas.drawRect(rect, paint)

        // 右下
        rect.left = width - LENGTH_DP * dp
        rect.top = height - WIDTH_DP * dp
        rect.right = width.toFloat()
        rect.bottom = height.toFloat()
        canvas.drawRect(rect, paint)
        rect.left = width - WIDTH_DP * dp
        rect.top = height - LENGTH_DP * dp
        rect.right = width.toFloat()
        rect.bottom = height.toFloat()
        canvas.drawRect(rect, paint)
    }

    companion object {
        /**
         * 枠の幅(dp)
         */
        private const val WIDTH_DP = 12

        /**
         * 枠の長さ(dp)
         */
        private const val LENGTH_DP = 36
    }
}
