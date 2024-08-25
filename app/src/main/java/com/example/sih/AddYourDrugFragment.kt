package com.example.sih

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.sih.databinding.FragmentAddYourDrugBinding
import com.example.sih.datamodels.medclass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddYourDrugFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var imageUri: Uri? = null
    private lateinit var binding: FragmentAddYourDrugBinding

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            imageUri = uri
            binding.image.setImageURI(imageUri)
        } else {
            Toast.makeText(activity, "No image selected.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddYourDrugBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.image.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if (binding.name.text.toString().isEmpty() || binding.company.text.toString().isEmpty() ||
            binding.etAmount.text.toString().isEmpty() || imageUri == null
        ) {
            Toast.makeText(activity, "Please complete your information", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        if (imageUri == null) {
            Toast.makeText(activity, "Image URI is null.", Toast.LENGTH_SHORT).show()
            return
        }

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    storeData(uri)
                }.addOnFailureListener { exception ->
                    Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData(imageUrl: Uri?) {
        val data = mapOf(
            "drugname" to binding.name.text.toString(),
            "company" to binding.company.text.toString(),
            "amount" to binding.etAmount.text.toString(),
            "image" to (imageUrl?.toString() ?: "https://cdn.pixabay.com/photo/2017/05/23/21/01/jar-2338584_1280.jpg")
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .updateChildren(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Added Successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddYourDrugFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}