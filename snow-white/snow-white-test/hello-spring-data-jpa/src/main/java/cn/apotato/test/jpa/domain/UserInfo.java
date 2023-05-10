package cn.apotato.test.jpa.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * 用户信息
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
@EntityListeners(AuditingEntityListener.class)
@Data
@Entity(name = "user_info")
public class UserInfo implements Serializable {

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
     * &#064;Column定义该属性对应数据库中的列名。
     */
    @Column(name = "name")
    private String name;
    /**
     * 项目代码
     */
    @Column(name = "sex")
    private String sex;
    /**
     * 作业名
     */
    @Column(name = "phone")
    private String phone;
    /**
     * 类型
     */
    @Column(name = "type")
    private String type;

    /**
     * mappedBy 指定 Pet 中的对应关系属性 user
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Pet> pets;

    /**
     * 车
     * @deprecated 注解解释：
     * 加了 @OneToOne 注解之后，系统会自动添加一个名为 address_cid
     * 的字段，这个字段是一个外键，跟 address 关联，形成一对一。
     * 设置 Car 和 UserInfo 之间是一对一的关系, cascade = CascadeType.ALL 设置所有的级联操作
     * 设置 @JoinColumn， 第一个 name 属性是指当前类中的 cid 属性，第二个属性是指所引用的外部类的属性名
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cid", referencedColumnName = "id")
    private Car car;

    /**
     * 描述
     * &#064;Transient表示该属性并非一个到数据库表的字段的映射，表示非持久化属性，与@Basic作用相反。JPA映射数据库的时候忽略它。
     */
    @Transient
    private String description;
}
