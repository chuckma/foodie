package com.imooc.service;

import com.imooc.pojo.Stu;

import java.util.List;

/**
 * @Author mcg
 * @Date 2019/11/14 22:02
 **/

public interface StuService {

    public Stu getStuInfo(int id);

    public void saveStu();

    public void updateStu(int id);

    public void deleteStu(int id);

    public void saveParent();
    public void saveChildren();


    public void saveStuList(List<Stu> list);

}
