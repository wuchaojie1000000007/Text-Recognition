package com.example.textrecognition

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.textrecognition.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition

const val pictureFileName = "pic3.png"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bitmap = assetsToBitMap(pictureFileName)
        bitmap.let {
            binding.imageView.setImageBitmap(it)
            Log.d(TAG, "Image width: ${it.width}, Image height: ${it.height}, ${it.byteCount}")
        }

        val image = InputImage.fromBitmap(bitmap, 0)

        val recognizer = TextRecognition.getClient()

        val result = recognizer.process(image)
            .addOnSuccessListener {
                Log.d(TAG, "resultText: ${it.text}, textBlocks Size: ${it.textBlocks.size}")
                val resultText = it.text
                binding.textView.text = resultText
            }.addOnFailureListener {
                Log.d(TAG, "Task failed, message ${it.message}, localizedMessage ${it.localizedMessage}")
                Log.d(TAG, "Task failed, stackTrace ${it.stackTrace}, suppressed ${it.suppressed}, cause ${it.cause}")

            }.addOnCompleteListener {
                Log.d(TAG, "Task complete")
            }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}

private fun Context.assetsToBitMap(filename: String): Bitmap {
    return with(assets.open(filename)) {
        BitmapFactory.decodeStream(this)
    }

}