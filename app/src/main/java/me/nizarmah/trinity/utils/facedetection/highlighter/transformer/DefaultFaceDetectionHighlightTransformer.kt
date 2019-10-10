package me.nizarmah.trinity.utils.facedetection.highlighter.transformer

object DefaultFaceDetectionHighlightTransformer : FaceDetectionHighlightTransformer {
    override val widthScaleFactor: Float = 1.0f
    override val heightScaleFactor: Float = 1.0f

    override val leftOffset: Int = 0
    override val topOffset: Int = 0
}