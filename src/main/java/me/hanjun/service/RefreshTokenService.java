package me.hanjun.service;

import lombok.RequiredArgsConstructor;
import me.hanjun.domain.RefreshToken;
import me.hanjun.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String token) {

        return refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(() -> new IllegalArgumentException("UnExpected token"));
    }
}
