package com.ep.yunq.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    private String code;   //
    private String name;    //
    private Integer status;     //

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 字典信息
     */
    @OneToMany(mappedBy = "dictionaryType",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    List<DictionaryDetail> dictionaryInfos = new ArrayList<>();


    public DictionaryType() {
    }

    public DictionaryType(int id, String code, String name, Integer status) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.status = status;
    }

    public DictionaryType(String code, String name, Integer status) {
        this.code = code;
        this.name = name;
        this.status = status;
    }
}
