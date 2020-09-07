package com.study.idempotent.controller;

import com.alibaba.fastjson.JSON;
import com.study.idempotent.annotation.ApiIdempotent;
import com.study.idempotent.common.Constant;
import com.study.idempotent.common.ServerResponse;
import com.study.idempotent.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Package: com.study.idempotent.controller
 * Description：
 * Author: zhangkaixuan
 * Date:  2020/9/7 10:13
 * Modified By:
 */

@RestController
public class IdempotentController {
    @Resource
    private TokenService tokenService;

    @PostMapping("/get/token")
    public String getToken() {
        String token = tokenService.createToken();
        if (StringUtils.isNotEmpty(token)) {
            ServerResponse resultVo = new ServerResponse();
            resultVo.setStatus(Constant.code_success);
            resultVo.setMsg(Constant.SUCCESS);
            resultVo.setData(token);
            return resultVo.toString();
        }
        return StringUtils.EMPTY;
    }


    @ApiIdempotent
    @PostMapping("/test/Idempotence")
    public String testIdempotence(HttpServletRequest request) throws Exception {
        boolean bool = tokenService.checkToken(request);
        if (bool) {
            ServerResponse resultVo = new ServerResponse();
            resultVo.setStatus(Constant.code_success);
            resultVo.setMsg(Constant.SUCCESS);
            resultVo.setData(Constant.SUCCESS);
            return resultVo.toString();
        }
        return StringUtils.EMPTY;
    }

}
