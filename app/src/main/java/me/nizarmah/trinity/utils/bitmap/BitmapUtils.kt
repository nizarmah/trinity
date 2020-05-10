package me.nizarmah.trinity.utils.bitmap

import android.graphics.Bitmap
import android.graphics.Color

// TODO : Add Support for different Image Types (RGBA and GRAYSCALE)

object BitmapUtils {
    fun getImageData(bitmap: Bitmap, imageType: ImageType): Array<Array<FloatArray>> {
        val imageWidth = bitmap.width
        val imageHeight = bitmap.height

        val imageData =
            Array(imageWidth) {
                Array(imageHeight) {
                    FloatArray(imageType.dimens)
                }
            }

        for (x in 0 until imageWidth) {
            for (y in 0 until imageHeight) {
                val pixel = bitmap.getPixel(x, y)

                imageData[x][y][0] = Color.red(pixel) / 255.0f
                imageData[x][y][1] = Color.green(pixel) / 255.0f
                imageData[x][y][2] = Color.blue(pixel) / 255.0f
            }
        }

        return imageData
    }
}