package com.ep.yunq.domain.dao;

import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.domain.entity.Subject;
import org.apache.lucene.index.DocIDMerger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface SubjectDAO extends JpaRepository<Subject, Integer> {
    Subject findById(int id);
    Subject findByName(String name);

    Page<Subject> findAll(Pageable pageable);

    @Modifying
    @Transactional
    void deleteById(int id);
}
