package me.nizarmah.trinity.app.facerecognition

import android.content.Context
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import kotlinx.coroutines.launch
import me.nizarmah.trinity.utils.camera.analysis.CameraAnalysisFactory
import me.nizarmah.trinity.utils.camera.analysis.CameraFrameAnalyzerLambdaType
import me.nizarmah.trinity.utils.camera.analysis.configs.LowResFaceDetectionCameraAnalysisConfig
import me.nizarmah.trinity.utils.face.classifier.FaceClassifier
import me.nizarmah.trinity.utils.face.classifier.configs.OracleFaceClassifierConfig
import me.nizarmah.trinity.utils.face.highlighter.highlight.FaceHighlight
import me.nizarmah.trinity.utils.face.detector.FaceDetector
import me.nizarmah.trinity.utils.face.detector.onDetectLambdaType
import me.nizarmah.trinity.utils.face.highlighter.highlight.LabeledRectangularFaceHighlight
import me.nizarmah.trinity.utils.facedetection.face.Face

class FaceRecognitionViewModel : ViewModel() {

    private lateinit var faceDetector: FaceDetector
    private lateinit var faceClassifier: FaceClassifier

    val highlightedFacesHashMap = HashMap<Int, Face>()
    val highlightedFacesLiveData = MutableLiveData<List<FaceHighlight>>()
    private val highlightDetectedFaces: onDetectLambdaType = { faces, frame ->
        faces.forEach {
            if (highlightedFacesHashMap.containsKey(it.trackingId)) {
                highlightedFacesHashMap[it.trackingId]?.updateFace(it, frame)
            } else {
                val face = Face(it, frame)
                highlightedFacesHashMap.put(it.trackingId, face)
                viewModelScope.launch {
                    highlightedFacesHashMap[it.trackingId]?.apply { face.classifyFace(faceClassifier) }
                }
            }
        }

        val highlightedFacesList = ArrayList<FaceHighlight>()
        highlightedFacesHashMap.values.forEach {
            highlightedFacesList.add(LabeledRectangularFaceHighlight(it))
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
            faceDetector.processImage(mediaImage, imageRotation, highlightDetectedFaces)
        }
    }

    fun initViewModel(context: Context) {
        initFaceDetector()
        initFaceClassifier(context)

        initCameraFrameAnalysis()
    }

    private fun initFaceDetector() {
        faceDetector = FaceDetector()
    }

    private fun initFaceClassifier(context: Context) {
        faceClassifier = FaceClassifier.getInstance(context, OracleFaceClassifierConfig)
    }

    private fun initCameraFrameAnalysis() {
        cameraFrameAnalysis = CameraAnalysisFactory.createCameraAnalysis(
            cameraFrameAnalysisConfig,
            analyzer = cameraFameAnalyzer
        )
    }

    override fun onCleared() {
        super.onCleared()

        faceDetector.stop()
    }
}