package com.imooc.pojo;

import javax.persistence.Id;

public class Stu2 {
    @Id
    private Integer id;

    private String name;

    private Integer age;

    private String nickName;

    public Stu2() {
    }

    public Stu2(Integer id, String name, Integer age, String nickName) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Stu2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}