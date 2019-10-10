package me.nizarmah.trinity.utils.facedetection.processor

import android.media.Image
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import java.io.IOException
import java.lang.Exception

typealias onDetectLambdaType = (List<FirebaseVisionFace>) -> Unit

abstract class FaceDetectionProcessorBase() {
    abstract val TAG: String

    abstract val detector: FirebaseVisionFaceDetector
    abstract val options: FirebaseVisionFaceDetectorOptions

    fun processImage(image: Image, rotation: Int, onDetect: onDetectLambdaType) {
        val visionImage = FirebaseVisionImage.fromMediaImage(image, rotation)

        detectInVisionImage(visionImage, onDetect)
    }

    private fun detectInImage(image: FirebaseVisionImage): Task<List<FirebaseVisionFace>> = detector.detectInImage(image)

    protected fun detectInVisionImage(image: FirebaseVisionImage, onDetect: onDetectLambdaType) {
        detectInImage(image)
            .addOnSuccessListener { faces ->
                onDetect(faces)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    open fun onFailure(exception: Exception) {
        Log.e(TAG, "Face Detection Failed : $exception")
    }

    fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector : $e")
        }
    }
}