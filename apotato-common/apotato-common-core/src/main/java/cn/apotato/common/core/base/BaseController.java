package cn.apotato.common.core.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * 基本控制器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Slf4j
public abstract class BaseController<T, ID> {

    @Autowired
    protected IService<T> service;

    /**
     * 服务
     *
     * @return {@link IService}
     */
    protected abstract IService<T> service();



    @PostMapping
    public T create(@RequestBody T entity) {
        log.debug("Creating a new entity with information: {}", entity);
        Assert.isTrue(service.save(entity), "Failed to create a entity！");
        return entity;
    }

    @GetMapping("/{id}")
    public T findById(@PathVariable("id") ID id) {
        log.debug("Finding entity by id: {}", id);
        return service.getById((Serializable) id);
    }

    @GetMapping
    public IPage<T> findByPage(IPage<T> page, T entity) throws IllegalAccessException {
        log.debug("Finding all entities by page: {}, {}", page, entity);
        // 创建通用查询条件
        BaseQueryWrapper<T> baseQueryWrapper = new BaseQueryWrapper<>();
        if (page != null) {
            page = new Page<>(1, 10);
        }
        page = service.page(page, baseQueryWrapper.createLambdaQueryWrapper(entity, true));
        // 设置分页
        long total = service.count(baseQueryWrapper.createLambdaQueryWrapper(entity, false));
        long pages = total % page.getSize() > 0 ? (total / page.getSize()) + 1 : (total / page.getSize());
        page.setPages(pages);
        page.setTotal(total);
        return page;
    }

    @PutMapping
    public T update(@RequestBody T entity) {
        log.debug("Updating entity with id: {}", entity);
        Assert.isTrue(service.updateById(entity), "Failed to update a entity！");
        return entity;
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") ID id) {
        log.debug("Deleting entity by id: {}", id);
        service.removeById((Serializable)id);
    }
}
