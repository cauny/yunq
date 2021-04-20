package com.ep.yunq.util;

import com.ep.yunq.dto.SysParamDTO;
import com.ep.yunq.pojo.SysParam;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}
