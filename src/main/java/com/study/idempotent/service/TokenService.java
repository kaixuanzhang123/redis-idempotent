package com.study.idempotent.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Package: com.study.idempotent.service
 * Description：
 * Author: zhangkaixuan
 * Date:  2020/9/4 18:15
 * Modified By:
 */
public interface  TokenService {
    /**
     * 创建token
     * @return
     */
    public  String createToken();

    /**
     * 检验token
     * @param request
     * @return
     */
    public boolean checkToken(HttpServletRequest request) throws Exception;
}
