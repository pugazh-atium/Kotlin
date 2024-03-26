package com.example.kotlinapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinapplication.databinding.ActivityMainBinding
import com.example.kotlinapplication.ui.theme.AppConstant
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var articles: ArrayList<Articles>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        articles = ArrayList()
        fetchArticles()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun fetchArticles() {
        GlobalScope.launch(Dispatchers.IO) {
            val connection =
                URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json").openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("X_Api_key", AppConstant.API_KEY)
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.doInput = true

            try {
                val reader = InputStreamReader(connection.inputStream)
                reader.use { input ->
                    val response = StringBuilder()
                    val bufferedReader = BufferedReader(input)
                    bufferedReader.forEachLine {
                        response.append(it.trim())
                    }
                    val jsonObject = JSONObject(response.toString())
                    val jsonArray: JSONArray = jsonObject.getJSONArray("articles")
                    for (i in 0 until jsonArray.length()) {
                        val articleJson: JSONObject = jsonArray.getJSONObject(i)
                        val author = articleJson.getString("author")
                        val headline = articleJson.getString("title")
                        val description = articleJson.getString("description")
                        val url = articleJson.getString("url")
                        if (author != "" && author != "null" && !author.contains("@") && !author.contains("/")) {
                            val article = Articles(author, headline, description, url)
                            articles.add(article)
                        } else {
                            val article = Articles("", headline, description, url)
                            articles.add(article)
                        }
                    }
                }
                // Update UI on the main thread
                launch(Dispatchers.Main) {
                    binding.listview.adapter = MuAdapter(this@MainActivity, articles)
                    binding.listview.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        val article = articles[position]
                        openArticle(article.url)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun openArticle(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
