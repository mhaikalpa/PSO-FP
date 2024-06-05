package com.dicoding.githubuserkresna2.di

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuserkresna2.data.FavoriteDao
import com.dicoding.githubuserkresna2.data.FavoriteEntity
import com.dicoding.githubuserkresna2.data.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteInjection(application: Application) {
    private val favDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        favDao = db.favDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = favDao.getAllFavorite()
    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteEntity>> =
        favDao.getUserFavoriteById(id)

    fun insert(fav: FavoriteEntity) {
        executorService.execute { favDao.insert(fav) }
    }

    fun delete(fav: FavoriteEntity) {
        executorService.execute { favDao.delete(fav) }
    }
}
