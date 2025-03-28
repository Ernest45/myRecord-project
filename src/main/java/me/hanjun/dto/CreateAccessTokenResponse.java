package me.hanjun.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
public class CreateAccessTokenResponse {
    private String accessToken;

}
