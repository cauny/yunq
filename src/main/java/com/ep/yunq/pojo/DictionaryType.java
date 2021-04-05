package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @classname: DictionaryType
 * @Author: yan
 * @Date: 2021/4/4 21:42
 * 功能描述： 字典类型
 **/
@Entity
@Table(name="dictionary_type")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class DictionaryType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int code;   //
    private String name;    //
    private boolean status;     //



}
