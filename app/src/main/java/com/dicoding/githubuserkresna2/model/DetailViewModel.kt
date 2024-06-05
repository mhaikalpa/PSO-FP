package com.dicoding.githubuserkresna2.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuserkresna2.data.UserResponse
import com.dicoding.githubuserkresna2.api.ApiConfig
import com.dicoding.githubuserkresna2.data.FavoriteEntity
import com.dicoding.githubuserkresna2.di.FavoriteInjection
import kotlinx.coroutines.launch

class DetailViewModel(username: String, app: Application) : ViewModel() {
    private val mFavoriteInjection: FavoriteInjection = FavoriteInjection(app)
    private val _detailUser = MutableLiveData<UserResponse?>()
    val detailUser: LiveData<UserResponse?> = _detailUser
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isNoInternet = MutableLiveData<Boolean>()
    private val _isDataFailed = MutableLiveData<Boolean>()
    val isDataFailed: LiveData<Boolean> = _isDataFailed


    init {
        viewModelScope.launch { getDetailUser(username) }
        Log.i(TAG, "DetailViewModel is Created")
    }
    fun insert(favEntity: FavoriteEntity) {
        mFavoriteInjection.insert(favEntity)
    }

    fun delete(favEntity: FavoriteEntity) {
        mFavoriteInjection.delete(favEntity)
    }

    fun getFavoriteById(id: Int): LiveData<List<FavoriteEntity>> {
        return mFavoriteInjection.getUserFavoriteById(id)
    }

    private suspend fun getDetailUser(username: String) {
        _isLoading.value = true
        val result = ApiConfig.getApiService().getDetailUserAsync(username)
        try {
            _isLoading.value = false
            _detailUser.postValue(result)
        } catch (e: Exception) {
            _isLoading.value = false
            _isNoInternet.value = false
            _isDataFailed.value = true
            Log.e(TAG, "onFailure: ${e.message.toString()}")
        }
    }


    companion object {
        private const val TAG = "DetailViewModel"
    }

    class DetailViewModelFactory(private val username: String, private val app: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                return DetailViewModel(username,app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
