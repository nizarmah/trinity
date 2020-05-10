package me.nizarmah.trinity.utils.facedetection.face

import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.nizarmah.trinity.utils.face.classifier.FaceClassifier
import me.nizarmah.trinity.utils.face.classifier.onClassifyLambdaType

class Face(visionFace: FirebaseVisionFace, var frame: Bitmap) {
    lateinit var boundingBox: Rect
    var trackingId: Int = -1

    var visionFace: FirebaseVisionFace = visionFace
        set(value) {
            field = value

            boundingBox = value.boundingBox
            trackingId = value.trackingId
        }

    init {
        this.updateFace(visionFace, frame)
    }

    var label = ""
        private set

    fun getFaceBitmap(): Bitmap {
        val faceX: Int =
            if ((boundingBox.centerX() + boundingBox.width()) > frame.width) 0 else boundingBox.centerX()
        val faceY: Int =
            if ((boundingBox.centerY() + boundingBox.height()) > frame.height) 0 else boundingBox.centerY()

        val faceWidth: Int =
            if ((faceX + boundingBox.width()) > frame.width) frame.width else boundingBox.width()
        val faceHeight: Int =
            if ((faceY + boundingBox.height()) > frame.height) frame.height else boundingBox.height()

        return Bitmap.createBitmap(frame, faceX, faceY, faceWidth, faceHeight)
    }

    fun updateFace(visionFace: FirebaseVisionFace, frame: Bitmap) {
        this.frame = frame
        this.visionFace = visionFace
    }

    suspend fun classifyFace(
        faceClassifier: FaceClassifier,
        onClassify: onClassifyLambdaType = {}
    ) {
        faceClassifier.classify(this.getFaceBitmap(), {
            this.label = it

            onClassify(it)
        })
    }
}