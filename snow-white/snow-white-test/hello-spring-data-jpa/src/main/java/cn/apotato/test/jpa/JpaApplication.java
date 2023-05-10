package cn.apotato.test.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * jpa应用程序
 *
 * @author 胡晓鹏
 * @date 2023/05/09
 */
@EnableJpaRepositories
@EnableTransactionManagement
@SpringBootApplication
public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class);
    }
}
