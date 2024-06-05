package com.dicoding.githubuserkresna2.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserkresna2.adapter.UserFollowAdapter
import com.dicoding.githubuserkresna2.databinding.FragmentFollowsBinding
import com.dicoding.githubuserkresna2.data.UserResponse
import com.dicoding.githubuserkresna2.model.FollowViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private val adapter: UserFollowAdapter by lazy {
        UserFollowAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val user = arguments?.getParcelable<UserResponse>(ARG_PARCEL)

        if (index == 1) {
            user?.login?.let {
                setViewModel(it, 1)
            }
        } else {
            user?.login?.let {
                setViewModel(it, 2)
            }
        }
    }

    private fun setViewModel(username: String, index: Int) {
        val followViewModel: FollowViewModel by viewModels {
            FollowViewModel.FollowViewModelFactory(username)
        }
        followViewModel.isLoading.observe(viewLifecycleOwner, { showProgressBar(it) })
        followViewModel.isDataFailed.observe(viewLifecycleOwner, { showFailedLoadData(it) })

        val followLiveData = if (index == 1) followViewModel.followers else followViewModel.following
        followLiveData.observe(viewLifecycleOwner) { userList ->
            if (userList != null) {
                if (userList.isNotEmpty()) {
                    adapter.addDataToList(userList)
                    binding.rvFollows.layoutManager = LinearLayoutManager(context)
                    binding.rvFollows.adapter = adapter
                    adapter.setOnItemClickCallback { user ->
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.KEY_USER, user)
                        startActivity(intent)
                    }
                } else {
                    showFailedLoadData(true)
                }
            } else {
                showFailedLoadData(true)
            }
        }
    }


    private fun showProgressBar(state: Boolean) {
        binding.animLoader.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun showFailedLoadData(isFailed: Boolean) {
        binding.animFailedDataLoad.visibility = if (isFailed) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_PARCEL = "user_model"

        fun newInstance(index: Int, userResponse: UserResponse?) = FollowFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, index)
                putParcelable(ARG_PARCEL, userResponse)
            }
        }
    }
}
