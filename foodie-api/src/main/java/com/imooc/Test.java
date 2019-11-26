package com.imooc;

import com.imooc.pojo.Stu;
import com.imooc.pojo.Stu2;
import org.springframework.beans.BeanUtils;

/**
 * @Author mcg
 * @Date 2019/11/26 20:48
 *
 * // 测试类，可删除。
 **/

public class Test {


    public static void main(String[] args) {
        Stu s = new Stu(122133, "cafda", 12);

        Stu2 stu2 = new Stu2();
        BeanUtils.copyProperties(s, stu2);
        System.out.println(stu2.getNickName());

    }
}
