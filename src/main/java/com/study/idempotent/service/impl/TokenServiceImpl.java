package com.study.idempotent.service.impl;

import com.study.idempotent.common.Constant;
import com.study.idempotent.common.ResponseCode;
import com.study.idempotent.exception.ServiceException;
import com.study.idempotent.service.TokenService;
import com.study.idempotent.utils.JedisUtil;
import com.study.idempotent.utils.RandomUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Package: com.study.idempotent.service.impl
 * Description：
 * Author: zhangkaixuan
 * Date:  2020/9/4 18:27
 * Modified By:
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private JedisUtil jedisUtil;


    /**
     * 创建token
     *
     * @return
     */
    @Override
    public String createToken() {
        String str = RandomUtil.UUID32();
        StringBuilder token = new StringBuilder();
        try {
            token.append(Constant.TOKEN_PREFIX).append(str);
            jedisUtil.setEx(token.toString(), token.toString(),10000);
            boolean notEmpty = StringUtils.isNotEmpty(token.toString());
            if (notEmpty) {
                return token.toString();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * 检验token
     *
     * @param request
     * @return
     */
    @Override
    public boolean checkToken(HttpServletRequest request) throws Exception {

        String token = request.getHeader(Constant.TOKEN_NAME);
        // header中不存在token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(Constant.TOKEN_NAME);
            // parameter中也不存在token
            if (StringUtils.isBlank(token)) {
                throw new ServiceException(ResponseCode.ILLEGAL_ARGUMENT.getMsg());
            }
        }

        if (!jedisUtil.exists(token)) {
            throw new ServiceException(ResponseCode.REPETITIVE_OPERATION.getMsg());
        }

//        Long del = jedisUtil.del(token);
//        if (del<= 0) {
//            throw new ServiceException(ResponseCode.REPETITIVE_OPERATION.getMsg());
//        }
        return true;
    }
}
