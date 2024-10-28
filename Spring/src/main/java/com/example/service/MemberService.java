package com.example.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Member;
import com.example.repository.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MemberService {
	
	@Autowired
    private MemberRepository memberRepository;
	
	
	
	public String getAccessTokenFromKakao(String client_id, String code) throws IOException {
        //------kakao POST 요청------
        String reqURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id="+client_id+"&code=" + code;
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");


        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });

        log.info("Response Body : " + result);

        String accessToken = (String) jsonMap.get("access_token");
        String refreshToken = (String) jsonMap.get("refresh_token");
        String scope = (String) jsonMap.get("scope");

        log.info("Access Token : " + accessToken);
        log.info("Refresh Token : " + refreshToken);
        log.info("Scope : " + scope);

        return accessToken;
    }

    @SuppressWarnings("unchecked")
	public Member getUserInfo(String accessToken) throws IOException {
        //------kakao GET 요청------
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        int responseCode = conn.getResponseCode();
        System.out.println("responseCode : " + responseCode);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        log.info("Response Body : " + result);

        // jackson objectmapper 객체 생성
        ObjectMapper objectMapper = new ObjectMapper();
        // JSON String -> Map
        Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
        });


        //사용자 정보 추출
        Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
        Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");	


        Long id = (Long) jsonMap.get("id");
        String nickname = properties.get("nickname").toString();
        String profileImage = properties.get("profile_image").toString();
        String email = (kakao_account.get("email") != null) ? kakao_account.get("email").toString() : null; // 이메일이 null일 수도 있으므로 체크

        //MemberEntity 객체 생성
        return new Member(id, nickname, profileImage, email);
    }
    
    public void loginUser(Member member) {
		if (memberRepository.findById(member.getId()).isPresent()) {
			log.info("login successed");
			
            return ; // 이미 등록된 사용자면 그냥 리턴
        }
		memberRepository.save(member);
		log.info("new member registered");
    }
    public void logoutUser(String accessToken) throws IOException {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        HttpURLConnection conn = (HttpURLConnection) new URL(reqURL).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            log.info("Kakao logout succeeded");
        } else {
            log.error("Kakao logout failed: " + responseCode);
            throw new IOException("Logout failed with response code " + responseCode); // 예외 발생
        }
    }

}
