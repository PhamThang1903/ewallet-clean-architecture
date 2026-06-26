package vn.ewallet.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final StringRedisTemplate redisTemplate;
    private final SecureRandom random = new SecureRandom();

    public String generateOtp(String userId, String purpose) {
        String rateKey = "otp_rate:" + userId + ":" + purpose;
        Long count = redisTemplate.opsForValue().increment(rateKey);
        if (count != null && count == 1) {
            redisTemplate.expire(rateKey, Duration.ofMinutes(10));
        }

        if (count != null && count > 3)
            throw new IllegalArgumentException("Too many OTP requests. Please try again later.");

        String otp = String.valueOf(100000 + random.nextInt(900000));
        String otpKey = "otp:" + userId + ":" + purpose;
        redisTemplate.opsForValue().set(otpKey, otp, Duration.ofMinutes(5));
        return otp;
    }

    public boolean verifyOtp(String userId, String purpose, String otp) {
        String otpKey = "otp:" + userId + ":" + purpose;
        String savedOtp = redisTemplate.opsForValue().get(otpKey);
        if (savedOtp == null) {
            return false;
        }
        boolean matched = savedOtp.equals(otp);
        if (matched)
            redisTemplate.delete(otpKey);
        return matched;
    }
}
