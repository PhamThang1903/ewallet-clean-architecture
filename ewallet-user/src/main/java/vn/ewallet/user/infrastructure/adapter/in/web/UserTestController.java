package vn.ewallet.user.infrastructure.adapter.in.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserTestController {

    @GetMapping("/api/v1/users/ping")
    public String ping() {
        return "user-service is still running";
    }
}
