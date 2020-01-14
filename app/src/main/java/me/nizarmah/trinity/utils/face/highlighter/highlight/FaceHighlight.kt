package me.nizarmah.trinity.utils.face.highlighter.highlight

import android.graphics.Canvas
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import me.nizarmah.trinity.utils.face.highlighter.FaceHighlighter
import me.nizarmah.trinity.utils.face.highlighter.transformer.FaceHighlightTransformer

abstract class FaceHighlight(face: FirebaseVisionFace) {
    abstract fun highlightOn(canvas: Canvas, highlighter: FaceHighlighter)

    abstract fun transform(transformer: FaceHighlightTransformer)
}