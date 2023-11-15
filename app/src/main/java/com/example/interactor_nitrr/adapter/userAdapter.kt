package com.example.interactor_nitrr.adapter

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.interactor_nitrr.activity.MessageActivity
import com.example.interactor_nitrr.databinding.ItemUserLayoutBinding
import com.example.interactor_nitrr.model.UserModel

class userAdapter(val context: Context, val list : ArrayList<UserModel>) : RecyclerView.Adapter<userAdapter.InteractViewHolder>(){
    inner class InteractViewHolder(val binding: ItemUserLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InteractViewHolder {
        return InteractViewHolder(ItemUserLayoutBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    override fun onBindViewHolder(holder: InteractViewHolder, position: Int) {
        holder.binding.name.text= list[position].name
        holder.binding.branch.text= list[position].branch

        Glide.with(context).load(list[position].image).into(holder.binding.showImage)

        holder.binding.chat.setOnClickListener{
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId", list[position].number)
            context.startActivity(intent)
        }
    }

}