package com.fabianshallari.pixabay.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatImageView


class SquareImageView : AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, @Nullable attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        if (measuredWidth > measuredHeight) {
            setMeasuredDimension(measuredHeight, measuredHeight)
        } else {
            setMeasuredDimension(measuredWidth, measuredWidth)
        }

        setMeasuredDimension(width, width)
    }
}