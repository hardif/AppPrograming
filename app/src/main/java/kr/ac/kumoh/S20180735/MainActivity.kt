package kr.ac.kumoh.S20180735

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kr.ac.kumoh.S20180735.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var model: GameViewModel
    private val gameAdapter = GameAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        model = ViewModelProvider(this)[GameViewModel::class.java]

        binding.list.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = gameAdapter
        }

        model.list.observe((this)) {
            gameAdapter.notifyItemRangeInserted(0,model.list.value?.size ?: 0)
        }
        model.requestGame()
    }
    inner class GameAdapter : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener {
            val txTitle = itemView.findViewById<TextView>(R.id.text1)
            val txGenre = itemView.findViewById<TextView>(R.id.text2)
            val niImage: NetworkImageView = itemView.findViewById<NetworkImageView>(R.id.image)

            init {
                niImage.setDefaultImageResId(android.R.drawable.ic_menu_report_image)
                itemView.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                Toast.makeText(application,"눌렀습니다",Toast.LENGTH_LONG).show()
                val intent = Intent(application, GameActivity::class.java)
                intent.putExtra(GameActivity.KEY_GAME,
                    model.list.value?.get(adapterPosition)?.name)
                intent.putExtra(GameActivity.KEY_INFO,
                    model.list.value?.get(adapterPosition)?.info)
                intent.putExtra(GameActivity.KEY_GENRE,
                    model.list.value?.get(adapterPosition)?.genre)
                intent.putExtra(GameActivity.KEY_PRICE,
                    model.list.value?.get(adapterPosition)?.price)
                intent.putExtra(GameActivity.KEY_IMAGE,
                    model.list.value?.get(adapterPosition)?.image)
                startActivity(intent)
            }


        }

        override fun onCreateViewHolder(parent: ViewGroup,viewType: Int) : ViewHolder {
            val view = layoutInflater.inflate(R.layout.item_game, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = model.list.value?.size ?: 0

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.txTitle.text = model.list.value?.get(position)?.name
            holder.txGenre.text = model.list.value?.get(position)?.genre
            holder.niImage.setImageUrl(model.list.value?.get(position)?.image, model.imageLoader)


        }
    }
}