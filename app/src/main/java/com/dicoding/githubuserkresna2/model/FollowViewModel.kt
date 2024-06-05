package com.dicoding.githubuserkresna2.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuserkresna2.data.UserResponse
import com.dicoding.githubuserkresna2.api.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FollowViewModel(private val username: String) : ViewModel() {
    private val _followers = MutableLiveData<ArrayList<UserResponse>?>()
    val followers: LiveData<ArrayList<UserResponse>?> = _followers
    private val _following = MutableLiveData<ArrayList<UserResponse>?>()
    val following: LiveData<ArrayList<UserResponse>?> = _following
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isDataFailed = MutableLiveData<Boolean>()
    val isDataFailed: LiveData<Boolean> = _isDataFailed

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        viewModelScope.launch {
            fetchFollowers()
            fetchFollowing()
        }
        Log.i(TAG, "FollowFragment is Created")
    }

    private suspend fun fetchFollowers() {
        coroutineScope.launch {
            _isLoading.value = true
            val result = ApiConfig.getApiService().getListFollowers(username)
            try{
                _isLoading.value = false
                _followers.postValue(result)
            }catch (e: Exception){
                _isLoading.value = false
                _isDataFailed.value = true
                Log.e(TAG, "OnFailure: ${e.message.toString()}")
            }
        }
    }

    private suspend fun fetchFollowing() {
        coroutineScope.launch {
            _isLoading.value = true
            val result = ApiConfig.getApiService().getListFollowing(username)
            try{
                _isLoading.value = false
                _following.postValue(result)
            }catch (e: Exception){
                _isLoading.value = false
                _isDataFailed.value = true
                Log.e(TAG, "OnFailure: ${e.message.toString()}")
            }
        }
    }
    companion object {
        private const val TAG = "FollowersAndFollowingViewModel"
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    class FollowViewModelFactory(private val username: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
                return FollowViewModel(username) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
