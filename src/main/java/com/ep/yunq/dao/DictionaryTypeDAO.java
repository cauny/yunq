package com.ep.yunq.dao;

import com.ep.yunq.pojo.DictionaryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
  * @Author: yan
  * @Date: 2021/4/10 22:34
  **/
public interface DictionaryTypeDAO extends JpaRepository<DictionaryType,Integer> {

    DictionaryType findById(int id);

    DictionaryType findByCode(String code);

    Page<DictionaryType> findAll(Pageable pageable);
}
