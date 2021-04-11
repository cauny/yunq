package com.ep.yunq.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @classname: SchoolInstitution
 * @Author: yan
 * @Date: 2021/4/10 22:37
 * 功能描述：
 **/

@Entity
@Table(name = "school_institution")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class SchoolInstitution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     *学校名称或下属机构名称
     */
    private String name;

    /**
     *机构级别字符串
     */
    private String level;

    /**
     *排序
     */
    private int sort;

    /**
     *父级主键id
     */
    @Column(name = "parent_id")
    private int parentId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 子机构
     */
    @Transient
    private List<SchoolInstitution> children;
}
