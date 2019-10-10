package me.nizarmah.trinity.utils.camera.analysis

import android.util.Size
import androidx.camera.core.ImageAnalysis

interface CameraAnalysisConfig {
    val resolution: Size
    val readerMode: ImageAnalysis.ImageReaderMode
}