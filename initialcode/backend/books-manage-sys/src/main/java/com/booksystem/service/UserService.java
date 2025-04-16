package com.booksystem.service;

import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.update.UserLoginDTO;
import com.booksystem.pojo.dto.update.UserRegisterDTO;
import com.booksystem.pojo.dto.update.UserUpdateDTO;
import com.booksystem.pojo.dto.query.extend.UserQueryDto;
import com.booksystem.pojo.entity.User;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.pojo.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * 用戶服務類
 */
public interface UserService {
    Result<String> register(UserRegisterDTO userRegisterDTO);

    Result<Object> login(UserLoginDTO userLoginDTO);

    Result<UserVO> auth();

    Result<List<User>> query(UserQueryDto userQueryDto);

    Result<String> update(UserUpdateDTO userUpdateDTO);

    Result<String> batchDelete(List<Integer> ids);

    Result<String> updatePwd(Map<String, String> map);

    Result<UserVO> getById(Integer id);

    Result<String> insert(UserRegisterDTO userRegisterDTO);

    Result<String> backUpdate(User user);

    Result<List<ChartVO>> daysQuery(Integer day);

}
