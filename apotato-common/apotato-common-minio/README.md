# 公共模块Redis
## 📚使用手册
### 依赖引用
```xml
    <parent>
        <groupId>cn.apotato</groupId>
        <artifactId>apotato-common-minio</artifactId>
        <version>1.0-SNAPSHOT</version>
    </parent>
```

### 🎁Redis配置文件

```yaml
# minio
minio:
  #minio服务地址
  endpoint: ${MINIO_URL:http://10.4.5.161:9000}
  #账户
  accessKey: ${MINIO_ACCESS_KEY:AKIAIOSFODNN7EXAMPLE}
  #密码
  secretKey: ${MINIO_SECRET_KEY:wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY}
  #桶名称
  bucketName: ${MINIO_BUCKET_NAME:forest-one}

```

### 🍺Hello Minio

**官网文档**
> http://www.minio.org.cn/docs/minio/container/operations/install-deploy-manage/deploy-minio-single-node-single-drive.html

**搭建 Minio 环境**
使用docker搭建一个简答的单机环境
```shell
docker run -dt                                  \
  -p 9000:9000 -p 9090:9090                     \
  -v PATH:/mnt/data                             \
  -v /etc/default/minio:/etc/config.env         \
  -e "MINIO_CONFIG_ENV_FILE=/etc/config.env"    \
  --name "minio_local"                          \
  minio server --console-address ":9090"
```


**MinioTemplate的使用**

```java
package cn.apotato.modules.test.controller;

import cn.apotato.common.minio.MinioTemplate;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * minio控制器
 *
 * @author 胡晓鹏
 * @date 2023/05/04
 */
@AllArgsConstructor
@RequestMapping("minio")
@RestController
public class MinioController {

    private final MinioTemplate minioTemplate;


    /**
     * 上传文件
     *
     * @param file 文件
     * @return {@link String}
     */
    @PostMapping("upload")
    public String uploadFile(MultipartFile file){
        return minioTemplate.upload(file);
    }


    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping("download")
    public void downloadFile(String fileName, HttpServletResponse response){
        minioTemplate.download(fileName, response);
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping("preview")
    public void previewFile(String fileName, HttpServletResponse response){
        minioTemplate.download(fileName, MinioTemplate.PREVIEW,response);
    }

}


```
