package me.nizarmah.trinity.utils.facedetection.highlighter.highlight

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import me.nizarmah.trinity.utils.facedetection.highlighter.FaceDetectionHighlighter
import me.nizarmah.trinity.utils.facedetection.highlighter.transformer.FaceDetectionHighlightTransformer
import kotlin.math.abs
import kotlin.math.ceil

class RectangularFaceDetectionHighlight(val face: FirebaseVisionFace) :
    FaceDetectionHighlight(face) {
    private val boxPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 4.0f
    }

    private var boxFace = face.boundingBox

    override fun highlightOn(canvas: Canvas, highlighter: FaceDetectionHighlighter) {
        canvas.drawRect(boxFace, boxPaint)
    }

    override fun transform(transformer: FaceDetectionHighlightTransformer) {
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