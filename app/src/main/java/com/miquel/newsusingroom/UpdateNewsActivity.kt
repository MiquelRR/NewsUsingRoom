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
        val newsItemToEditId = intent.getIntExtra("id", -1)
        val retunIntent = Intent(this, NewsListActivity::class.java)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = UpdateNewsItemBinding.inflate(layoutInflater)
        if (newsItemToEditId != -1) {
            binding.idViewer.text = "Item $newsItemToEditId"
            lifecycleScope.launch {
                newsItemToEdit = NewsApplication.database.newsArticleDao().getNewsById(newsItemToEditId)
                binding.titleTextInputLayout.editText?.setText(newsItemToEdit?.title)
                binding.linkTextInputLayout.editText?.setText(newsItemToEdit?.link)
                binding.dateTextInputLayout.editText?.setText(newsItemToEdit?.date)
                binding.contentTextInputLayout.editText?.setText(newsItemToEdit?.content)
                binding.authorTextInputLayout.editText?.setText(newsItemToEdit?.author)
                binding.imageUrlTextInputLayout.editText?.setText(newsItemToEdit?.imageUrl)
            }
        } else {
            newsItemToEdit = NewsItem(title = "", link = "", date = "", content = "", author = "", imageUrl = "")
        }
        binding.cancelButton.setOnClickListener {
            startActivity(retunIntent)
            finish()
        }
        binding.submitButton.setOnClickListener {
            newsItemToEdit?.apply{
                title = binding.titleTextInputLayout.editText?.text.toString()
                link = binding.linkTextInputLayout.editText?.text.toString()
                date = binding.dateTextInputLayout.editText?.text.toString()
                content = binding.contentTextInputLayout.editText?.text.toString()
                author = binding.authorTextInputLayout.editText?.text.toString()
                imageUrl = binding.imageUrlTextInputLayout.editText?.text.toString()
            }

            lifecycleScope.launch {
                if (newsItemToEditId != -1) {
                    NewsApplication.database.newsArticleDao().updateNewsItem(newsItemToEdit!!)
                } else {
                    NewsApplication.database.newsArticleDao().addNewsItem(newsItemToEdit!!)
                }
                startActivity(retunIntent)
                finish()
            }
        }
        setContentView(binding.root)
    }
}