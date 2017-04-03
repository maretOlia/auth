package giraffe.auth;


import giraffe.SharedConfigurationReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@SpringBootApplication
@Import(SharedConfigurationReference.class)
public class GiraffeAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiraffeAuthApplication.class, args);
	}

}


