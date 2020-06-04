package com.guli.teacher.controller;


import com.guli.common.result.Result;
import com.guli.teacher.service.EduSubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-06-03
 */
@RestController
@RequestMapping("/subject")
@CrossOrigin
public class EduSubjectController {

    @Autowired
    private EduSubjectService eduSubjectService;

    @PostMapping("import")
    public Result importSubject(MultipartFile file) {

        //因为考虑到模板中的数据不准确，所以返回多个错误信息，那么多个错误信息放在集合中
        List<String> mesList = eduSubjectService.importEXCL(file);
        if (mesList.size() == 0) {
            return Result.ok();
        } else {
            return Result.ok().data("messageList",mesList);
        }

    }

}

