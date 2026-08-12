package ar.edu.utn.dds.k3003.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(scanBasePackages = "ar.edu.utn.dds.k3003")
@EnableJpaRepositories(basePackages = "ar.edu.utn.dds.k3003.repositories")
@EntityScan(basePackages = "ar.edu.utn.dds.k3003.model")
@EnableFeignClients(basePackages = "ar.edu.utn.dds.k3003.apisexternas")
@EnableScheduling
public class Application{
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

}
