package com.miquel.newsusingroom

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.miquel.newsusingroom.databinding.UpdateNewsItemBinding

class UpdateNewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val newsItemToEdit = preferences.getInt("news_item_to_edit", -1)
        val intent = Intent(this, NewsListActivity::class.java)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = UpdateNewsItemBinding.inflate(layoutInflater)
        if (newsItemToEdit != -1) {

        }
        setContentView(binding.root)
    }
}