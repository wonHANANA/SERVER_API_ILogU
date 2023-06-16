package com.onehana.server_ilogu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import com.onehana.server_ilogu.dto.SmsDto;
import com.onehana.server_ilogu.dto.request.SmsRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.SmsResponse;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.UserRepository;
import com.onehana.server_ilogu.util.VerificationCode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SmsService {

    private final UserRepository userRepository;
    private final Map<String, VerificationCode> verifyCodes = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate;
    private final RateLimiter rateLimiter;

    @Value("${naver.cloud.sms.accessKey}")
    private String accessKey;

    @Value("${naver.cloud.sms.secretKey}")
    private String secretKey;

    @Value("${naver.cloud.sms.serviceId}")
    private String serviceId;

    @Value("${naver.cloud.sms.senderPhone}")
    private String fromPhone;

    @PostConstruct
    public void init() {
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @PostConstruct
    public void scheduleCleanupTask() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(this::cleanupExpiredCodes, 0, 24, TimeUnit.HOURS);
    }

    public SmsResponse sendVerifySms(String email, String toPhone) throws JsonProcessingException, RestClientException,
            URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {

        if (!rateLimiter.tryAcquire()) {
            throw new BaseException(BaseResponseStatus.EXCEED_VERIFY_REQUEST);
        }

        verifyUser(email, toPhone);
        VerificationCode verifyCode = createVerificationCode(email);

        List<SmsDto> messages = new ArrayList<>();
        messages.add(new SmsDto(toPhone));

        SmsRequest request = SmsRequest.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(fromPhone)
                .content("ILogU 인증번호는 ["+verifyCode.getCode()+"] 입니다.")
                .messages(messages)
                .build();

        Long time = System.currentTimeMillis();
        HttpHeaders headers = createHeaders(time);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+
                serviceId +"/messages"), httpBody, SmsResponse.class);
    }

    private HttpHeaders createHeaders(Long time) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));

        return headers;
    }

    private VerificationCode createVerificationCode(String email) {
        Random random = new Random();
        String generateCode = String.valueOf(random.nextInt(900000) + 100000);

        long expirationTime = System.currentTimeMillis() + 3 * 60 * 1000;

        VerificationCode verifyCode = new VerificationCode(generateCode, expirationTime);
        verifyCodes.remove(email);
        verifyCodes.put(email, verifyCode);

        return verifyCode;
    }

    private void verifyUser(String email, String toPhone) {
        userRepository.findByEmail(email).ifPresent(it -> {
            throw new BaseException(BaseResponseStatus.DUPLICATED_EMAIL);
        });
        userRepository.findByPhone(toPhone).ifPresent(it -> {
            throw new BaseException(BaseResponseStatus.DUPLICATED_PHONE);
        });
    }

    public boolean isVerifiedCode(String email, String verificationCode) {
        if (verifyCodes.containsKey(email)) {
            VerificationCode storedCode = verifyCodes.get(email);
            if (storedCode.getCode().equals(verificationCode) && storedCode.isValid()) {
                verifyCodes.remove(email);
                return true;
            }
        }
        return false;
    }

    public String makeSignature(Long time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/"+ this.serviceId+"/messages";
        String timestamp = time.toString();
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        return Base64.encodeBase64String(rawHmac);
    }

    private void cleanupExpiredCodes() {
        Iterator<Map.Entry<String, VerificationCode>> iterator = verifyCodes.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, VerificationCode> entry = iterator.next();
            if (!entry.getValue().isValid()) {
                iterator.remove();
            }
        }
    }
}
