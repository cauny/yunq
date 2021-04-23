package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.DictionaryType;
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
