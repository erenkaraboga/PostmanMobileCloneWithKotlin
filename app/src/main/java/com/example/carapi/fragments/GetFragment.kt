package com.example.carapi.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carapi.R
import com.example.carapi.adapter.RecyclerViewAdapter
import com.example.carapi.model.BrandModel
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_get.*
import kotlinx.coroutines.*

private var job : Job?=null
private var brandModels : ArrayList<BrandModel>?=null
private var recyclerViewAdapter: RecyclerViewAdapter?=null
private  var customDialog : Dialog?=null
class GetFragment : Fragment(),RecyclerViewAdapter.Listener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        customDialog= Dialog(requireActivity())
        customDialog!!.setContentView(R.layout.custom_dialog_progress)
    }
    override fun onStart() {
        super.onStart()
        loadData()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutManager: RecyclerView.LayoutManager= LinearLayoutManager(context)
        recyclerView.layoutManager=layoutManager
        loadData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
         return inflater.inflate(R.layout.fragment_get, container, false)
    }
    private fun loadData() {
        customDialog?.setCanceledOnTouchOutside(false)
        customDialog?.show()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = CarApi().getData()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        brandModels = ArrayList(it)
                        brandModels.let {
                            recyclerViewAdapter = RecyclerViewAdapter(brandModels!!, this@GetFragment)
                              recyclerView.adapter = recyclerViewAdapter
                            customDialog?.cancel()
                        }
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