package com.ep.yunq.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @classname: PageUtil
 * @Author: yan
 * @Date: 2021/4/20 14:17
 * 功能描述：
 **/
@Slf4j
@Component
public class PageUtil {

    public static <R,T> Page<R> pageChange(Page<T> page,Type type){
        Pageable pageable=page.getPageable();
        long totalElements=page.getTotalElements();

        ModelMapper modelMapper=new ModelMapper();
        List<T> oldPages= page.getContent();
        List<R> pages=new ArrayList<>();
        for(T t:oldPages){
            R r=modelMapper.map(t,type);
            pages.add(r);
        }
        Page<R> newPages=new PageImpl<>(pages,pageable,totalElements);
        return newPages;
    }

    public static <T> Page<T> listToPage(List<T> list,Pageable pageable,Long totalElements){
        Page<T> pages=new PageImpl<>(list,pageable,totalElements);
        return pages;
    }

    public static <T> Page<T> listToPage(List<T> list,int pageNumber,int pageSize){
        Sort sort=Sort.by(Sort.Direction.ASC,"id");
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);
        //当前页第一条数据在list中的位置
        int start = (int) pageable.getOffset();
        //当前页最后一条数据在list中的位置
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        Page<T> pages=new PageImpl<>(list.subList(start,end),pageable,list.size());
        return pages;
    }

    public static <R,T> List<R> listChange(List<T> list,Type type){
        ModelMapper modelMapper=new ModelMapper();
        List<R> newLists=new ArrayList<>();
        for(T t:list){
            R r=modelMapper.map(t,type);
            newLists.add(r);
        }
        return newLists;
    }





}
