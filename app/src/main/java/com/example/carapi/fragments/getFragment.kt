package com.example.carapi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carapi.R
import com.example.carapi.adapter.RecyclerViewAdapter
import com.example.carapi.model.BrandModel
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_get.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var job : Job?=null
private var brandModels : ArrayList<BrandModel>?=null
private var recyclerViewAdapter: RecyclerViewAdapter?=null

class getFragment : Fragment(),RecyclerViewAdapter.Listener {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get, container, false)
    }
    private fun loadData() {
        var progress: ProgressBar = this.getprogressBar
        progress.visibility = View.VISIBLE
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = CarApi().getData()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        brandModels = ArrayList(it)
                        brandModels.let {
                            recyclerViewAdapter =
                                RecyclerViewAdapter(brandModels!!, this@getFragment)
                            recyclerView.adapter = recyclerViewAdapter
                            progress.visibility = View.INVISIBLE

                        }
                    }
                }

            }


        }

    }
}