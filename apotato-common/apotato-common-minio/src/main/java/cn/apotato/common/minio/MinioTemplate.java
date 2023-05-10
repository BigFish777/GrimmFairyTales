package cn.apotato.common.minio;

import cn.apotato.common.minio.config.MinioConfig;
import cn.apotato.common.minio.properties.MinioProperties;
import cn.hutool.core.util.IdUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * minio跑龙套
 *
 * @author 胡晓鹏
 * @date 2023/04/13
 */

@Component
@Slf4j
public class MinioTemplate {
    public static final String DOWNLOAD = "download";
    public static final String PREVIEW = "preview";

    final MinioProperties prop;

    final MinioClient minioClient;

    public MinioTemplate(MinioProperties prop, MinioClient minioClient) {
        this.prop = prop;
        this.minioClient = minioClient;
    }

    /**
     * 查看存储bucket是否存在
     * @return boolean
     */
    public Boolean bucketExists(String bucketName) {
        Boolean found;
        try {
            found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return found;
    }

    /**
     * 创建存储bucket
     * @return Boolean
     */
    public Boolean makeBucket(String bucketName) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 删除存储bucket
     * @return Boolean
     */
    public Boolean removeBucket(String bucketName) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 获取全部bucket
     */
    public List<Bucket> getAllBuckets() {
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            return buckets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file, String contentType) {

        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)){
            originalFilename = file.getName();
        }
        String objectName = getObjectName(originalFilename);
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1).contentType(contentType).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectName;
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)){
            originalFilename = file.getName();
        }
        String objectName = getObjectName(originalFilename);
        InputStream is = null;
        try {
            is = file.getInputStream();
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(prop.getBucketName()).object(objectName)
                    .stream(is, file.getSize(), -1).contentType(file.getContentType()).build();
            //文件名称相同会覆盖
            minioClient.putObject(objectArgs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return objectName;
    }

    /**
     * 得到对象名称
     *
     * @param originalFilename 原始文件名
     * @return {@link String}
     */
    @NotNull
    private static String getObjectName(String originalFilename) {
        String fileName = IdUtil.getSnowflake().nextIdStr() + originalFilename.substring(originalFilename.lastIndexOf("."));
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int dey = now.getDayOfMonth();
        return year + "/" + month + "/" + dey + "/" + fileName;
    }

    /**
     * 文件上传
     *
     * @param file 文件
     * @return Boolean
     */
    public String upload(File file) {
        // 创建FileInputStream对象
        InputStream is = null;
        String objectName = null;
        try {
            is = new FileInputStream(file);
            // 创建MockMultipartFile对象，并指定上传文件的参数
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), is);
            objectName = upload(multipartFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return objectName;
    }

    /**
     * 文件上传
     *
     * @param inputStream 文件
     * @param fileName 文件名称
     * @return Boolean
     */
    public String upload(InputStream inputStream, String fileName, String contentType){
        // 创建FileInputStream对象
        String objectName = null;
        try {
            // 创建MockMultipartFile对象，并指定上传文件的参数
            MultipartFile multipartFile = new MockMultipartFile(fileName, inputStream);
            objectName = upload(multipartFile, contentType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return objectName;
    }

    /**
     * 预览
     * @param fileName
     * @return
     */
    public String preview(String fileName){
        // 查看文件地址
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs.builder()
                .bucket(prop.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     * @param fileName 文件名称
     * @param response response
     * @return Boolean
     */
    public void download(String fileName, HttpServletResponse response) {
        download(fileName,DOWNLOAD, response);
    }

    /**
     * 文件下载
     * @param fileName 文件名称
     * @param response response
     * @return Boolean
     */
    public void download(String fileName, String action, HttpServletResponse response) {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(prop.getBucketName())
                .object(fileName).build();
        try (GetObjectResponse objectResponse = minioClient.getObject(objectArgs)){
            response.setCharacterEncoding("utf-8");
            if (DOWNLOAD.equals(action)) {
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            } else if (PREVIEW.equals(action)) {
                String mimeType = URLConnection.guessContentTypeFromName(fileName);
                if (mimeType == null) {
                    mimeType = Files.probeContentType(Paths.get(fileName));
                }
                response.setContentType(mimeType);
                response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
            }

            byte[] buf = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()){
                while ((len=objectResponse.read(buf))!=-1){
                    os.write(buf,0,len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                try (ServletOutputStream stream = response.getOutputStream()){
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查看文件对象
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return items;
    }

    /**
     * 删除
     * @param fileName
     * @return
     * @throws Exception
     */
    public boolean remove(String fileName){
        try {
            minioClient.removeObject( RemoveObjectArgs.builder().bucket(prop.getBucketName()).object(fileName).build());
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
