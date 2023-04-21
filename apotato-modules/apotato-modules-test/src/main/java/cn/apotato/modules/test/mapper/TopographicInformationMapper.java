package cn.apotato.modules.test.mapper;

import cn.apotato.modules.test.entity.TopographicInformation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地形信息映射器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Mapper
public interface TopographicInformationMapper extends BaseMapper<TopographicInformation> {
}
