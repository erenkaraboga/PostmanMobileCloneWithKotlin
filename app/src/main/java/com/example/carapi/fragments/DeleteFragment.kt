package com.example.carapi.fragments
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.carapi.R
import com.example.carapi.service.CarApi
import kotlinx.android.synthetic.main.fragment_delete.*
import kotlinx.android.synthetic.main.fragment_get.*
import kotlinx.coroutines.*

private var job : Job?=null
class DeleteFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete, container, false)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        deleteButton.setOnClickListener {
            val id = editTextId.text.toString().toIntOrNull()
            if (id == null) {
                editTextId.error="Id required"
               return@setOnClickListener }
            else {
                deleteData()
            }
        }
    }
    private fun deleteData(){
        val id = editTextId.text.toString()
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = CarApi().delete(id.toInt())
            withContext(Dispatchers.Main) {
                   if(response.isSuccessful) {
                       Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show()
                    }
                   else {
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show()
                   }
                }
            }
        }
    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }
}
