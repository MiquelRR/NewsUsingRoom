package com.miquel.newsusingroom.repository
import kotlin.random.Random

class UrlProvider {
    private val images = listOf(

        "https://emtstatic.com/2016/01/aniversario-19391.jpg",
        "https://emtstatic.com/2016/01/aniversario-19811.jpg",
        "https://emtstatic.com/2016/01/aniversario-19361.jpg",
        "https://emtstatic.com/2016/01/aniversario-19321.jpg",
        "https://emtstatic.com/2016/01/aniversario-19141.jpg",
        "https://emtstatic.com/2016/01/aniversario-19121.jpg",
        "https://emtstatic.com/2016/01/aniversario-20011.jpg"
    )

    fun getRandomImage(): String {
        return images[Random.nextInt(images.size)]
    }
}