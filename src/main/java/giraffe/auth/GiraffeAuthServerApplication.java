package giraffe.auth;


import giraffe.SharedConfigurationReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@Import(SharedConfigurationReference.class)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class GiraffeAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GiraffeAuthServerApplication.class, args);
	}

}


