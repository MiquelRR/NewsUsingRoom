package com.miquel.newsusingroom

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.miquel.newsusingroom.databinding.UpdateNewsItemBinding
import com.miquel.newsusingroom.repository.NewsItem
import com.miquel.newsusingroom.repository.NewsApplication
import kotlinx.coroutines.launch

class UpdateNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var newsItemToEdit: NewsItem? = null
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val newsItemToEditId = preferences.getInt("news_item_to_edit", -1)
        val intent = Intent(this, NewsListActivity::class.java)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = UpdateNewsItemBinding.inflate(layoutInflater)
        if (newsItemToEditId != -1) {
            lifecycleScope.launch {
                newsItemToEdit = NewsApplication.database.newsArticleDao().getNewsById(newsItemToEditId)
                binding.titleTextInputLayout.editText?.setText(newsItemToEdit?.title)
                binding.linkTextInputLayout.editText?.setText(newsItemToEdit?.link)
                binding.dateTextInputLayout.editText?.setText(newsItemToEdit?.date)
                binding.contentTextInputLayout.editText?.setText(newsItemToEdit?.content)
                binding.authorTextInputLayout.editText?.setText(newsItemToEdit?.author)
                binding.imageUrlTextInputLayout.editText?.setText(newsItemToEdit?.imageUrl)
            }
        }
        binding.cancelButton.setOnClickListener {
            startActivity(intent)
        }
        binding.submitButton.setOnClickListener {
            newsItemToEdit = NewsItem(
                title = binding.titleTextInputLayout.editText?.text.toString(),
                link = binding.linkTextInputLayout.editText?.text.toString(),
                date = binding.dateTextInputLayout.editText?.text.toString(),
                content = binding.contentTextInputLayout.editText?.text.toString(),
                author = binding.authorTextInputLayout.editText?.text.toString(),
                imageUrl = binding.imageUrlTextInputLayout.editText?.text.toString()
            )
            lifecycleScope.launch {
                NewsApplication.database.newsArticleDao().addNewsItem(newsItemToEdit!!)
                startActivity(intent)
            }
        }
        setContentView(binding.root)
    }
}