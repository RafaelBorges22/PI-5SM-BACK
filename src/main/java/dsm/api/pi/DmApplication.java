package dsm.api.pi;

import dsm.api.pi.Config.PixConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
public class DmApplication {

	public static void main(String[] args) {
		SpringApplication.run(DmApplication.class, args);
	}

}
