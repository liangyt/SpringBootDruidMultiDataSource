package com.liangyt.service;

import com.liangyt.entity.datasourceone.DsOne;
import com.liangyt.entity.datasourcetwo.DsTwo;
import com.liangyt.mapper.one.DsOneMapper;
import com.liangyt.mapper.two.DsTwoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 描述：服务类
 * 创建时间 2017-12-14 10:50
 * 作者 liangyongtong
 */
@Service
public class MultiDataSourceService {
    @Autowired
    private DsOneMapper dsOneMapper;

    @Autowired
    private DsTwoMapper dsTwoMapper;

    @Transactional
    public void insert(DsOne dsOne, DsTwo dsTwo) {
        dsOneMapper.insert(dsOne);
        dsTwoMapper.insert(dsTwo);
    }

    /**
     * 返回两个表的数据
     * @return
     */
    public List list() {
        List one = dsOneMapper.selectAll();
        List two = dsTwoMapper.selectAll();

        if (Objects.nonNull(one) && Objects.nonNull(two)) one.addAll(two);
        else one = two;
        return one;
    }
}
