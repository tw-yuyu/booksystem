package com.booksystem.service.impl;

import com.booksystem.context.LocalThreadHolder;
import com.booksystem.mapper.UserMapper;
import com.booksystem.pojo.api.ApiResult;
import com.booksystem.pojo.api.PageResult;
import com.booksystem.pojo.api.Result;
import com.booksystem.pojo.dto.query.base.QueryDto;
import com.booksystem.pojo.dto.query.extend.UserQueryDto;
import com.booksystem.pojo.dto.update.UserLoginDTO;
import com.booksystem.pojo.dto.update.UserRegisterDTO;
import com.booksystem.pojo.dto.update.UserUpdateDTO;
import com.booksystem.pojo.em.LoginStatusEnum;
import com.booksystem.pojo.em.RoleEnum;
import com.booksystem.pojo.em.WordStatusEnum;
import com.booksystem.pojo.entity.User;
import com.booksystem.pojo.vo.ChartVO;
import com.booksystem.pojo.vo.UserVO;
import com.booksystem.service.UserService;
import com.booksystem.utils.DateUtil;
import com.booksystem.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用戶服務實現類
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 用戶註冊
     *
     * @param userRegisterDTO 註冊入參
     * @return Result<String> 響應結果
     */
    @Override
    public Result<String> register(UserRegisterDTO userRegisterDTO) {
        User user = userMapper.getByActive(
                User.builder().userName(userRegisterDTO.getUserName()).build()
        );
        if (Objects.nonNull(user)) {
            return ApiResult.error("用戶名已經被使用，請換一個");
        }
        User entity = userMapper.getByActive(
                User.builder().userAccount(userRegisterDTO.getUserAccount()).build()
        );
        if (Objects.nonNull(entity)) {
            return ApiResult.error("帳號不可用");
        }
        User saveEntity = User.builder()
                .userRole(RoleEnum.USER.getRole())
                .userName(userRegisterDTO.getUserName())
                .userAccount(userRegisterDTO.getUserAccount())
                .userAvatar(userRegisterDTO.getUserAvatar())
                .userPwd(userRegisterDTO.getUserPwd())
                .userEmail(userRegisterDTO.getUserEmail())
                .createTime(LocalDateTime.now())
                .isLogin(LoginStatusEnum.USE.getFlag())
                .isWord(WordStatusEnum.USE.getFlag()).build();
        userMapper.insert(saveEntity);
        return ApiResult.success("註冊成功");
    }

    /**
     * 用戶登錄
     *
     * @param userLoginDTO 登錄入參
     * @return Result<String> 響應結果
     */
    @Override
    public Result<Object> login(UserLoginDTO userLoginDTO) {
        User user = userMapper.getByActive(
                User.builder().userAccount(userLoginDTO.getUserAccount()).build()
        );
        if (!Objects.nonNull(user)) {
            return ApiResult.error("帳號不存在");
        }
        if (!Objects.equals(userLoginDTO.getUserPwd(), user.getUserPwd())) {
            return ApiResult.error("密碼錯誤");
        }
        if (user.getIsLogin()) {
            return ApiResult.error("登錄狀態異常");
        }
        String token = JwtUtil.toToken(user.getId(), user.getUserRole());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("role", user.getUserRole());
        return ApiResult.success("登錄成功", map);
    }

    /**
     * 令牌檢驗 -- 認證成功返回用戶訊息
     *
     * @return Result<UserVO>
     */
    @Override
    public Result<UserVO> auth() {
        Integer userId = LocalThreadHolder.getUserId();
        User queryEntity = User.builder().id(userId).build();
        User user = userMapper.getByActive(queryEntity);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ApiResult.success(userVO);
    }

    /**
     * 分頁查詢用戶數據
     *
     * @param userQueryDto 分頁參數
     * @return Result<List < User>> 響應結果
     */
    @Override
    public Result<List<User>> query(UserQueryDto userQueryDto) {
        List<User> users = userMapper.query(userQueryDto);
        Integer count = userMapper.queryCount(userQueryDto);
        return PageResult.success(users, count);
    }

    /**
     * 用戶訊息修改
     *
     * @param userUpdateDTO 修改訊息入參
     * @return Result<String> 響應結果
     */
    @Override
    public Result<String> update(UserUpdateDTO userUpdateDTO) {
        User updateEntity = User.builder().id(LocalThreadHolder.getUserId()).build();
        BeanUtils.copyProperties(userUpdateDTO, updateEntity);
        userMapper.update(updateEntity);
        return ApiResult.success();
    }


    /**
     * 批量刪除用戶訊息
     */
    @Override
    public Result<String> batchDelete(List<Integer> ids) {
        userMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 用戶訊息修改密碼
     *
     * @param map 修改訊息入參
     * @return Result<String> 響應結果
     */
    @Override
    public Result<String> updatePwd(Map<String, String> map) {
        String oldPwd = map.get("oldPwd");
        String newPwd = map.get("newPwd");
        String againPwd = map.get("againPwd");
        if (Objects.isNull(oldPwd)) {
            return ApiResult.error("原始密碼輸入不能爲空");
        }
        if (Objects.isNull(newPwd)) {
            return ApiResult.error("請輸入新密碼");
        }
        if (Objects.isNull(againPwd)) {
            return ApiResult.error("請補充確認密碼");
        }
        if (!newPwd.equals(againPwd)) {
            return ApiResult.error("前後密碼輸入不一致");
        }
        User user = userMapper.getByActive(
                User.builder().id(LocalThreadHolder.getUserId()).build()
        );
        if (!user.getUserPwd().equals(oldPwd)) {
            return ApiResult.error("原始密碼驗證失敗");
        }
        user.setUserPwd(newPwd);
        userMapper.update(user);
        return ApiResult.success();
    }

    /**
     * 通過ID查詢用戶訊息
     *
     * @param id 用戶ID
     */
    @Override
    public Result<UserVO> getById(Integer id) {
        User user = userMapper.getByActive(User.builder().id(id).build());
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return ApiResult.success(userVO);
    }

    /**
     * 後臺新增用戶
     *
     * @param userRegisterDTO 註冊入參
     * @return Result<String> 響應結果
     */
    @Override
    public Result<String> insert(UserRegisterDTO userRegisterDTO) {
        return register(userRegisterDTO);
    }

    /**
     * 後臺用戶訊息修改
     *
     * @param user 訊息實體
     * @return Result<String> 響應結果
     */
    @Override
    public Result<String> backUpdate(User user) {
        userMapper.update(user);
        return ApiResult.success();
    }

    /**
     * 統計指定時間裏面的用戶存量數據
     *
     * @param day 天數
     * @return Result<List < ChartVO>>
     */

    /**
     * List<LocalDateTime> localDateTimes = userList.stream().map(User::getCreateTime).collect(Collectors.toList());
     * 相同於
     * List<LocalDateTime> times = new ArrayList<>();
     * for (User user : userList) {
     *     times.add(user.getCreateTime());
     * }
     */
    @Override
    public Result<List<ChartVO>> daysQuery(Integer day) {
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        UserQueryDto userQueryDto = new UserQueryDto();
        userQueryDto.setStartTime(queryDto.getStartTime());
        userQueryDto.setEndTime(queryDto.getEndTime());
        List<User> userList = userMapper.query(userQueryDto);
        List<LocalDateTime> localDateTimes = userList.stream().map(User::getCreateTime).collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(day, localDateTimes);
        return ApiResult.success(chartVOS);
    }
}
