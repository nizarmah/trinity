package me.nizarmah.trinity.utils.camera.analysis

import android.os.Handler
import android.os.HandlerThread
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysisConfig
import androidx.camera.core.ImageProxy

typealias CameraFrameAnalyzerLambdaType = (imageProxy: ImageProxy?, rotation: Int) -> Unit

object CameraAnalysisFactory {
    private fun createAnalysisConfig(cameraAnalysisConfig: CameraAnalysisConfig): ImageAnalysisConfig {
        val imageAnalysisConfig = ImageAnalysisConfig.Builder().apply {
            setTargetResolution(cameraAnalysisConfig.resolution)
            setImageReaderMode(cameraAnalysisConfig.readerMode)

            val analyzerThread =
                HandlerThread(cameraAnalysisConfig::class.java.simpleName).apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
        }.build()

        return imageAnalysisConfig
    }

    fun createCameraAnalysis(
        cameraAnalysisConfig: CameraAnalysisConfig,
        analyzer: CameraFrameAnalyzerLambdaType
    ): ImageAnalysis {
        val imageAnalysisConfig = createAnalysisConfig(cameraAnalysisConfig)

        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        imageAnalysis.setAnalyzer(analyzer)

        return imageAnalysis
    }
}