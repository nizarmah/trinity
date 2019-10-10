package me.nizarmah.trinity.utils.facedetection.highlighter.highlight

import android.graphics.Canvas
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import me.nizarmah.trinity.utils.facedetection.highlighter.FaceDetectionHighlighter
import me.nizarmah.trinity.utils.facedetection.highlighter.transformer.FaceDetectionHighlightTransformer

abstract class FaceDetectionHighlight(face: FirebaseVisionFace) {
    abstract fun highlightOn(canvas: Canvas, highlighter: FaceDetectionHighlighter)

    abstract fun transform(transformer: FaceDetectionHighlightTransformer)
}