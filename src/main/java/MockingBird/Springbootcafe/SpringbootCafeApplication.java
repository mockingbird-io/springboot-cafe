package MockingBird.Springbootcafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootCafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootCafeApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/res")
    public String res(@RequestParam(value = "haha", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/lazy")
    public String lazy(@RequestParam(value = "lazy", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/admin")
    public String admin(@RequestParam(value = "admin", defaultValue = "login") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "login", defaultValue = "admin") String name) {
        return String.format("Hello %s!", name);
    }

    @GetMapping("/update")
    public String update(@RequestParam(value = "success", defaultValue = "update") String name) {
        return String.format("Hello %s!", name);
    }
}
