package com.ep.yunq.application.dto;

import com.ep.yunq.domain.entity.SchoolInstitution;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @classname: SchoolInstitutionDTO
 * @Author: yan
 * @Date: 2021/4/28 16:55
 * 功能描述：
 **/
@Data
public class SchoolInstitutionDTO {
    private int id;
    private String name;
    private int sort;
    private int parentId;
    private Boolean hasChildren;

    private List<SchoolInstitutionDTO> children;
}
