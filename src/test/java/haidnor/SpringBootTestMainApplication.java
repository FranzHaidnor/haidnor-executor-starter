package haidnor;

import haidnor.executor.config.HFConfig;
import haidnor.executor.controller.ThreadPoolController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(excludeFilters =
        {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {HFConfig.class,
                        ThreadPoolController.class
                })}
)
public class SpringBootTestMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestMainApplication.class, args);
    }

}
