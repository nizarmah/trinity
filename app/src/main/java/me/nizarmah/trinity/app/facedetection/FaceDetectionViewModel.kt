package me.nizarmah.trinity.app.facedetection

import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import me.nizarmah.trinity.utils.camera.analysis.CameraAnalysisFactory
import me.nizarmah.trinity.utils.camera.analysis.CameraFrameAnalyzerLambdaType
import me.nizarmah.trinity.utils.camera.analysis.configs.LowResFaceDetectionCameraAnalysisConfig
import me.nizarmah.trinity.utils.facedetection.highlighter.highlight.FaceDetectionHighlight
import me.nizarmah.trinity.utils.facedetection.highlighter.highlight.RectangularFaceDetectionHighlight
import me.nizarmah.trinity.utils.facedetection.processor.FaceDetectionProcessor

class FaceDetectionViewModel : ViewModel() {

    private lateinit var faceDetectionProcessor: FaceDetectionProcessor

    val highlightedFacesLiveData = MutableLiveData<List<FaceDetectionHighlight>>()
    private val highlightDetectedFaces: (List<FirebaseVisionFace>) -> Unit = { faces ->
        val highlightedFacesList = ArrayList<FaceDetectionHighlight>()

        faces.forEach {
            highlightedFacesList.add(RectangularFaceDetectionHighlight(it))
        }

        highlightedFacesLiveData.postValue(highlightedFacesList)
    }

    public lateinit var cameraFrameAnalysis: ImageAnalysis
    public val cameraFrameAnalysisConfig = LowResFaceDetectionCameraAnalysisConfig

    private val cameraFameAnalyzer: CameraFrameAnalyzerLambdaType = { imageProxy, rotation ->
        val mediaImage = imageProxy?.image

        val imageRotation = when (rotation) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
        }

        mediaImage?.let {
            faceDetectionProcessor.processImage(mediaImage, imageRotation, highlightDetectedFaces)
        }
    }

    fun initViewModel() {
        initFaceDetectorProcessor()

        initCameraFrameAnalysis()
    }

    private fun initFaceDetectorProcessor() {
        faceDetectionProcessor = FaceDetectionProcessor()
    }

    private fun initCameraFrameAnalysis() {
        cameraFrameAnalysis = CameraAnalysisFactory.createCameraAnalysis(
            cameraFrameAnalysisConfig,
            analyzer = cameraFameAnalyzer
        )
    }

    override fun onCleared() {
        super.onCleared()

        faceDetectionProcessor.stop()
    }
}