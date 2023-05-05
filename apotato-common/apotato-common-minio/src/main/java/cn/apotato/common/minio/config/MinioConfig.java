package cn.apotato.common.minio.config;

import cn.apotato.common.minio.properties.MinioProperties;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * MinIo客户端配置
 *
 * @author 胡晓鹏
 * @date 2023/04/13
 */
@EnableConfigurationProperties(MinioProperties.class)
@Configuration
public class MinioConfig {

    @Resource
    private MinioProperties properties;

    /**
     * minio 客户端
     *
     * @return {@link MinioClient}
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }
}
