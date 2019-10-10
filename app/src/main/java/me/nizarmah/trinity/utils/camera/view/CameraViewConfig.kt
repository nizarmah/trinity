package me.nizarmah.trinity.utils.camera.view

import androidx.camera.core.CameraX
import androidx.camera.view.CameraView

interface CameraViewConfig {
    val cameraLensFacing: CameraX.LensFacing

    val keepScreenOn: Boolean
    val enableTorch: Boolean
    val isPinchToZoomEnabled: Boolean
    val touchscreenBlocksFocus: Boolean

    val scaleType: CameraView.ScaleType
}