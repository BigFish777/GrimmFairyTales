package cn.apotato.modules.test.controller;

import cn.apotato.common.core.base.BaseController;
import cn.apotato.modules.test.entity.TopographicInformation;
import cn.apotato.modules.test.service.TopographicInformationService;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 地形信息控制器
 *
 * @author 胡晓鹏
 * @date 2023/04/21
 */
@AllArgsConstructor
@RequestMapping("/top")
@RestController
public class TopographicInformationController extends BaseController<TopographicInformation, Long> {

    private final TopographicInformationService topographicInformationService;

    @Override
    protected IService<TopographicInformation> service() {
        this.service = topographicInformationService;
        return service;
    }
}
