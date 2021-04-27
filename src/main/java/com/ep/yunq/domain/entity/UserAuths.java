package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * @classname: UserAuths
 * @Author: yan
 * @Date: 2021/4/26 16:54
 * 功能描述：
 **/
@Entity
@Table(name="user_auths")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class UserAuths {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;

    private Integer userId;
    private String identityType;
    private String identifier;
    private String credential;



}
