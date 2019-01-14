package com.lsz.depot.local.api;


import com.lsz.core.common.ResponseInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 初始化
 */
@RestController
@RequestMapping(value = "/local/test")
public class TestApi {



    @RequestMapping(value = "/1")
    public ResponseInfo<String> findAllUrl() {
        return ResponseInfo.assertion("返回1");
    }

}
