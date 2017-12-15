package com.liangyt.restcontroller;

import com.liangyt.entity.datasourceone.DsOne;
import com.liangyt.entity.datasourcetwo.DsTwo;
import com.liangyt.service.MultiDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：接口访问
 * 创建时间 2017-12-15 13:51
 * 作者 liangyongtong
 */
@RestController
@RequestMapping(value = "/multiapi")
public class MultiDataSourceRestController {

    @Autowired
    private MultiDataSourceService multiDataSourceService;

    @GetMapping("/save")
    public Object save() {

        DsOne dsOne = new DsOne();
        dsOne.setId(1);
        dsOne.setOne("one1");

        DsTwo dsTwo = new DsTwo();
        dsTwo.setId(1);
        dsTwo.setTwo("two1");

        multiDataSourceService.insert(dsOne, dsTwo);

        Map map = new HashMap();
        map.put("code", 1);
        map.put("message", "成功");
        return map;
    }

    @GetMapping("list")
    public Object get() {
        return multiDataSourceService.list();
    }
}
