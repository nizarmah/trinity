package me.nizarmah.trinity.utils.face.highlighter.highlight

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import me.nizarmah.trinity.utils.face.highlighter.FaceHighlighter
import me.nizarmah.trinity.utils.face.highlighter.transformer.FaceHighlightTransformer
import me.nizarmah.trinity.utils.facedetection.face.Face
import kotlin.math.abs
import kotlin.math.ceil

class RectangularFaceHighlight(val face: Face) :
    FaceHighlight(face) {
    private val boxPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4.0f
    }

    private var boxFace = face.boundingBox

    override fun highlightOn(canvas: Canvas, highlighter: FaceHighlighter) {
        canvas.drawRect(boxFace, boxPaint)
    }

    override fun transform(transformer: FaceHighlightTransformer) {
        val originalBoxWidth = abs(boxFace.left - boxFace.right)
        val originalBoxHeight = abs(boxFace.top - boxFace.bottom)

        val newBoxWidth = ceil(originalBoxWidth * transformer.widthScaleFactor).toInt()
        val newBoxHeight = ceil(originalBoxHeight * transformer.heightScaleFactor).toInt()

        boxFace.left =
            (boxFace.left * transformer.widthScaleFactor).toInt() + transformer.leftOffset
        boxFace.top =
            (boxFace.top * transformer.heightScaleFactor).toInt() + transformer.topOffset

        boxFace.right = boxFace.left + newBoxWidth
        boxFace.bottom = boxFace.top + newBoxHeight
    }
}