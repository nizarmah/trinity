package me.nizarmah.trinity.utils.face.highlighter.transformer

object DefaultFaceHighlightTransformer : FaceHighlightTransformer {
    override val widthScaleFactor: Float = 1.0f
    override val heightScaleFactor: Float = 1.0f

    override val leftOffset: Int = 0
    override val topOffset: Int = 0
}