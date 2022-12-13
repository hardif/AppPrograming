package kr.ac.kumoh.S20180735

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.collection.LruCache
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject


class GameViewModel(application: Application) : AndroidViewModel(application) {
    data class Game(var id: Int,  var name: String,var info: String, var genre: String, var price: String, var image: String)
    companion object{
        const val QUEUE_TAG = "GameVolleyRequest"
        const val SERVER_URL = "https://mygame-wzhaj.run.goorm.io/"
    }

    private val games = ArrayList<Game>()
    private val _list = MutableLiveData<ArrayList<Game>>()
    val list: LiveData<ArrayList<Game>>
        get() = _list

    private var queue: RequestQueue
    val imageLoader: ImageLoader


    init {
        _list.value = games
        queue = Volley.newRequestQueue(getApplication())

        imageLoader = ImageLoader(queue,
            object : ImageLoader.ImageCache {
                private val cache = LruCache<String, Bitmap>(100)
                override fun getBitmap(url: String): Bitmap? {
                    return cache.get(url)
                }
                override fun putBitmap(url: String, bitmap: Bitmap) {
                    cache.put(url, bitmap)
                }
            })
    }



    fun requestGame(){
        val request = JsonArrayRequest(
            Request.Method.GET,
            SERVER_URL,
            null,
            {
                games.clear()
                parseJson(it)
                _list.value = games
            },
            {
                Toast.makeText(getApplication(),it.toString(),Toast.LENGTH_LONG).show()
            }
        )
        request.tag = QUEUE_TAG
        queue.add(request)
    }

    private fun parseJson(items: JSONArray) {
        for(i in 0 until items.length()) {
            val item: JSONObject = items[i] as JSONObject
            val id = item.getInt("id")
            val name = item.getString("name")
            val info = item.getString("info")
            val genre = item.getString("genre")
            val price = item.getString("price")
            val image = item.getString("image")

            games.add(Game(id, name, info,genre, price,image))
        }
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(QUEUE_TAG)
    }

}