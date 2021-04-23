package com.ep.yunq.domain.service;

import com.ep.yunq.domain.dao.CourseDAO;
import com.ep.yunq.domain.entity.Course;
import com.ep.yunq.infrastructure.util.CommonUtil;
import com.ep.yunq.infrastructure.util.ConstantUtil;
import com.ep.yunq.infrastructure.util.QrcodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @classname: CourseService
 * @Author: yan
 * @Date: 2021/4/12 11:15
 * 功能描述：
 */

@Service
public class CourseService {
    @Autowired
    CourseDAO courseDAO;
    @Autowired
    UserService userService;

    public void addOrUpdate(Course course) {
        course.setModifitionDate(new Date());
        courseDAO.save(course);
    }

    public Course getById(int id) {
        return courseDAO.findById(id);
    }

    public String add(Course course) {
        String message = "";
        try {
            // 封面

            course.setCover(course.getCover());
            /*course.setQrcode("");*/
//            course.setCreator(userService.getCurrentUserId());
            addOrUpdate(course);
            // 二维码
            String qrcode = CommonUtil.creatUUID() +  ".jpg";
            String imagePath = ConstantUtil.FILE_QrCode.string +  qrcode;
            String content = String.valueOf(course.getId());
            /*QrcodeUtil.encodeimage(imagePath, "JPEG", content, 430, 430 , logo);*/
            String imgURL = ConstantUtil.FILE_Url_QrCode.string  + qrcode;
            /*course.setQrcode(qrcode);*/
            addOrUpdate(course);
            message = imgURL;
        } catch (Exception e) {
            message = "参数异常，添加失败";
            e.printStackTrace();
        }
        return message;
    }

    public String edit(Course course) {
        String message = "";
        try{
            Course courseInDB = courseDAO.findById(course.getId());
            courseInDB.setName(course.getName());
            courseInDB.setGrade(course.getGrade());
            courseInDB.setSemester(course.getSemester());
            courseInDB.setSchool(course.getSchool());
            courseInDB.setCollege(course.getCollege());
            courseInDB.setMajor(course.getMajor());
            courseInDB.setTeacher(course.getTeacher());
            courseInDB.setLearnRequire(course.getLearnRequire());
            courseInDB.setTeachProgress(course.getTeachProgress());
            courseInDB.setExamArrange(course.getExamArrange());
            /*courseInDB.setSchoolId(course.getSchoolId());
            courseInDB.setCollegeId(course.getCollegeId());*/

            addOrUpdate(courseInDB);
            message = "修改成功";
        } catch (Exception e) {
            message = "参数异常，修改失败";
            e.printStackTrace();
        }

        return message;
    }

    public String editContainCover(Course course, MultipartFile file) {
        String message = "";
        try{
            Course courseInDB = courseDAO.findById(course.getId());
            courseInDB.setName(course.getName());
            courseInDB.setGrade(course.getGrade());
            courseInDB.setSemester(course.getSemester());
            courseInDB.setSchool(course.getSchool());
            courseInDB.setCollege(course.getCollege());
            courseInDB.setMajor(course.getMajor());
            courseInDB.setTeacher(course.getTeacher());
            courseInDB.setLearnRequire(course.getLearnRequire());
            courseInDB.setTeachProgress(course.getTeachProgress());
            courseInDB.setExamArrange(course.getExamArrange());
            /*courseInDB.setSchoolId(course.getSchoolId());
            courseInDB.setCollegeId(course.getCollegeId());*/

            if (null != file) {
                File imageFolder = new File(ConstantUtil.FILE_Photo_Course.string);
                File f = new File(imageFolder, CommonUtil.creatUUID() + file.getOriginalFilename()
                        .substring(file.getOriginalFilename().length() - 4));
                file.transferTo(f);
                courseInDB.setCover(f.getName());
            }
            addOrUpdate(courseInDB);
            message = "修改成功";
        } catch (Exception e) {
            message = "参数异常，修改失败";
            e.printStackTrace();
        }

        return message;
    }

    public String updateCover(MultipartFile file, int cid) throws Exception {
        File imageFolder = new File(ConstantUtil.FILE_Photo_Course.string);
        File f = new File(imageFolder, CommonUtil.creatUUID() + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        try {
            file.transferTo(f);
            String imgURL = ConstantUtil.FILE_Url_Course.string+ f.getName();

            Course courseInDB = courseDAO.findById(cid);
            courseInDB.setCover(f.getName());
            addOrUpdate(courseInDB);
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "更新失败";
        }
    }

    @Modifying
    @Transactional
    public String delete(int cid) {
        String message = "";
        try {
            courseDAO.deleteById(cid);
            message = "删除成功";
        } catch (Exception e) {
            message = "参数异常，删除失败";
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }

        return message;
    }

    public String batchDelete(LinkedHashMap courseIds) {
        String message = "";
        for (Object value : courseIds.values()) {
            for (int cid :(List<Integer>)value) {
                message = delete(cid);
                if (!"删除成功".equals(message)) {
                    break;
                }
            }
        }
        return message;
    }

    public List<Course> findAllByCreatorId(int uid){
        List<Course> courses = courseDAO.findAllByCreator(uid);
        for (Course course:courses) {
            course.setCover(ConstantUtil.FILE_Url_Course.string+course.getCover());
            /*course.setQrcode(ConstantUtil.FILE_Url_QrCode.string+course.getQrcode());*/
        }
        return courses;
    }

    public List<Course> findAll(){
        Sort sort = Sort.by(Sort.Direction.ASC, "semester", "grade");
        List<Course> courses = courseDAO.findAll(sort);
        /*for (Course course:courses) {
            course.setCover(ConstantUtil.FILE_Url_Course.string+course.getCover());
            *//*course.setQrcode(ConstantUtil.FILE_Url_QrCode.string+course.getQrcode());*//*
        }*/
        return courses;
    }

    public List<Course> search(String keywords) {
        List<Course> courses = courseDAO.findAllByNameLikeAndTeacherLikeAndGradeLikeAndSemesterLikeOrderBySemesterAsc(
                "%" + keywords + "%", "%" + keywords + "%", "%" + keywords + "%", "%" + keywords + "%");
        return courses;
    }

    public Course findById(int cid) {
        try {
            Course course = courseDAO.findById(cid);
            if (null == course)
                return null;
            course.setCover(ConstantUtil.FILE_Url_Course.string+course.getCover());
            /*course.setQrcode(ConstantUtil.FILE_Url_QrCode.string+course.getQrcode());*/
            return course;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
