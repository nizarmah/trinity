package me.nizarmah.trinity.utils.facedetection.highlighter.transformer

interface FaceDetectionHighlightTransformer {
    val widthScaleFactor: Float
    val heightScaleFactor: Float

    val leftOffset: Int
    val topOffset: Int
}