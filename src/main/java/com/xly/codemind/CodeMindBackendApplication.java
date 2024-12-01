package com.xly.codemind;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.xly.codemind.mapper")
@EnableSwagger2
public class CodeMindBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeMindBackendApplication.class, args);
    }

}
