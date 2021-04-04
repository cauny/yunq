package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

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



}
