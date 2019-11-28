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
        Stu2 stu21 = new Stu2();
        stu2.setAge(12);
        stu21.setAge(12);
        System.out.println(stu2.hashCode());
        System.out.println(stu21.hashCode());
        System.out.println(stu2.equals(stu21));

        System.out.println(stu2.getNickName());

    }
}
