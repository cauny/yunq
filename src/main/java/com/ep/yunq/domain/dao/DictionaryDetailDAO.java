package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.DictionaryDetail;
import com.ep.yunq.domain.entity.DictionaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:35
  **/
public interface DictionaryDetailDAO extends JpaRepository<DictionaryDetail,Integer> {

    DictionaryDetail findById(int id);

    DictionaryDetail findByDefaultValue(int defaultValue);

    @Query(nativeQuery = true, value = "select * from dictionary_detail where type_id = ?1 order by sort ")
    List<DictionaryDetail> findAllByDictionaryTypeOrderBySort(int dicTypeId);

    @Query(nativeQuery = true, value = "select di.name,di.value from dictionary_detail di " +
            "left join dictionary_type dt on dt.id = di.type_id " +
            "where dt.code = ?1 and dt.`status` = '1' and di.`status` = '1' order by di.`sort`")
    List<Map<String,String>> findAllByCode(int code);

    @Query(nativeQuery = true, value = "select * from dictionary_detail where value = ?1 and type_id = ?2")
    DictionaryDetail findByValueAndDictionaryType(int value, int typeId);

    Page<DictionaryDetail> findByDictionaryType(DictionaryType dictionaryType, Pageable pageable);
}
