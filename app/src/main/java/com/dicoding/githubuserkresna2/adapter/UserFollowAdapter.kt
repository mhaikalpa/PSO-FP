package com.dicoding.githubuserkresna2.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.githubuserkresna2.R
import com.dicoding.githubuserkresna2.databinding.ItemUserFollowsBinding
import com.dicoding.githubuserkresna2.data.UserResponse
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class UserFollowAdapter :
    RecyclerView.Adapter<UserFollowAdapter.MyViewHolder>() {

    private var userList = ArrayList<UserResponse>()
    private lateinit var onItemClick: (UserResponse) -> Unit

    @SuppressLint("NotifyDataSetChanged")
    fun addDataToList(users: ArrayList<UserResponse>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = ItemUserFollowsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.itemView.setOnClickListener { onItemClick.invoke(userList[position]) }
    }

    override fun getItemCount() = userList.size

    fun setOnItemClickCallback(listener: (UserResponse) -> Unit) {
        onItemClick = listener
    }

    inner class MyViewHolder(private var binding:  ItemUserFollowsBinding) :
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
}
