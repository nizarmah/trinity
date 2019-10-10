package me.nizarmah.trinity.utils.camera.view.configs

import androidx.camera.core.CameraX
import androidx.camera.view.CameraView
import me.nizarmah.trinity.utils.camera.view.CameraViewConfig

object FaceDetectionCameraViewConfig : CameraViewConfig {
    override val cameraLensFacing: CameraX.LensFacing = CameraX.LensFacing.BACK

    override val keepScreenOn: Boolean = true
    override val enableTorch: Boolean = false
    override val isPinchToZoomEnabled: Boolean = false
    override val touchscreenBlocksFocus: Boolean = false

    override val scaleType: CameraView.ScaleType = CameraView.ScaleType.CENTER_CROP
}