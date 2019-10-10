package me.nizarmah.trinity.utils.camera.analysis.configs

import android.util.Size
import androidx.camera.core.ImageAnalysis
import me.nizarmah.trinity.utils.camera.analysis.CameraAnalysisConfig

object LowResFaceDetectionCameraAnalysisConfig : CameraAnalysisConfig {
    override val resolution: Size = Size(240, 320)
    override val readerMode: ImageAnalysis.ImageReaderMode = ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
}