package com.guli.teacher.controller;

import com.guli.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@Api(description = "用户管理")
@CrossOrigin //解决跨域
public class UserController {

    @ApiOperation(value = "用户登录")
    @PostMapping("login")
    public Result login() {
        return Result.ok().data("token","admin");
    }

    @ApiOperation(value = "获取用户信息")
    @GetMapping("info")
    public Result info() {

        return Result.ok().data("roles","['admin']").data("name","admin").data("avatar","ceshi");
    }
}
