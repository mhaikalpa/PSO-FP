package com.dicoding.githubuserkresna2.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.githubuserkresna2.R
import com.dicoding.githubuserkresna2.adapter.SectionPagerAdapter
import com.dicoding.githubuserkresna2.databinding.ActivityDetailBinding
import com.dicoding.githubuserkresna2.data.UserResponse
import com.dicoding.githubuserkresna2.api.Connection
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuserkresna2.data.FavoriteEntity
import com.dicoding.githubuserkresna2.model.DetailViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var isFavorite = false
    private var username: String? = null // Pindahkan deklarasi ke tingkat kelas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.detailDataLayout.visibility = View.GONE
        // Inisialisasi variabel username di sini
        username = intent.getStringExtra(KEY_USERNAME)
        username?.let {
            checkInternetConnection(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                username?.let { // Gunakan username yang sudah diinisialisasi
                    val sendIntent: Intent = Intent( 17616 ).apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, "https://github.com/$it")
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    startActivity(shareIntent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun checkInternetConnection(username : String) {
        val user = intent.getParcelableExtra<UserResponse>(KEY_USER)
        val networkConnection = Connection(applicationContext)
        networkConnection.observe(this, { isConnected ->
            if (isConnected) {
                showNoInternetAnimation(false)
                val favorite = FavoriteEntity()
                favorite.login = username
                favorite.id = intent.getIntExtra(KEY_ID, 0)
                favorite.avatar_url = user?.avatarUrl
                val detailViewModel: DetailViewModel by viewModels {
                    DetailViewModel.DetailViewModelFactory(username, application)
                }
                detailViewModel.isLoading.observe(this@DetailActivity, {
                    showProgressBar(it)
                })
                detailViewModel.isDataFailed.observe(this@DetailActivity, {
                    showFailedLoadData(it)
                })
                detailViewModel.detailUser.observe(this@DetailActivity, { userResponse ->
                    if (userResponse != null) {
                        setData(userResponse)
                        setTabLayoutAdapter(userResponse)
                    }
                })
                detailViewModel.getFavoriteById(favorite.id!!)
                    .observe(this@DetailActivity, { listFav ->
                        isFavorite = listFav.isNotEmpty()

                        binding.detailFabFavorite.imageTintList = if (listFav.isEmpty()) {
                            ColorStateList.valueOf(Color.rgb(255, 255, 255))
                        } else {
                            ColorStateList.valueOf(Color.rgb(247, 106, 123))
                        }

                    })

                binding.detailFabFavorite.apply {
                    setOnClickListener {
                        if (isFavorite) {
                            detailViewModel.delete(favorite)
                            Toast.makeText(
                                this@DetailActivity,
                                "${favorite.login} telah dihapus dari data User Favorite ",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            detailViewModel.insert(favorite)
                            Toast.makeText(
                                this@DetailActivity,
                                "${favorite.login} telah ditambahkan ke data User Favorite",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            } else {
                binding.detailDataLayout.visibility = View.GONE
                binding.detailAnimationLayout.visibility = View.VISIBLE
                showNoInternetAnimation(true)
            }
        })
    }

    private fun setTabLayoutAdapter(user: UserResponse) {
        val sectionPagerAdapter = SectionPagerAdapter(this@DetailActivity)
        sectionPagerAdapter.userData = user
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setData(userResponse: UserResponse?) {
        if (userResponse != null) {
            with(binding) {
                detailDataLayout.visibility = View.VISIBLE
                detailImage.visibility = View.VISIBLE
                Glide.with(root)
                    .load(userResponse.avatarUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    )
                    .circleCrop()
                    .into(binding.detailImage)
                detailName.visibility = View.VISIBLE
                detailUsername.visibility = View.VISIBLE
                detailName.text = userResponse.name
                detailUsername.text = userResponse.login
                if (userResponse.bio != null) {
                    detailBio.visibility = View.VISIBLE
                    detailBio.text = userResponse.bio
                } else {
                    detailBio.visibility = View.GONE
                }

                if (userResponse.followers != null) {
                    detailFollowersValue.visibility = View.VISIBLE
                    detailFollowersValue.text = userResponse.followers
                } else {
                    detailFollowersValue.visibility = View.GONE
                }
                if (userResponse.followers != null) {
                    detailFollowers.visibility = View.VISIBLE
                } else {
                    detailFollowers.visibility = View.GONE
                }
                if (userResponse.following != null) {
                    detailFollowingValue.visibility = View.VISIBLE
                    detailFollowingValue.text = userResponse.following
                } else {
                    detailFollowingValue.visibility = View.GONE
                }
                if (userResponse.following != null) {
                    detailFollowing.visibility = View.VISIBLE
                } else {
                    detailFollowing.visibility = View.GONE
                }

            }
        } else {
            Log.i(TAG, "setData fun is error")
        }
    }

    private fun showProgressBar(isLoading: Boolean) {
        binding.detailLoader.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showNoInternetAnimation(isNoInternet: Boolean) {
        binding.detailNoInternet.visibility = if (isNoInternet) View.VISIBLE else View.GONE
    }

    private fun showFailedLoadData(isFailed: Boolean) {
        binding.detailFailedDataLoad.visibility = if (isFailed) View.VISIBLE else View.GONE
        binding.tvFailed.visibility = if (isFailed) View.VISIBLE else View.GONE
    }

    companion object {
        const val KEY_USER = "user"
        private const val TAG = "DetailActivity"
        const val KEY_USERNAME = "username"
        const val KEY_ID = "extra id"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}
