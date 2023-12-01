package com.shesw.hamibot.utils;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.QueryParam;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
@RequestMapping("/script")
public class AndServerConfig {

    @GetMapping("/click")
    public String click(@QueryParam("x") String x, @QueryParam("y") String y) {
        String asis = "(" + x + ", " + y + ")";
        JSEngineHelper.runScriptStr("click" + asis);
        return "success! click: " + asis;
    }

    @GetMapping("/swipe")
    public String swipe(@QueryParam("direction") int direction) {    // 0:down, 1:up, 2:left, 3:right
        String params;
        if (0 == direction) {
            params = "(350, 800, 200, 350, 300)";
        } else if (1 == direction) {
            params = "(350, 350, 350, 800, 300)";
        } else if (2 == direction) {
            params = "(500, 400, 200, 400, 300)";
        } else {
            params = "(200, 400, 500, 400, 300)";
        }
        JSEngineHelper.runScriptStr("swipe" + params);
        return "success! click: " + direction + ", " + params;
    }
}
