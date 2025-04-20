package com.booksystem.controller;

import com.booksystem.aop.Pager;
import com.booksystem.aop.Protector;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.extend.UserQueryDto;
import com.booksystem.pojo.dto.update.UserLoginDTO;
import com.booksystem.pojo.dto.update.UserRegisterDTO;
import com.booksystem.pojo.dto.update.UserUpdateDTO;
import com.booksystem.pojo.entity.User;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.pojo.vo.UserVO;
import com.booksystem.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用戶登錄
     *
     * @param userLoginDTO 登錄入參
     * @return Result<String> 響應結果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Result<Object> login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }


    /**
     * token校驗
     */
    @Protector
    @GetMapping(value = "/auth")
    @ResponseBody
    public Result<UserVO> auth() {
        return userService.auth();
    }


    /**
     * 通過ID查詢用戶信息
     *
     * @param id 用戶ID
     * @return Result<UserVO>
     */
    @Protector
    @GetMapping(value = "/getById/{id}")
    @ResponseBody
    public Result<UserVO> getById(@PathVariable Integer id) {
        return userService.getById(id);
    }


    /**
     * 用戶註冊
     *
     * @param userRegisterDTO 註冊入參
     * @return Result<String> 響應結果
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public Result<String> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.register(userRegisterDTO);
    }

    /**
     * 後臺新增用戶
     *
     * @param userRegisterDTO 註冊入參
     * @return Result<String> 響應結果
     */
    @Protector(role = "管理員")
    @PostMapping(value = "/insert")
    @ResponseBody
    public Result<String> insert(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.insert(userRegisterDTO);
    }

    /**
     * 用戶信息修改
     *
     * @param userUpdateDTO 修改信息入參
     * @return Result<String> 響應結果
     */
    @Protector
    @PutMapping(value = "/update")
    @ResponseBody
    public Result<String> update(@RequestBody UserUpdateDTO userUpdateDTO) {
        return userService.update(userUpdateDTO);
    }

    /**
     * 後臺用戶信息修改
     *
     * @param user 信息實體
     * @return Result<String> 響應結果
     */
    @Protector(role = "管理員")
    @PutMapping(value = "/backUpdate")
    @ResponseBody
    public Result<String> backUpdate(@RequestBody User user) {
        return userService.backUpdate(user);
    }

    /**
     * 用戶修改密碼
     *
     * @param map 修改信息入參
     * @return Result<String> 響應結果
     */
    @PutMapping(value = "/updatePwd")
    @ResponseBody
    public Result<String> updatePwd(@RequestBody Map<String, String> map) {
        return userService.updatePwd(map);
    }

    /**
     * 批量刪除用戶信息
     */
    @Protector(role = "管理員")
    @PostMapping(value = "/batchDelete")
    @ResponseBody
    public Result<String> batchDelete(@RequestBody List<Integer> ids) {
        return userService.batchDelete(ids);
    }

    /**
     * 查詢用戶數據
     *
     * @param userQueryDto 查詢參數
     * @return Result<List < User>> 響應結果
     */
    @Pager
    @Protector(role = "管理員")
    @PostMapping(value = "/query")
    @ResponseBody
    public Result<List<User>> query(@RequestBody UserQueryDto userQueryDto) {
        return userService.query(userQueryDto);
    }

    /**
     * 統計用戶存量數據
     *
     * @return Result<List < ChartVO>> 響應結果
     */
    @GetMapping(value = "/daysQuery/{day}")
    @ResponseBody
    public Result<List<ChartVO>> query(@PathVariable Integer day) {
        return userService.daysQuery(day);
    }

}

