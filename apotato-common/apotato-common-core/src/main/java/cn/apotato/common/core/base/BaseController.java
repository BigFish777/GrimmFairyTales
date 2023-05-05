package cn.apotato.common.core.base;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.base.MPJBaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 基本控制器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Slf4j
public class BaseController<T, ID> {

    protected final IService<T> service;

    protected final MPJBaseMapper<T> mapper;

    public BaseController(IService<T> service, MPJBaseMapper<T> mapper) {
        this.service = service;
        this.mapper = mapper;
    }


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
        // 设置查询参数
        entity = setQueryParamHook(entity);
        // 创建通用查询条件
        BaseQueryWrapper<T> baseQueryWrapper = new BaseQueryWrapper<>();
        if (page != null) {
            page = new Page<>(1, 10);
        }
        page = service.page(page, setQueryCriteriaHook(baseQueryWrapper.createLambdaQueryWrapper(entity, true).lambda()));
        // 设置分页
        long total = service.count(setQueryCriteriaHook(baseQueryWrapper.createLambdaQueryWrapper(entity, false).lambda()));
        long pages = total % page.getSize() > 0 ? (total / page.getSize()) + 1 : (total / page.getSize());
        page.setPages(pages);
        page.setTotal(total);
        return queryResultFilterHook(page);
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


    /**
     * 查询过滤钩子函数
     *
     * @param page 页面
     * @return {@link IPage}<{@link T}>
     */
    public IPage<T> queryResultFilterHook(IPage<T> page) {
        // 层级过滤形成过滤链子
        page.setRecords(queryResultFilterHook(page.getRecords()));
        return page;
    }

    /**
     * 查询过滤钩子函数
     *
     * @param records 记录
     * @return {@link List}<{@link T}>
     */
    public List<T> queryResultFilterHook(List<T> records) {
        records.forEach(this::queryResultFilterHook);
        for (T record : records) {
            System.out.println(record);
        }
        return records;
    }

    /**
     * 查询过滤钩子函数
     *
     * @param object 对象
     * @return {@link T}
     */
    public T queryResultFilterHook(T object) {
        return object;
    }

    /**
     * 设置查询参数钩子函数
     *
     * @param queryParam 查询参数
     * @return {@link T}
     */
    public T setQueryParamHook(T queryParam) {
        return queryParam;
    }

    /**
     * 设置查询参数钩子函数
     *
     * @param lambdaQueryWrapper 查询包装
     * @return {@link QueryWrapper}<{@link T}>
     */
    public LambdaQueryWrapper<T> setQueryCriteriaHook(LambdaQueryWrapper<T> lambdaQueryWrapper) {
        return lambdaQueryWrapper;
    }
}
