package me.nizarmah.trinity.utils.camera.view

import androidx.camera.view.CameraView

object CameraViewFactory {
    fun applyConfig(cameraView: CameraView, config: CameraViewConfig) {
        cameraView.apply {
            cameraLensFacing = config.cameraLensFacing

            keepScreenOn = config.keepScreenOn

            enableTorch(config.enableTorch)
            isPinchToZoomEnabled = config.isPinchToZoomEnabled
            touchscreenBlocksFocus = config.touchscreenBlocksFocus

            scaleType = config.scaleType
        }
    }
}