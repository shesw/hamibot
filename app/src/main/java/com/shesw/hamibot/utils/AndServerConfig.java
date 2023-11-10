package com.shesw.hamibot.utils;

import com.yanzhenjie.andserver.annotation.GetMapping;
//import com.yanzhenjie.andserver.annotation.PathVariable;
import com.yanzhenjie.andserver.annotation.QueryParam;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RestController;

@RestController
@RequestMapping("/home")
public class AndServerConfig {

//    @GetMapping(path = "/{userId}")
//    public String info3(@PathVariable(name = "userId") String userId, @QueryParam("3") String fields) {
//        return "3 success! " +  ", " + fields + ", userId: " + userId;
//    }

    @GetMapping("/1")
    public String info(@QueryParam("fields") String fields) {
        return "success! " +  ", " + fields;
    }

}
