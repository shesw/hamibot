package com.shesw.hamibot.douyin_kol

import com.hamibot.hamibot.R
import com.shesw.hamibot.CslBaseActivity

class DouyinKolProductionActivity : CslBaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_douyin_kol_production
    }

    override fun scriptsName(): String {
        return "douyin_production.js"
    }
}