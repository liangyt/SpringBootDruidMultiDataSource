package com.liangyt.mapper.one;

import com.liangyt.entity.datasourceone.DsOne;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DsOneMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DsOne record);

    DsOne selectByPrimaryKey(Integer id);

    List<DsOne> selectAll();

    int updateByPrimaryKey(DsOne record);
}