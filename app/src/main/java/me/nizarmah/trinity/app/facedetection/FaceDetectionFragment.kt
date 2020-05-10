package me.nizarmah.trinity.app.facedetection

import android.os.Bundle
import android.view.*
import androidx.camera.core.CameraX
import androidx.camera.view.CameraView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import me.nizarmah.trinity.R
import me.nizarmah.trinity.utils.face.highlighter.FaceHighlighter
import me.nizarmah.trinity.utils.camera.view.CameraViewFactory
import me.nizarmah.trinity.utils.camera.view.configs.FaceDetectionCameraViewConfig

class FaceDetectionFragment : Fragment(), LifecycleOwner {

    private lateinit var viewModel: FaceDetectionViewModel
    private val viewModelInstance: Class<FaceDetectionViewModel> =
        FaceDetectionViewModel::class.java

    private lateinit var cameraView: CameraView
    private val cameraViewConfig = FaceDetectionCameraViewConfig

    private lateinit var highlighterFaces: FaceHighlighter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_facedetection, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(viewModelInstance)
        viewModel.initViewModel(view.context)

        initViews(view)

        setupViews()

        setupObservables()
    }

    private fun initViews(view: View) {
        view.apply {
            cameraView = findViewById(R.id.view_camera)

            highlighterFaces = findViewById(R.id.highlighter_faces)
        }
    }

    private fun setupViews() {
        cameraView.post {
            setupCameraView()

            setupCameraAnalysis()
        }
    }

    private fun setupCameraView() {
        cameraView.apply {
            bindToLifecycle(this@FaceDetectionFragment)

            CameraViewFactory.applyConfig(cameraView, cameraViewConfig)

            highlighterFaces.attachCameraView(cameraView, cameraViewConfig)
        }
    }

    private fun setupCameraAnalysis() {
        CameraX.bindToLifecycle(this, viewModel.cameraFrameAnalysis)

        highlighterFaces.attachCameraAnalysisConfig(viewModel.cameraFrameAnalysisConfig)
    }

    private fun setupObservables() {
        viewModel.highlightedFacesLiveData.observe(this, Observer { highlightedFaces ->
            highlighterFaces.clear()

            highlightedFaces.forEach {
                highlighterFaces.add(it)
            }

            highlighterFaces.postInvalidate()
        })
    }
}