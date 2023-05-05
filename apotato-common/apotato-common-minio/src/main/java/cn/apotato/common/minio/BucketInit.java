package cn.apotato.common.minio;

import cn.apotato.common.minio.properties.MinioProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

/**
 * 桶 初始化
 *
 * @author 胡晓鹏
 * @date 2023/05/04
 */
@Slf4j
@Component
public class BucketInit implements CommandLineRunner {


    @Resource
    private MinioProperties properties;
    @Resource
    private MinioTemplate minioTemplate;

    @Override
    public void run(String... args) {
        createMinIoBucket();
    }


    /**
     * 创建最minio桶
     */
    private void createMinIoBucket() {
        CompletableFuture.runAsync(() -> {
            if (!minioTemplate.bucketExists(properties.getBucketName())) {
                log.info("创建MinIo桶...");
                minioTemplate.makeBucket(properties.getBucketName());
            }
        });
    }
}

