package com.example.demo.web;

import com.example.demo.config.ResultData;
import com.example.demo.entity.Dashboard;
import com.example.demo.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin")
@Api(value = "仪表盘", tags = {"仪表盘"})
public class AdminDashboardController {


    @Resource
    private DashboardService dashboardservice;

    /**
     * @return
     */
    @ApiOperation(value = "admin获取仪表盘信息")
    @GetMapping("/dashboard")
    public ResultData<Dashboard> getDashboard() {
        return ResultData.success(dashboardservice.getDashboard());
    }
}
