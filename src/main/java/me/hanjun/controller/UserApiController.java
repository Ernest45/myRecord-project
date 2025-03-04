package me.hanjun.controller;

import lombok.RequiredArgsConstructor;
import me.hanjun.dto.AddUserRequest;
import me.hanjun.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserApiController {

    private final UserService userService;

    @PostMapping("/user")
    public String signuo(AddUserRequest request) {
        userService.save(request);
        return "redirect:/login";
    }
}
