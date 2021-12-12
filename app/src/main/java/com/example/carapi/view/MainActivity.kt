package com.example.carapi.view
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.carapi.R
import com.example.carapi.adapter.RecyclerViewAdapter
import com.example.carapi.fragments.DeleteFragment
import com.example.carapi.fragments.GetFragment
import com.example.carapi.fragments.PostFragment
import com.example.carapi.fragments.UpdateFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),RecyclerViewAdapter.Listener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getFragment = GetFragment()
        val postFragment = PostFragment()
        val updateFragment = UpdateFragment()
        val deleteFragment = DeleteFragment()
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