package cn.apotato.test.jpa.service;

import cn.apotato.test.jpa.domain.UserInfo;
import cn.apotato.test.jpa.repository.UserInfoRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * 用户信息服务
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
@Service
public class UserInfoService {
    @PersistenceContext
    private EntityManager em;

    @Resource
    UserInfoRepository userInfoRepository;

    /**
     * 保存所有
     *
     * @param userInfoList 用户信息列表
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveAll(List<UserInfo> userInfoList) {
        for (UserInfo userInfo : userInfoList) {
            em.persist(userInfo);
        }
    }

    /**
     * 删除所有
     *
     * @param userInfoList 用户信息列表
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteAll(List<UserInfo> userInfoList) {
        for (UserInfo userInfo : userInfoList) {
            em.remove(userInfo);
        }
        userInfoRepository.deleteAll(userInfoList);
    }

    /**
     * 查找用户信息
     * 找到用户信息名字和电话是忽略大小写
     *
     * @param userInfo 用户信息
     * @param pageable 可分页
     * @return {@link Page}<{@link UserInfo}>
     */
    public Page<UserInfo> findUserInfoList(UserInfo userInfo, Pageable pageable) {
        Example<UserInfo> example = Example.of(userInfo);
        return userInfoRepository.findAll(example, pageable);
    }

    /**
     * 查找用户信息
     * 找到用户信息名字和电话是忽略大小写
     *
     * @param userInfo 用户信息
     * @param pageable 可分页
     * @return {@link Page}<{@link UserInfo}>
     */
    public Page<UserInfo> findUserInfoList2(UserInfo userInfo, Pageable pageable) {
        // 1. 获取 CriteriaBuilder
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // 2. 创建 CriteriaQuery
        CriteriaQuery<UserInfo> query = cb.createQuery(UserInfo.class);

        // 3. 设置查询根和关联关系
        Root<UserInfo> user = query.from(UserInfo.class);
        //   // 一的一方关联多的一方
        user.join("pets");

        // 4. 设置查询条件
        // name like %张三%
        Predicate condition1 = cb.like(user.get("name"), "%"+userInfo.getName()+"%");
        // id = 1
        Predicate condition2 = cb.equal(user.get("id"), userInfo.getId());
        // name like "张三" or id = 1
        Predicate finalCondition = cb.or(condition1, condition2);

        // 5. 设置查询选择和查询条件
        query.select(user).where(finalCondition);

        // 6. 获取查询结果
        List<UserInfo> resultList = em.createQuery(query)
                // 页码从 0 开始,第 2 页为 10,第 3 页为 20,
                // FirstResult = (page - 1) * 10; page >= 1
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                // 每页 10 条
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        // 获取总记录数
        int total = em.createQuery(query)
                .getResultList().size();

        // 设置分页信息并返回
        return new PageImpl<>(resultList, pageable, total);
    }

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     * @return {@link UserInfo}
     */
    public UserInfo saveUserInfo(UserInfo userInfo) {
        userInfoRepository.saveAndFlush(userInfo);
        return userInfo;
    }

}
