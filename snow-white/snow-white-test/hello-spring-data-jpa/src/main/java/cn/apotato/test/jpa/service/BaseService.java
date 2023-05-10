package cn.apotato.test.jpa.service;

import cn.apotato.test.jpa.domain.UserInfo;
import cn.apotato.test.jpa.repository.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

/**
 * 基础服务
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
public class BaseService</**
 * t
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */ T> {

    @PersistenceContext
    private EntityManager entityManager;

    protected final BaseRepository<T, Long> repository;


    public BaseService(BaseRepository<T, Long> repository) {
        this.repository = repository;
    }

    /**
     * 保存
     *
     * @param value 价值
     * @return {@link T}
     */
    public T save(T value) {
        return repository.save(value);
    }

    /**
     * 删除
     *
     * @param value 价值
     * @return boolean
     */
    public boolean remove(T value) {
        repository.delete(value);
        return true;
    }

    /**
     * 删除通过id
     * 删除
     *
     * @param id id
     * @return boolean
     */
    public boolean removeById(Long id) {
        repository.deleteById(id);
        return true;
    }

    /**
     * 更新
     *
     * @param value 价值
     * @return {@link T}
     */
    public T update(T value) {
        return repository.save(value);
    }

    /**
     * 获取页面
     *
     * @param value    价值
     * @param pageable 可分页
     * @return {@link Page}<{@link T}>
     */
    public Page<T> getPage(T value, Pageable pageable) {
        Example<T> example = Example.of(value);
        return repository.findAll(example, pageable);
    }
}
