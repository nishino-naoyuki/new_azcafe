package jp.ac.asojuku.azcafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AzCafeApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AzCafeApplication.class, args);
	}
}
