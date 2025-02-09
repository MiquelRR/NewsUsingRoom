package com.miquel.newsusingroom

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import androidx.compose.material3.Card
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import com.miquel.newsusingroom.repository.NewsItem


class MyAdapter(private var cardDataList: List<NewsItem>,
                private var likedNewsIds: Set<Int>, // Conjunto de IDs de noticias que el usuario ha dado like
                private val onLikeClicked: (NewsItem, Boolean) -> Unit // Callback para manejar likes
) : RecyclerView.Adapter<MyAdapter.CardViewHolder>(){
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.card_a_image)
        val titleTextView: TextView = itemView.findViewById(R.id.card_title)
        val date: TextView = itemView.findViewById(R.id.date_plus_author)
        val descriptionTextView: TextView = itemView.findViewById(R.id.card_paragraph)
        val card: MaterialCardView = itemView.findViewById(R.id.card)
        val cbFavorite: MaterialCheckBox = itemView.findViewById(R.id.cbFavorite)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.card_layout, parent, false)
        return CardViewHolder(itemView)
    }

    override fun getItemCount(): Int = cardDataList.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardData: NewsItem = cardDataList[position]
        Glide.with(holder.itemView.context)
            .load(cardData.imageUrl)
            .override(300, 300)
            .into(holder.imageView)
        holder.titleTextView.text = cardData.title
        holder.date.text = "${cardData.date} - ${cardData.author}"
        holder.descriptionTextView.text = cardData.content
        holder.cbFavorite.isChecked = likedNewsIds.contains(cardData.id)
        holder.cbFavorite.setOnCheckedChangeListener { _, isChecked ->
            onLikeClicked(cardData, isChecked)
        }
        /*
        holder.card.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cardData.link))
            holder.itemView.context.startActivity(intent)
        }
        */


    }
}

