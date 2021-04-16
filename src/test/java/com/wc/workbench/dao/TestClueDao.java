package com.wc.workbench.dao;
import com.wc.utils.SqlSessionUtil;
import com.wc.workbench.domain.Clue;
import org.junit.Test;


public class TestClueDao {
    @Test
    public void queryClueById2() {
        ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        Clue clue = clueDao.queryClueById2("3817123a73684ec79a87743e6f9d837a");
        System.out.println(clue);
    }
}
