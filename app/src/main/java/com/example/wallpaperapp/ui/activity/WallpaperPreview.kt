package com.example.wallpaperapp.ui.activity

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.navArgs
import coil.load
import com.example.wallpaperapp.R
import com.example.wallpaperapp.databinding.ActivityWallpaperPreviewBinding
import java.io.IOException


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WallpaperPreview : AppCompatActivity() {

    private lateinit var binding: ActivityWallpaperPreviewBinding
    private val args: WallpaperPreviewArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallpaperPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemBars()

        supportActionBar?.hide()

        binding.apply {
            wallpaperPreviewHolder.load(args.photoUrl){
                placeholder(R.drawable.placeholder)
            }

            setWallpaperBtn.setOnClickListener {
                binding.setWallpaperBtn.isEnabled = false
                Handler(Looper.getMainLooper()).run {
                    postDelayed({
                        setWallpaper()
                    }, 1000)
                }
            }
        }

    }

    private fun setWallpaper() {

        val wallpaperManager = WallpaperManager.getInstance(applicationContext)
        val drawable = wallpaperManager.builtInDrawable
        wallpaperManager.setWallpaperOffsetSteps(1F, 1F)
        val height = drawable.minimumHeight
        val width = drawable.minimumWidth
        val bitmap = (binding.wallpaperPreviewHolder.drawable as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height,true)

        try {
            wallpaperManager.setBitmap(scaledBitmap)
            Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show()
            binding.setWallpaperBtn.isEnabled = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}