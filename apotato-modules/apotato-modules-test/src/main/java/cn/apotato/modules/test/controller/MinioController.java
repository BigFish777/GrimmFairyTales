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
 * minio测试
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
     * 预览文件
     *
     * @param fileName 文件名称
     * @param response 响应
     */
    @GetMapping("preview")
    public void previewFile(String fileName, HttpServletResponse response){
        minioTemplate.download(fileName, MinioTemplate.PREVIEW,response);
    }

}
