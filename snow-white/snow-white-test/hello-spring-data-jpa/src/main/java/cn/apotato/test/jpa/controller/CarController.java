package cn.apotato.test.jpa.controller;

import cn.apotato.test.jpa.domain.UserInfo;
import cn.apotato.test.jpa.service.UserInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 用户信息控制器
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
@RequestMapping("car")
@RestController
public class CarController {

    @Resource
    private UserInfoService userInfoService;

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     * @return {@link UserInfo}
     */
    @PostMapping
    public UserInfo saveUserInfo(@RequestBody UserInfo userInfo) {
        return userInfoService.saveUserInfo(userInfo);
    }

    /**
     * 得到用户信息列表
     *
     * @param userInfo 用户信息
     * @param size     大小
     * @param page     页面
     * @return {@link Page}<{@link UserInfo}>
     */
    @GetMapping
    public Page<UserInfo> getUserInfoList(UserInfo userInfo,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          @RequestParam(defaultValue = "1") Integer page) {
        return userInfoService.findUserInfoList(userInfo, PageRequest.of(page - 1, size));
    }
}
