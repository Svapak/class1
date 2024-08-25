package com.example.sih

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sih.datamodels.medclass
import kotlinx.coroutines.launch
import org.json.JSONArray

class MedicineViewModel(application: Application) : AndroidViewModel(application){
    private val _list = MutableLiveData<List<medclass>>()
    val list: LiveData<List<medclass>> get() = _list
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getList(){
        viewModelScope.launch {
            _loading.value = true
            val newList = mutableListOf<medclass>()
            val jsonStr = getApplication<Application>().applicationContext
                .assets.open("medicine.json").bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val medicineData = medclass(
                    drugname = jsonObject.getString("name"),
                    amount = jsonObject.getString("price"),
                    image = jsonObject.getString("image"),
                    company = jsonObject.getString("treatment")
                )
                newList.add(medicineData)
            }
            _list.value = newList
            _loading.value = false
        }
    }
}