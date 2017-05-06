package MonziMonzi.ui

import apple.uikit.UIImageView
import apple.uikit.UILabel
import apple.uikit.UITableViewCell
import org.moe.natj.general.Pointer
import org.moe.natj.general.ann.RegisterOnStartup
import org.moe.natj.objc.ObjCRuntime
import org.moe.natj.objc.ann.IBOutlet
import org.moe.natj.objc.ann.ObjCClassName
import org.moe.natj.objc.ann.Property
import org.moe.natj.objc.ann.Selector

/**
 * Created by baghyeongi on 2017. 5. 6..
 */
@org.moe.natj.general.ann.Runtime(ObjCRuntime::class)
@ObjCClassName("MonziTableCell")
@RegisterOnStartup
class MonziTableCell protected constructor(peer: Pointer) : UITableViewCell(peer) {

    @Selector("imageView")
    @IBOutlet
    @Property
    external fun getStatusImageView(): UIImageView

    @Selector("titleView")
    @IBOutlet
    @Property
    external fun getStatusTitleView(): UILabel

    @Selector("valueView")
    @IBOutlet
    @Property
    external fun getStatusValueView(): UILabel
}