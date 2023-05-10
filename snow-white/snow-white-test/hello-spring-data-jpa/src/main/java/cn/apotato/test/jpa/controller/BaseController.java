package cn.apotato.test.jpa.controller;

import cn.apotato.test.jpa.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 基本控制器
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
public class BaseController <T>{

    protected final BaseService<T> service;

    public BaseController(BaseService<T> service) {
        this.service = service;
    }


    /**
     * 保存
     *
     * @param value 价值
     * @return {@link T}
     */
    public T save(T value) {
        return service.save(value);
    }

    /**
     * 删除
     *
     * @param value 价值
     * @return boolean
     */
    public boolean remove(T value) {
        return service.remove(value);
    }

    /**
     * 删除通过id
     *
     * @param id id
     * @return boolean
     */
    public boolean removeById(Long id) {
        return service.removeById(id);
    }

    /**
     * 更新
     *
     * @param value 价值
     * @return {@link T}
     */
    public T update(T value) {
        return service.update(value);
    }


    /**
     * 获取页面
     *
     * @param value    价值
     * @param pageable 可分页
     * @return {@link Page}<{@link T}>
     */
    public Page<T> getPage(T value, Pageable pageable) {
        return service.getPage(value, pageable);
    }
}
