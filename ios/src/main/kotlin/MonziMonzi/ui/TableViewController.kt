package MonziMonzi.ui

import apple.foundation.NSIndexPath
import apple.uikit.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import org.moe.natj.general.Pointer
import org.moe.natj.general.ann.Owned
import org.moe.natj.general.ann.RegisterOnStartup
import org.moe.natj.objc.ObjCRuntime
import org.moe.natj.objc.ann.ObjCClassName
import org.moe.natj.objc.ann.Selector
import tylenol.common.model.City
import tylenol.common.util.DustInfoManager
import tylenol.common.util.SSLConnect

@org.moe.natj.general.ann.Runtime(ObjCRuntime::class)
@ObjCClassName("TableViewController")
@RegisterOnStartup
class TableViewController protected constructor(peer: Pointer) : UITableViewController(peer) {

    @Selector("init")
    override external fun init(): TableViewController

    val dustInfoManager = DustInfoManager()
    var awaitData = ArrayList<City>()

    override fun viewDidLoad() {
        super.viewDidLoad()

        val data = async(CommonPool) {
            dustInfoManager.initCrawler()
            val ssl = SSLConnect()
            ssl.postHttps(dustInfoManager.pageUrl, 1000, 1000)
            dustInfoManager.dataCrawling()
        }
        launch(CommonPool) {
            awaitData = data.await()
            tableView().reloadData()
        }
    }

    override fun viewWillAppear(animated: Boolean) {
        super.viewWillAppear(true)
        tableView().reloadData()
    }


    override fun tableViewCellForRowAtIndexPath(tableView: UITableView?, indexPath: NSIndexPath?): UITableViewCell {
        var cell = tableView().dequeueReusableCellWithIdentifierForIndexPath("MonziTableCell", indexPath) as MonziTableCell
        val data = awaitData[indexPath!!.row().toInt()]
        val value = data.values[0]
        val image = UIImage.imageNamed("dust${dustInfoManager.statusCode(value) + 1}")
        cell.getStatusImageView().setImage(image)
        cell.getStatusTitleView().setText(data.name)
        cell.getStatusValueView().setText(value.toString())
        cell.setBackgroundColor(colorWithRGBHex(dustInfoManager.colorStatus(value).toInt()))
        return cell
    }

    fun colorWithRGBHex(hex: Int): UIColor {
        var r = (hex.shr(16)).and(0xFF) / 255.0
        var g = (hex.shr(8)).and(0xFF) / 255.0
        var b = hex.and(0xFF) / 255.0
        return UIColor.colorWithRedGreenBlueAlpha(r, g, b, 1.0)
    }

    override fun tableViewNumberOfRowsInSection(tableView: UITableView?, section: Long) = awaitData.size.toLong()

    companion object {

        @Owned
        @Selector("alloc")
        @JvmStatic external fun alloc(): TableViewController
    }
}
