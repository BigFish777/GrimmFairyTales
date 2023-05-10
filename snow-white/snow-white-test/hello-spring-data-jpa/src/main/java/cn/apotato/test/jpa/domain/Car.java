package cn.apotato.test.jpa.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 车
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
@EntityListeners(AuditingEntityListener.class)
@Data
@Entity(name = "car")
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 在创建
     */
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    /**
     * 更新在
     */
    @LastModifiedDate
    private Timestamp updatedAt;

    /**
     * 名字
     */
    private String name;

    /**
     * 颜色
     */
    private String color;
    /**
     * 品牌
     */
    private String brand;

    @Column(name = "license_plate")
    private String licensePlate;
    /**
     * 价格
     */
    private Double price;

}
