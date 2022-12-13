package kr.ac.kumoh.S20180735

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.collection.LruCache
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import kr.ac.kumoh.S20180735.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    companion object {
        const val KEY_GAME = "GameTitle"
        const val KEY_GENRE = "GameGenre"
        const val KEY_INFO = "GameInfo"
        const val KEY_PRICE = "GamePrice"
        const val KEY_IMAGE = "GameImage"
    }
    private lateinit var binding: ActivityGameBinding
    private lateinit var imageLoader: ImageLoader
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageLoader = ImageLoader(
            Volley.newRequestQueue(this),
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })

        // 클릭시 나오는 창의 정보
        binding.imageGame.setImageUrl(intent.getStringExtra(KEY_IMAGE), imageLoader)
        binding.textName.text = intent.getStringExtra(KEY_GAME)
        binding.textInfo.text = intent.getStringExtra(KEY_INFO)
    }
}