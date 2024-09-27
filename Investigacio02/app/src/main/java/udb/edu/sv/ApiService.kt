package udb.edu.sv

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    /* Obtiene el listado de posts */
    @GET("posts")
    suspend fun getPosts(): List<Post>

    /* Manda los datos de 1 post */
    @POST("posts")
    suspend fun createPost(@Body post: Post): Post
}