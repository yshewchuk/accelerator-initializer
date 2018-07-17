package {{PACKAGE}};

{{#IS_SPRING_BOOT_APP}}
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
{{/IS_SPRING_BOOT_APP}}
public class Application {

{{#IS_SPRING_BOOT_APP}}
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
{{/IS_SPRING_BOOT_APP}}
}
