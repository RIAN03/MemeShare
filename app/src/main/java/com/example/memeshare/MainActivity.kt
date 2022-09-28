package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    var currentImageUrl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()
    }
    private fun loadMeme(){
        val Progressbar=findViewById<ProgressBar>(R.id.progressBar)
        Progressbar.visibility=View.VISIBLE
        //Progressbar.setVisibility(View.VISIBLE);
              // Instantiate the RequestQueue.
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                currentImageUrl=response.getString("url")
                val image=findViewById<ImageView>(R.id.memeimageView)
                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //Progressbar.setVisibility(View.GONE)
                        Progressbar.visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        //Progressbar.setVisibility(View.GONE)
                        Progressbar.visibility=View.GONE
                        return false
                    }
                }).into(image)
               // Log.d ("success Request", response.substring (0,500))
            },
            Response.ErrorListener {
                Toast.makeText(this,"something went wrong", Toast.LENGTH_LONG).show()
               // Log.d("error",it.localizedMessage)
            })

        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, checkout this cool meme I got from Reddit $currentImageUrl")
        //val chooser=Intent(Intent.ACTION_CHOOSER)
        val chooser=Intent.createChooser(intent,"Share this meme using....")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}