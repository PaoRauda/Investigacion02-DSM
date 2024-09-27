package udb.edu.sv

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

import retrofit2.HttpException
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private lateinit var refreshButton: Button
    private lateinit var addButton: Button
    private var posts: MutableList<Post> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        refreshButton = findViewById(R.id.btnRefresh)
        addButton = findViewById(R.id.btnAddPost)


        postAdapter = PostAdapter(posts)
        recyclerView.adapter = postAdapter


        getPostsFromApi() //Se llama al método para leer los POSTS

        //Listeners
        addButton.setOnClickListener {
            //Se llama al método para añadir POSTS
            addNewPost()
        }

        refreshButton.setOnClickListener {
            getPostsFromApi() //Se vuelve a realizar la peticion a la API
        }
    }

    //Método para agregar un nuevo post
    private fun getPostsFromApi() {
        lifecycleScope.launch {
            try {
                val postsFromApi = RetrofitClient.instance.getPosts()
                posts.clear()
                posts.addAll(postsFromApi)
                postAdapter.notifyDataSetChanged()
            } catch (e: IOException) {
                Log.e("MainActivity", "Network error: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de red, por favor verifica tu conexión", Toast.LENGTH_LONG).show()
            } catch (e: HttpException) {
                Log.e("MainActivity", "HTTP error: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de servidor, por favor intenta más tarde", Toast.LENGTH_LONG).show()
            }
        }
    }

    //Método para agregar un nuevo post
    private fun addNewPost() {
        val newPost = Post(
            userId = 1,
            id = null,
            title = "Nuevo Post",
            body = "Este es el cuerpo del nuevo post"
        )

        lifecycleScope.launch {
            try {
                val createdPost = RetrofitClient.instance.createPost(newPost)
                postAdapter.addPost(createdPost)
            } catch (e: IOException) {
                Log.e("MainActivity", "Network error: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de red, por favor verifica tu conexión", Toast.LENGTH_LONG).show()
            } catch (e: HttpException) {
                Log.e("MainActivity", "HTTP error: ${e.message}")
                Toast.makeText(this@MainActivity, "Error de servidor, por favor intenta más tarde", Toast.LENGTH_LONG).show()
            }
        }
    }
}
