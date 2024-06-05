package com.dicoding.githubuserkresna2.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.githubuserkresna2.data.UserResponse
import com.dicoding.githubuserkresna2.di.UserInjection
import com.dicoding.githubuserkresna2.ui.SettingsPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingsPreferences) : ViewModel() {

    val user: LiveData<ArrayList<UserResponse>?> = UserInjection.user
    val searchUser: LiveData<ArrayList<UserResponse>?> = UserInjection.userSearch
    val isLoading: LiveData<Boolean> = UserInjection.isLoading
    val isDataFailed: LiveData<Boolean> = UserInjection.isDataFailed

    init {
        viewModelScope.launch{
            UserInjection.getListUser()
        }

    }
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
    override fun onCleared() {
        super.onCleared()
        UserInjection.viewModelJob.cancel()
    }
    class MainViewModelFactory(private val pref: SettingsPreferences) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}
