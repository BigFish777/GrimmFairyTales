package cn.apotato.test.jpa.repository;

import cn.apotato.test.jpa.domain.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


/**
 * 用户信息存储库
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
public interface UserInfoRepository extends BaseRepository<UserInfo, Long> {
    /**
     * 找到用户信息名字和电话是忽略大小写
     *
     * @param name     名字
     * @param phone    电话
     * @param pageable 可分页
     * @return {@link Page}<{@link UserInfo}>
     */
    Page<UserInfo> findUserInfosByNameIsLikeAndPhoneIsLikeIgnoreCase(String name, String phone, Pageable pageable);

    /**
     * 通过电话和删除id
     *
     * @param phone 电话
     * @param id    id
     */
    @Transactional(rollbackFor = RuntimeException.class)
    void deleteByPhoneAndId(String phone, String id);
}
