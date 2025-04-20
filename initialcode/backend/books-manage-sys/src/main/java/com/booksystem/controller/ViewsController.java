package com.booksystem.controller;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.service.ViewsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 可視化接口
 */
@RestController
@RequestMapping(value = "/views")
public class ViewsController {

    @Resource
    private ViewsService viewsService;

    /**
     * 統計一些系統的基礎數據
     *
     * @return Result<List < ChartVO>>
     */
    @GetMapping("/staticControls")
    public Result<List<ChartVO>> staticControls() {
        return viewsService.staticControls();
    }

}
