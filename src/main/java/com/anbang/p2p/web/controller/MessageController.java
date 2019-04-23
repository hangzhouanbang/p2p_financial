package com.anbang.p2p.web.controller;

import com.anbang.p2p.plan.bean.LeaveWord;
import com.anbang.p2p.plan.dao.LeaveWordDao;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.web.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 留言、通知
 */
@CrossOrigin
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private LeaveWordDao leaveWordDao;

    @RequestMapping("/listLeaveWord")
    public CommonVO listLeaveWord(@RequestParam(name = "page", defaultValue = "1") Integer page,
                                  @RequestParam(name = "size", defaultValue = "20") Integer size) {
        List<LeaveWord> leaveWords = leaveWordDao.list(page,size);
        return CommonVOUtil.success(leaveWords, "success");
    }

    @RequestMapping("/deleteLeaveWordS")
    public CommonVO deleteLeaveWordS(@RequestParam(name = "ids") String[] ids) {
        leaveWordDao.deleteByIds(ids);
        return CommonVOUtil.success("success");
    }

//    @RequestMapping("/getNotification")
//    public CommonVO getNotification() {
//        List<LeaveWord> leaveWords = leaveWordDao.list(page,size);
//        return CommonVOUtil.success(leaveWords, "success");
//    }



}
