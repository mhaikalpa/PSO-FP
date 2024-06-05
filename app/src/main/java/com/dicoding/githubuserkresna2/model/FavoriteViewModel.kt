package com.dicoding.githubuserkresna2.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserkresna2.data.FavoriteEntity
import com.dicoding.githubuserkresna2.di.FavoriteInjection

class FavoriteViewModel(application : Application) : ViewModel() {
    private val mFavRepository : FavoriteInjection = FavoriteInjection(application)
    fun getAllFavorites() : LiveData<List<FavoriteEntity>> = mFavRepository.getAllFavorites()
    class FavViewModelFactory private constructor(private val application: Application) :
        ViewModelProvider.NewInstanceFactory() {
        companion object {
            @Volatile
            private var INSTANCE: FavViewModelFactory? = null

            @JvmStatic
            fun getInstance(application: Application): FavViewModelFactory {
                if (INSTANCE == null) {
                    synchronized(FavViewModelFactory::class.java) {
                        INSTANCE = FavViewModelFactory(application)
                    }
                }
                return INSTANCE as FavViewModelFactory
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class : ${modelClass.name} ")
        }

    }
}