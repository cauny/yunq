package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @classname: subject
 * @Author: yan
 * @Date: 2021/6/6 20:53
 * 功能描述：
 **/
@Entity
@Table(name = "subject")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    private String name;
}
