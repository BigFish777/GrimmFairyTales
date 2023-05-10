package cn.apotato.modules.minio;

import lombok.Data;

/**
 * 对象项
 *
 * @author 胡晓鹏
 * @date 2023/04/13
 */
@Data
public class ObjectItem {
    private String objectName;
    private Long size;
}
