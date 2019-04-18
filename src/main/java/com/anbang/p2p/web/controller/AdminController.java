package com.anbang.p2p.web.controller;

import com.anbang.p2p.conf.AdminConfig;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.web.vo.CommonVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    /**
     * 管理员登录
     */
    @RequestMapping("/adminLogin")
    public CommonVO adminLogin(String account, String pass) {
        if (StringUtils.isAnyBlank(account, pass)) {
            return CommonVOUtil.invalidParam();
        }

        if (AdminConfig.ACCOUNT.equals(account) && AdminConfig.PASS.equals(pass)) {
            String token = encode(account + pass);
            return CommonVOUtil.success(token,"success");
        }

        return CommonVOUtil.error("ACCOUNT error");
    }

    public static String encode(String data) {
        byte[] b = data.getBytes();
        //遍历
        for(int i=0;i<b.length;i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }
}
