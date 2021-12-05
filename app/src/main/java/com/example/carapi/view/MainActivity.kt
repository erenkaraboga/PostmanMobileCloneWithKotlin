package com.example.carapi.view

import android.hardware.lights.LightState
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carapi.R
import com.example.carapi.adapter.RecyclerViewAdapter
import com.example.carapi.fragments.deleteFragment
import com.example.carapi.fragments.getFragment
import com.example.carapi.fragments.postFragment
import com.example.carapi.fragments.updateFragment
import com.example.carapi.model.BrandModel
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_get.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener{
    private val BASE_URL ="https://carrestfulapi.azurewebsites.net/"
    private var brandModels : ArrayList<BrandModel>?=null
    private var recyclerViewAdapter: RecyclerViewAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getFragment = getFragment()
        val postFragment = postFragment()
        val updateFragment = updateFragment()
        val deleteFragment = deleteFragment()
        bottom_navi_bar.setOnItemSelectedListener{
            when(it.itemId){
                R.id.get -> makeCurrentFragment(getFragment)
                R.id.post -> makeCurrentFragment(postFragment)
                R.id.update -> makeCurrentFragment(updateFragment)
                R.id.delete -> makeCurrentFragment(deleteFragment)

            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper,fragment).commit()
        }
    }
}