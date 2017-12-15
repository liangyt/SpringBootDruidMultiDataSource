package com.liangyt.multidatasource;

import com.liangyt.Application;
import com.liangyt.entity.datasourceone.DsOne;
import com.liangyt.entity.datasourcetwo.DsTwo;
import com.liangyt.service.MultiDataSourceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 描述：多数据源测试
 * 创建时间 2017-12-14 10:56
 * 作者 liangyongtong
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MultiDataSourceTest {
    @Autowired
    private MultiDataSourceService multiDataSourceService;

    @Test
    public void testInsert() {
        DsOne dsOne = new DsOne();
        dsOne.setId(1);
        dsOne.setOne("one1");

        DsTwo dsTwo = new DsTwo();
        dsTwo.setId(1);
        dsTwo.setTwo("two1");

        multiDataSourceService.insert(dsOne, dsTwo);
    }

    @Test
    public void testList() {
        System.out.println(multiDataSourceService.list());
    }
}
