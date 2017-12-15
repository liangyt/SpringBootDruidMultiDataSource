package com.liangyt.mapper.two;

import com.liangyt.entity.datasourcetwo.DsTwo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DsTwoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DsTwo record);

    DsTwo selectByPrimaryKey(Integer id);

    List<DsTwo> selectAll();

    int updateByPrimaryKey(DsTwo record);
}