package com.example.carapi.view
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.carapi.R
import com.example.carapi.adapter.RecyclerViewAdapter
import com.example.carapi.fragments.deleteFragment
import com.example.carapi.fragments.getFragment
import com.example.carapi.fragments.postFragment
import com.example.carapi.fragments.updateFragment

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener{
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