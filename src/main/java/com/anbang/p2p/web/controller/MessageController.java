package com.anbang.p2p.web.controller;

import com.anbang.p2p.plan.bean.LeaveWord;
import com.anbang.p2p.plan.bean.Notification;
import com.anbang.p2p.plan.dao.LeaveWordDao;
import com.anbang.p2p.util.CommonVOUtil;
import com.anbang.p2p.web.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/getNotification")
    public CommonVO getNotification() {
        Map data = Notification.getMap();
        return CommonVOUtil.success(data, "success");
    }

    @RequestMapping("/updateNotification")
    public CommonVO updateNotification(String applySuccess, String repaySuccess, String repayFront, String repayDay,
                                       String beyond1, String beyond3, String beyond7, String beyond15) {
        Map map = new HashMap();
        map.put("applySuccess", applySuccess);
        map.put("repaySuccess", repaySuccess);
        map.put("repayFront", repayFront);
        map.put("repayDay", repayDay);
        map.put("beyond1", beyond1);
        map.put("beyond3", beyond3);
        map.put("beyond7", beyond7);
        map.put("beyond15", beyond15);
        Notification.update(map);
        return CommonVOUtil.success("success");
    }
}
