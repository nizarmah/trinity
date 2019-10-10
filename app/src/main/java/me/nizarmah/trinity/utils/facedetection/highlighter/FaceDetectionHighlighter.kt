package me.nizarmah.trinity.utils.facedetection.highlighter

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.view.CameraView
import me.nizarmah.trinity.utils.camera.analysis.CameraAnalysisConfig
import me.nizarmah.trinity.utils.camera.view.CameraViewConfig
import me.nizarmah.trinity.utils.facedetection.highlighter.highlight.FaceDetectionHighlight
import me.nizarmah.trinity.utils.facedetection.highlighter.transformer.DefaultFaceDetectionHighlightTransformer
import me.nizarmah.trinity.utils.facedetection.highlighter.transformer.FaceDetectionHighlightTransformer
import kotlin.math.round

class FaceDetectionHighlighter(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val lock: Object = Object()

    private var cameraFrameResolution: Size = Size(1, 1)

    private var cameraViewSize: Size = Size(0, 0)
    private var cameraViewResolution: Size = Size(1, 1)

    public var transformer: FaceDetectionHighlightTransformer =
        DefaultFaceDetectionHighlightTransformer

    private var highlights: ArrayList<FaceDetectionHighlight> = ArrayList()

    fun attachCameraView(cameraView: CameraView, cameraViewConfig: CameraViewConfig) {
        synchronized(lock) {
            cameraViewSize = Size(cameraView.measuredWidth, cameraView.measuredHeight)
        }

        postInvalidate()
    }

    fun attachCameraAnalysisConfig(cameraAnalysisConfig: CameraAnalysisConfig) {
        synchronized(lock) {
            cameraFrameResolution = cameraAnalysisConfig.resolution

            var frameAspectRatio: Float = 1.0f
            cameraFrameResolution.let { frameResolution ->
                frameAspectRatio =
                    (frameResolution.width.toFloat() / frameResolution.height.toFloat())
            }

            // Change this to support different ScaleTypes
            cameraViewResolution = Size(
                round(cameraViewSize.height * frameAspectRatio).toInt(),
                cameraViewSize.height
            )
        }

        updateTransformer()

        postInvalidate()
    }

    fun updateTransformer() {
        synchronized(lock) {
            transformer = object : FaceDetectionHighlightTransformer {
                override val widthScaleFactor: Float =
                    (cameraViewResolution.width / cameraFrameResolution.width.toFloat())
                override val heightScaleFactor: Float =
                    (cameraViewResolution.height / cameraFrameResolution.height.toFloat())

                override val leftOffset: Int =
                    (cameraViewSize.width - cameraViewResolution.width) / 2
                override val topOffset: Int =
                    (cameraViewSize.height - cameraViewResolution.height) / 2
            }

            highlights.forEach {
                it.transform(transformer)
            }
        }

        postInvalidate()
    }

    fun clear() {
        synchronized(lock) {
            highlights.clear()
        }

        postInvalidate()
    }

    fun add(highlight: FaceDetectionHighlight) {
        synchronized(lock) {
            highlight.transform(transformer)
            highlights.add(highlight)
        }

        postInvalidate()
    }

    fun remove(highlight: FaceDetectionHighlight) {
        synchronized(lock) {
            highlights.remove(highlight)
        }

        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        synchronized(lock) {
            canvas?.apply {
                highlights.forEach {
                    it.highlightOn(this, this@FaceDetectionHighlighter)
                }
            }
        }
    }
}