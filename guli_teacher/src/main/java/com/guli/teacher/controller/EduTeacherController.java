package com.guli.teacher.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guli.common.result.Result;
import com.guli.common.result.ResultCode;
import com.guli.teacher.entity.EduTeacher;
import com.guli.teacher.entity.TeacherQuery.TeacherQuery;
import com.guli.teacher.exception.EduException;
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
    public Result list() {

        try {
            List<EduTeacher> list = teacherService.list(null);
            return Result.ok().data("items", list);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("{id}") //占位符
    public Result deleteTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable(value = "id") String id) {
        try {
            teacherService.removeById(id);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "根据讲师条件分页查询")
    @PostMapping("/{page}/{limit}")
    public Result selectTeacherByPage(
            @ApiParam(name = "page", value = "当前页", required = true)
            @PathVariable(value = "page") Integer page,
            @ApiParam(name = "limit", value = "每页显示记录数", required = true)
            @PathVariable(value = "limit") Integer limit,
            @RequestBody TeacherQuery query
    ) {
        try {
            Page<EduTeacher> teacherPage = new Page<>(page, limit);
            teacherService.pageQuery(teacherPage, query);
            List<EduTeacher> teacherList = teacherPage.getRecords();
            long total = teacherPage.getTotal();
            return Result.ok().data("total", total).data("items", teacherList);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "根据Id查询讲师")
    @PostMapping("{id}")
    public Result selectTeacherById(
            @ApiParam(name = "id", value = "讲师id", required = true)
            @PathVariable(value = "id") String id
    ) {
        try {
            EduTeacher teacher = teacherService.getById(id);
            if (teacher == null) {
                throw new EduException(ResultCode.EDU_ID_ERROR,"没有此讲师信息");
            }
            return Result.ok().data("teacher", teacher);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "修改讲师信息")
    @PutMapping("update")
    public Result updateTeacherById(
            @RequestBody EduTeacher eduTeacher
    ) {
        try {
            teacherService.updateById(eduTeacher);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @ApiOperation(value = "新增讲师")
    @PostMapping("save")
    public Result saveTeacher(
            @ApiParam(name = "teacher", value = "讲师对象", required = true)
            @RequestBody EduTeacher teacher
    ) {
        try {
            teacherService.save(teacher);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}

