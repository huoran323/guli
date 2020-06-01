package com.guli.teacher.controller;


import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.service.EduTeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author guli
 * @since 2020-06-01
 */
@RestController
@RequestMapping("/teacher")
@Api(description = "讲师管理")
public class EduTeacherController {

    @Autowired
    private EduTeacherService teacherService;

    @ApiOperation(value = "所有讲师列表")
    @GetMapping("list")
    public List<EduTeacher> list() {
        return teacherService.list(null);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("{id}") //占位符
    public Boolean deleteTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable(value = "id") String id) {
        try {
            teacherService.removeById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

