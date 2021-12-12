package com.example.carapi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.carapi.R
import com.example.carapi.adapter.RecyclerViewAdapter
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.fragment_delete.*
import kotlinx.android.synthetic.main.fragment_get.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val BASE_URL ="https://carrestfulapi.azurewebsites.net/"
private var job : Job?=null
class deleteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_delete, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        deleteButton.setOnClickListener {
            var id = editTextId.text.toString().toIntOrNull()
            if (id == null){
                editTextId.error="Id required"
               return@setOnClickListener
            }else{
                deleteData()
            }
        }
    }
    fun deleteData(){
        Toast.makeText(context,"If this id is in the records, it will be deleted.",Toast.LENGTH_SHORT).show()
        var id = editTextId.text.toString()

        job = CoroutineScope(Dispatchers.IO).launch {
            val response = CarApi().delete(id.toInt())
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {

                        }
                    }
                }
            }
        }
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
    }
