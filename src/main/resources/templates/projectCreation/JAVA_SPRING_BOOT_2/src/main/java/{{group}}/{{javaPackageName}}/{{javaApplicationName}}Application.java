package {{group}}.{{javaPackageName}};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class {{javaApplicationName}}Application {

	public static void main(String[] args) {
		SpringApplication.run({{javaApplicationName}}Application.class, args);
	}
}
