package me.nizarmah.trinity.utils.face.highlighter.highlight

import android.graphics.Canvas
import me.nizarmah.trinity.utils.face.highlighter.FaceHighlighter
import me.nizarmah.trinity.utils.face.highlighter.transformer.FaceHighlightTransformer
import me.nizarmah.trinity.utils.facedetection.face.Face

abstract class FaceHighlight(face: Face) {
    abstract fun highlightOn(canvas: Canvas, highlighter: FaceHighlighter)

    abstract fun transform(transformer: FaceHighlightTransformer)
}