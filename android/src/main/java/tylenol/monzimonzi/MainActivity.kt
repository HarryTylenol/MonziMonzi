package tylenol.monzimonzi

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.*
import tylenol.common.model.City
import tylenol.common.util.DustInfoManager

class MainActivity : AppCompatActivity() {
    val dustInfoManager = DustInfoManager()
    val mainUi = MainUi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainUi.setContentView(this)

        val asyncData = async(CommonPool) {
            dustInfoManager.initCrawler()
            dustInfoManager.dataCrawling()
        }

        launch(Android) {
            mainUi.recyclerView.adapter = DustAdapter(asyncData.await())
        }

    }

    inner class DustAdapter(var data: ArrayList<City>) : RecyclerView.Adapter<DustViewHolder>() {
        override fun onBindViewHolder(holder: DustViewHolder?, position: Int) {
            holder!!.setData(data[position])
        }

        override fun getItemCount() = data.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int)
                = DustViewHolder(UI {
            frameLayout {
                lparams(matchParent)
                id = R.id.contentPanel
                padding = dip(24)
                verticalLayout {
                    gravity = Gravity.CENTER_VERTICAL
                    lparams { gravity = Gravity.START }
                    textView {
                        setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL))
                        textColor = Color.BLACK
                        textSize = 56f
                        id = R.id.text
                    }
                    textView {
                        textColor = Color.BLACK
                        alpha = 0.5f
                        textSize = 18f
                        id = R.id.title
                    }
                }

                imageView {
                    lparams(dip(108), dip(108)) { gravity = Gravity.END }
                    id = R.id.icon
                }
            }
        }.view)
    }

    inner class DustViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.find<TextView>(R.id.title)
        val text = view.find<TextView>(R.id.text)
        val icon = view.find<ImageView>(R.id.icon)
        val contentPanel = view.find<FrameLayout>(R.id.contentPanel)

        fun setData(city: City) {
            val value = city.values[0]
            title.text = city.name
            text.text = value.toString()
            icon.imageResource = R.drawable.ic_dusty_1 + dustInfoManager.statusCode(value)
            contentPanel.setBackgroundColor(dustInfoManager.colorStatus(value).toInt())
        }
    }

}
