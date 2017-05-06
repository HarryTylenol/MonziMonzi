package tylenol.common.util

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import tylenol.common.model.City
import java.util.*

/**
 * Created by baghyeongi on 2017. 5. 6..
 */
class DustInfoManager {

    val pageUrl = "https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=미세먼지"
    var document : Document? = null

    fun initCrawler() {
        document = Jsoup.connect(pageUrl).get()
    }

    fun dataCrawling(): ArrayList<City> {
        var cities = ArrayList<City>()
        val elements = document!!.select("div.dust_data > div.tb_scroll > table > tbody > tr")
        elements.asIterable().forEach {
            val cityName = it.select("th").text()
            val values = it.select("td").asIterable().map { it.text().toInt() }
            cities.add(City(cityName, values))
        }
        return cities
    }

    fun statusCode(value : Int) : Int {
        when (value)
        {
            in 0..30 -> return 0
            in 31..80 -> return 1
            in 81..130 -> return 2
            in 131..150 -> return 3
            in 151..1000 -> return 4
            else -> return 5
        }
    }

    fun colorStatus(value : Int) : Long {
        when (statusCode(value))
        {
            0 -> return 0xff6cbaff
            1 -> return 0xff7ec677
            2 -> return 0xffffd95f
            3 -> return 0xffffd95f
            4 -> return 0xffffa96b
            else -> return 0xffff7c7c
        }
    }

}