package com.krashkrosh748199.regularnews

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity(), NewsItemClicked {
     private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvFirst=findViewById<RecyclerView>(R.id.recyclerView)
        rvFirst.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter= NewsListAdapter(this)
        rvFirst.adapter = mAdapter
    }

    fun fetchData() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=b66b589a92d6412385f6aa1cedb751bf"

        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
                Log.e("sdsadas","$it")
                val newsJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until  newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    Toast.makeText(this,"Entered",Toast.LENGTH_LONG).show()
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener { error ->

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        queue.add(getRequest)
        MySingleton.getInstance(this).addToRequestQueue(getRequest)
    }

    override fun onItemClicked(item: News) {
         val builder=CustomTabsIntent.Builder()
        val customTabsIntent=builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}