package me.nizarmah.trinity.utils.face.detector

import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions

class FaceDetector : FaceDetectorBase() {
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