package com.example.sih

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sih.databinding.ItemLayoutBinding
import com.example.sih.datamodels.medclass

class InventoryAdapter(private var list: List<medclass>) : RecyclerView.Adapter<InventoryAdapter.ViewHolder>(){
    class ViewHolder(val binding : ItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.apply {
            tvMedicineName.text = data.drugname
            companyname.text = data.company
            amount.text=data.amount
            Glide.with(holder.itemView.context).load(data.image).placeholder(R.drawable.meds)
                .into(ivMedicine)
            }
        }
//
fun setData(newData: List<medclass>) {
    list = newData as ArrayList<medclass>
    notifyDataSetChanged()
}
}