package com.shesw.hamibot.douyin_kol

import com.shesw.hamibot.CslBaseActivity

class DouyinKolProductionActivity : CslBaseActivity() {
    override fun pageTitle(): String {
        return "查询用户作品"
    }

    override fun scriptsName(): String {
        return "douyin_production.js"
    }
}