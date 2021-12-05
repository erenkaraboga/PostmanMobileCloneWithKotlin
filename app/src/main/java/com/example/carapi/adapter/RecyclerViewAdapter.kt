package com.example.carapi.adapter

import android.app.Activity
import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.carapi.R
import com.example.carapi.model.BrandModel
import com.example.carapi.view.MainActivity
import kotlinx.android.synthetic.main.row_layout.view.*

class RecyclerViewAdapter(private val list: List<BrandModel>, private val listener:Listener ) : RecyclerView.Adapter<RecyclerViewAdapter.RowHolder>() {
    interface Listener{
        fun onItemClick(brandModel: BrandModel){

        }
    }
    class RowHolder(view : View) : RecyclerView.ViewHolder(view) {
             fun bind(brandModel: BrandModel,listener: Listener){
                 if (brandModel.imageUrl==null){
                     itemView.setOnClickListener {
                         listener.onItemClick(brandModel)
                     }
                     itemView.ID.text= brandModel.id.toString()
                     itemView.name.text=brandModel.name
                     itemView.madeby.text=brandModel.madeby
                     Glide
                         .with(itemView.context)
                         .load(R.drawable.noimage)
                         .into(itemView.imageView3)


                 }else{
                     itemView.setOnClickListener {
                         listener.onItemClick(brandModel)
                     }
                     itemView.ID.text= brandModel.id.toString()
                     itemView.name.text=brandModel.name
                     itemView.madeby.text=brandModel.madeby

                     Glide
                         .with(itemView.context)
                         .load(brandModel.imageUrl)
                         .into(itemView.imageView3)
                 }

             }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
             return  RowHolder(view)
    }

    override fun onBindViewHolder(holder: RowHolder, position: Int) {
           holder.bind(list[position],listener)
    }

    override fun getItemCount(): Int {
      return list.size
    }

}