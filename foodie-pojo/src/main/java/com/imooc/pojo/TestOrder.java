package com.imooc.pojo;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "test_order")
public class TestOrder {
    @Id
    private Integer id;

    private String description;

    private Long cost;

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
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return cost
     */
    public Long getCost() {
        return cost;
    }

    /**
     * @param cost
     */
    public void setCost(Long cost) {
        this.cost = cost;
    }


    @Override
    public String toString() {
        return "TestOrder{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                '}';
    }
}