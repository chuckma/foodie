package com.test;

import com.imooc.Application;
import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import com.imooc.service.TestTransService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mcg
 * @create 2019-11-15-9:06
 */

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
public class TransTest {

    @Autowired
    private StuService stuService;
    @Autowired
    private Sid sid;

    @Autowired
    private TestTransService testTransService;

//    @Test
    public void myTest() {
//        stuService.testPropagationTrans();
        testTransService.testPropagationTrans();
    }


//    @Test
    public void test1() {
        List<Stu> list = new ArrayList<>();
        Stu s1 = new Stu();
        s1.setAge(12);
        s1.setName("lucasfa");
        s1.setId(RandomUtils.nextInt(0,6000000));

        Stu s2 = new Stu();
        s2.setAge(12);
        s2.setName("lucas");
        s2.setId(RandomUtils.nextInt(0,6000000));

        Stu s3 = new Stu();
        s3.setAge(12);
        s3.setName("kuehe");
        s3.setId(RandomUtils.nextInt(0,6000000));

        list.add(s1);
        list.add(s2);
        list.add(s3);
        stuService.saveStuList(list);
    }
}
