package com.bblackbelt.githubusers.utils

import android.graphics.*
import com.squareup.picasso.Transformation

class RoundedTransformation constructor(url: String?) : Transformation {

    private var mUrl: String? = url

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val radius = Math.min(source.width, source.height) / 2.toFloat()

        canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), radius, radius, paint)

        if (source != output) {
            source.recycle()
        }

        return output
    }

    override fun key(): String {
        return "rounded$mUrl"
    }
}