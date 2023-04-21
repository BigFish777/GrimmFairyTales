package cn.apotato.modules.test.service.impl;

import cn.apotato.modules.test.entity.TopographicInformation;
import cn.apotato.modules.test.mapper.TopographicInformationMapper;
import cn.apotato.modules.test.service.TopographicInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 地形信息服务 实现类
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@Service
public class TopographicInformationServiceImpl extends ServiceImpl<TopographicInformationMapper, TopographicInformation> implements TopographicInformationService {
}
