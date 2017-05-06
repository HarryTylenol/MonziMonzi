package tylenol.monzimonzi

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.verticalLayout

/**
 * Created by baghyeongi on 2017. 5. 6..
 */
class MainUi : AnkoComponent<MainActivity> {
    lateinit var recyclerView : RecyclerView
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            appBarLayout {
                toolbar {
                    (this@with.ctx as MainActivity).setSupportActionBar(this)
                }
            }
            recyclerView = recyclerView {
                layoutManager = LinearLayoutManager(this@with.ctx)
            }
        }
    }
}