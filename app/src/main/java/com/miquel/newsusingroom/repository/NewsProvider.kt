import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class NewsProvider {

    data class News(val title: String, val link: String, val author: String, val date: String, val content: String, val imageUrl: String)

    private val rssUrl = "https://www.elmundotoday.com/feed/"

    suspend fun getRandomNews(): News? = withContext(Dispatchers.IO) {
        val newsList = mutableListOf<News>()
        try {
            val url = URL(rssUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()

            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            val inputStream = connection.inputStream
            parser.setInput(inputStream, "UTF-8")

            var eventType = parser.eventType
            var title = ""
            var link = ""
            var author = ""
            var date = ""
            var content = ""
            var imageUrl = ""
            var insideItem = false

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "item" -> insideItem = true
                            "title" -> if (insideItem) title = parser.nextText()
                            "link" -> if (insideItem) link = parser.nextText()
                            "dc:creator" -> if (insideItem) author = parser.nextText()
                            "pubDate" -> if (insideItem) date = parser.nextText()
                            "description" -> if (insideItem) content = parser.nextText().replace(Regex("<.*?>"), "")
                            "itunes:image" -> if (insideItem) imageUrl = parser.getAttributeValue(null, "href")
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item" && insideItem) {
                            newsList.add(News(title, link, author, date, content, imageUrl))
                            insideItem = false
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext if (newsList.isNotEmpty()) newsList[Random.nextInt(newsList.size)] else null
    }
}

