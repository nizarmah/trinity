package me.nizarmah.trinity.utils.face.classifier

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.custom.*
import me.nizarmah.trinity.utils.bitmap.BitmapUtils
import me.nizarmah.trinity.utils.bitmap.ImageType

// TODO : Add Support for `inputBatchSize` > 1
// TODO : Add FirebaseCustomRemoteModel Constructor

// TODO : Change Labels to an API

typealias onClassifyLambdaType = (name: String) -> Unit

class FaceClassifier {
    companion object {
        fun getInstance(context: Context, classifierConfig: FaceClassifierConfig): FaceClassifier {
            val model = FirebaseCustomLocalModel.Builder()
                .setAssetFilePath(classifierConfig.modelPath)
                .build()

            val labelsList = ArrayList<String>()
            context.assets.open(classifierConfig.labelsPath)
                .bufferedReader().forEachLine { labelsList.add(it) }

            return FaceClassifier(classifierConfig, model, labelsList)
        }
    }

    private var classifierConfig: FaceClassifierConfig

    private var interpreterOptions: FirebaseModelInterpreterOptions
    private var interpreter: FirebaseModelInterpreter? = null

    private var labels: ArrayList<String>

    private constructor(
        config: FaceClassifierConfig,
        model: FirebaseCustomLocalModel,
        labels: ArrayList<String>
    ) {
        classifierConfig = config

        interpreterOptions = FirebaseModelInterpreterOptions.Builder(model).build()
        interpreter = FirebaseModelInterpreter.getInstance(interpreterOptions)

        this.labels = labels

        setupInputOutputOptions()
    }

    private lateinit var inputOutputOptions: FirebaseModelInputOutputOptions

    private var inputBatchSize: Int = 0
    private var inputImageType: ImageType = ImageType.RGB

    private var inputWidth: Int = 0
    private var inputHeight: Int = 0

    fun setupInputOutputOptions() {
        inputBatchSize = classifierConfig.dataInputShape[0]
        inputImageType = classifierConfig.dataImageType

        inputWidth = classifierConfig.dataInputShape[1]
        inputHeight = classifierConfig.dataInputShape[2]

        inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, classifierConfig.dataInputType, classifierConfig.dataInputShape)
            .setOutputFormat(0, classifierConfig.dataOutputType, classifierConfig.dataOutputShape)
            .build()
    }

    fun classify(face: Bitmap, onClassify: onClassifyLambdaType) {
        val bitmap = Bitmap.createScaledBitmap(face, inputWidth, inputHeight, true)

        val input =
            Array(inputBatchSize) {
                BitmapUtils.getImageData(bitmap, inputImageType)
            }

        val inputs = FirebaseModelInputs.Builder()
            .add(input)
            .build()

        interpreter?.run(inputs, inputOutputOptions)?.apply {
            addOnSuccessListener {
                val out = it.getOutput<Array<FloatArray>>(0)

                val pred = getMostConfidentPrediction(out[0])
                onClassify(labels[pred])
            }

            addOnFailureListener {
                Log.d(
                    "FaceClassifier",
                    "Classification Error : ${(it as FirebaseMLException).code}"
                )
            }
        }
    }

    private fun getMostConfidentPrediction(probabilities: FloatArray): Int {
        var pred = -1
        probabilities.forEachIndexed { index, conf ->
            if ((pred == -1) || (conf > probabilities[pred])) {
                pred = index
            }
        }

        return pred
    }
}