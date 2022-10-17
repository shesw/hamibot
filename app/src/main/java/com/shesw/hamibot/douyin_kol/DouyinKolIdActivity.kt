package com.shesw.hamibot.douyin_kol

import com.shesw.hamibot.CslBaseActivity

class DouyinKolIdActivity : CslBaseActivity() {
    override fun pageTitle(): String {
        return "获取用户id"
    }

    override fun scriptsName(): String {
        return "douyin_ids.js"
    }
}