package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @classname: dictionaryDetail
 * @Author: yan
 * @Date: 2021/4/3 20:15
 * 功能描述： 字典细节内容
 **/
@Entity
@Table(name="dictionary_detail")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class DictionaryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    int id;

    private int sort;

    private String name;

    private int value;

    private int defaultValue;

    private int status;

    @ManyToOne()
    @JoinColumn(name = "type_id")
    DictionaryType dictionaryType;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    public DictionaryDetail() {
    }

    public DictionaryDetail(int id, int sort, String name, int value, int defaultValue, int status, DictionaryType dictionaryType) {
        this.id = id;
        this.sort = sort;
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.status = status;
        this.dictionaryType = dictionaryType;
    }

    public DictionaryDetail(int sort, String name, int value, int defaultValue, int status, DictionaryType dictionaryType) {
        this.sort = sort;
        this.name = name;
        this.value = value;
        this.defaultValue = defaultValue;
        this.status = status;
        this.dictionaryType = dictionaryType;
    }
}
