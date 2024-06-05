package com.dicoding.githubuserkresna2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserkresna2.R
import com.dicoding.githubuserkresna2.databinding.ItemUserBinding
import com.dicoding.githubuserkresna2.data.UserResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserAdapter :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private val listUserResponse = ArrayList<UserResponse>()
    private var onItemClick: ((UserResponse) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listUserResponse[position])
        holder.itemView.setOnClickListener { onItemClick?.invoke(listUserResponse[position]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(items: ArrayList<UserResponse>) {
        listUserResponse.clear()
        listUserResponse.addAll(items)
        notifyDataSetChanged()
    }

    override fun getItemCount() = listUserResponse.size

    inner class MyViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserResponse) {
            binding.name.text = user.login
            Glide.with(binding.root)
                .load(user.avatarUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading)
                        .error(R.drawable.ic_error)
                )
                .circleCrop()
                .into(binding.circleImageView)
        }
    }

    fun setOnItemClickListener(listener: (UserResponse) -> Unit) {
        onItemClick = listener
    }
}
