package com.example.sih

import android.Manifest.permission.CAMERA
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sih.Model.DataResponse
import com.example.sih.databinding.ActivityDetectMedicineBinding
import com.example.sih.datamodels.medclass
import com.google.ai.client.generativeai.GenerativeModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetectMedicine : AppCompatActivity() {

    private var bmp: Bitmap? = null
    private lateinit var captureImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityDetectMedicineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectMedicineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the ActivityResultLauncher for capturing images
        captureImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                bmp = data?.extras?.get("data") as Bitmap?
                bmp?.let {
                    binding.imagecap.setImageBitmap(it)
                }
            }
        }



        binding.detect.setOnClickListener {
            detectText()

        }

        binding.button6.setOnClickListener {
            if (checkPermission()) {
                captureImage()
            } else {
                requestPermission()
            }
        }


    }

    private fun checkPermission(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(applicationContext, CAMERA)
        return cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        val PERMISSION_CODE = 200
        ActivityCompat.requestPermissions(this, arrayOf(CAMERA), PERMISSION_CODE)
    }

    private fun captureImage() {
        val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (i.resolveActivity(packageManager) != null) {
            captureImageLauncher.launch(i)
        }
        else{
            Toast.makeText(this,"hell no",Toast.LENGTH_SHORT).show()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                captureImage()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun detectText() {
        if (bmp != null) {
            val image = InputImage.fromBitmap(bmp!!, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val result = StringBuilder()
                    for (block in visionText.textBlocks) {
                        for (line in block.lines) {
                            for (element in line.elements) {
                                result.append(element.text)
                            }
                            binding.textans.text = block.text // Display the text detected
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Text detection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Bitmap is null", Toast.LENGTH_SHORT).show()
        }
        val generativeModel = GenerativeModel(
            modelName = "gemini-1.5-flash",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyBgseuokv8WMY4J0a8vh5sZHGqyQuYJ0UU"
        )
        var prompt = "You are a physician. Your job is to extract information from queries about the medicine in JSON format, here is a example" +
                "Query= Luliconazole Cream IP 1% w/w"+"Answer=[{\"name\":\"Luliconazole Cream\", \"company\":\"ALKEM\",\"type\":\"ointment\"}]"+"int the same way the next query is "+binding.textans.text.toString()+" so the answer will be? and if any information is missing write something yourself"
        GlobalScope.launch {
            val response =generativeModel.generateContent(prompt)
            runOnUiThread{
                binding.tvGeminiResponse.setText(response.text)
            }
        }

    }
}