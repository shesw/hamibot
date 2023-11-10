package com.shesw.hamibot.views

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hamibot.hamibot.R
import java.util.ArrayList

class AccountAdapter(
    data: ArrayList<AccountModel>,
    private val runScript: (model: AccountModel) -> Unit
) : BaseQuickAdapter<AccountModel, BaseViewHolder>(R.layout.account_btn_item_layout, data) {
    override fun convert(helper: BaseViewHolder?, item: AccountModel?) {
        helper ?: return
        item ?: return
        (helper.itemView as? TextView)?.run {
            text = if (item.account.contains("@")) item.account.substring(0, item.account.indexOf("@")) else item.account
            setOnClickListener { runScript(item) }
        }
    }
}