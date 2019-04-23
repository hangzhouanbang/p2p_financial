package com.anbang.p2p.plan.dao;


import com.anbang.p2p.plan.bean.LeaveWord;

import java.util.List;

/**
 * @Description:
 */
public interface LeaveWordDao {
    void save(LeaveWord leaveWord);

    void deleteByIds(String[] ids);

    long getCount();

    List<LeaveWord> list(int page, int size);
}
