package com.example.sih

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import android.provider.MediaStore
import android.provider.Settings.Global
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.sih.Adapter.GeminiAdapter
import com.example.sih.Model.DataResponse
import com.example.sih.databinding.ActivityChatBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private var imageUri : Uri? = null

    lateinit var recyclerView: RecyclerView
    var bitmap: Bitmap?=null
    //lateinit var imageUri: String
    var responseData = arrayListOf<DataResponse>()
    lateinit var adapter: GeminiAdapter

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver,it)
        binding.imageView2.setImageURI(imageUri)
    }

//    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri->
//            imageUri= uri
//            bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver,uri)
//            binding.imageView2.setImageURI(uri)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        recyclerView= findViewById(R.id.recy)

        adapter = GeminiAdapter(this,responseData)
        recyclerView.adapter=adapter

        binding.imageView2.setOnClickListener{
            Toast.makeText(this,"clicked", Toast.LENGTH_SHORT).show()
            selectImage.launch("image/*")
        }

        binding.button.setOnClickListener{
            Log.d("get","the button is clicked")
            Toast.makeText(this,"clicked", Toast.LENGTH_SHORT).show()
            if (binding.editText.text!=null){
                val generativeModel = GenerativeModel(
                    modelName = "gemini-1.5-flash",
                    // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                    apiKey = "AIzaSyBgseuokv8WMY4J0a8vh5sZHGqyQuYJ0UU"
                )

                var prompt = binding.editText.text.toString()
                binding.editText.setText("")

                if(bitmap!= null){
                    bitmap=null
                    imageUri=null
                    responseData.add(DataResponse(0,prompt,imageUri = imageUri.toString()))
                    adapter.notifyDataSetChanged()
                    val inputContent = content{
                        image(bitmap!!)
                        text(prompt)
                    }

                    GlobalScope.launch {
                        val response= generativeModel.generateContent(inputContent)
                        runOnUiThread{
                            responseData.add(DataResponse(1,response.text!!,""))
                            adapter.notifyDataSetChanged()
                        }
                    }


                }else{
                    responseData.add(DataResponse(0,prompt,""))
                    adapter.notifyDataSetChanged()

                    GlobalScope.launch {
                        val response= generativeModel.generateContent(prompt)

                        runOnUiThread{
                            responseData.add(DataResponse(1,response.text!!,""))
                            adapter.notifyDataSetChanged()
                        }

                    }
                }
            }
        }
    }
}