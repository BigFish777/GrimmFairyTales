package cn.apotato.modules.test.mapper;

import cn.apotato.modules.test.entity.Organization;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 组织映射器
 *
 * @author 胡晓鹏
 * @date 2023/05/05
 */
@Mapper
public interface OrganizationMapper extends MPJBaseMapper<Organization> {
}
