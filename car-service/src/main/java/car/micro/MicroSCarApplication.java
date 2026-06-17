package car.micro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
public class MicroSCarApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroSCarApplication.class, args);
    }
}