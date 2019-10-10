package me.nizarmah.trinity.utils.facedetection.processor

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import java.io.IOException
import java.lang.Exception

class FaceDetectionProcessor : FaceDetectionProcessorBase() {
    override val TAG: String = "FaceDetectionProcessor"

    override lateinit var detector: FirebaseVisionFaceDetector
    override lateinit var options: FirebaseVisionFaceDetectorOptions

    init {
        options = FirebaseVisionFaceDetectorOptions.Builder().apply {
            setPerformanceMode(FirebaseVisionFaceDetectorOptions.FAST)
            enableTracking()
        }.build()

        detector = FirebaseVision.getInstance().getVisionFaceDetector(options)
    }
}