package com.example.sih.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sih.CustomView.TypeWriter
import com.example.sih.Model.DataResponse
import com.example.sih.R

class GeminiAdapter(var context: Context,var list: ArrayList<DataResponse>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val GEMINI =1;
        const val USER=0;
    }

    private inner class GeminiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val text: TypeWriter = itemView.findViewById(R.id.tv_gemini_response)

        fun bind(position: Int){

            val data= list[position]

            if(list.size-1==position){
                text.animateText(data.prompt)
                text.setCharacterDelay(30)
            }else{
                text.setText(data.prompt)
            }
        }
    }

    private inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var text: TextView = itemView.findViewById(R.id.tv_user_response)
        var image: ImageView= itemView.findViewById(R.id.iv_user_res)

        fun bind(position: Int){
            val data=list[position]
            text.text = data.prompt
            if(data.imageUri.isNotEmpty()){
                image.setImageURI(Uri.parse(data.imageUri))
            }else{
                image.visibility=View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType== GEMINI){
            return GeminiViewHolder(LayoutInflater.from(context).inflate(R.layout.gemini_layout,parent,false))
        }else{
            return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.user_layout,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].isUser
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].isUser== GEMINI){
            (holder as GeminiViewHolder).bind(position)
        }else{
            (holder as UserViewHolder).bind(position)
        }
    }

}