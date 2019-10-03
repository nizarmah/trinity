package me.nizarmah.trinity.app.facedetection

import android.os.Bundle
import android.view.*
import androidx.camera.core.CameraX
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.main.fragment_facedetection.*
import me.nizarmah.trinity.R

class FaceDetectionFragment : Fragment(), LifecycleOwner {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_facedetection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)

        setupViews()
    }

    private fun initViews(view: View) {

    }

    private fun setupViews() {
        setupCameraView()
    }

    private fun setupCameraView() {
        view_camera.apply {
            bindToLifecycle(this@FaceDetectionFragment)

            cameraLensFacing = CameraX.LensFacing.BACK

            keepScreenOn = true
            enableTorch(false)
            isPinchToZoomEnabled = true
            touchscreenBlocksFocus = false
        }
    }
}