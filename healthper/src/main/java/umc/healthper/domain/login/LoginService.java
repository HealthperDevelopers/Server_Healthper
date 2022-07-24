package umc.healthper.domain.login;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import umc.healthper.domain.login.model.OAuthToken;
import umc.healthper.domain.login.model.KakaoId;

@Service
public class LoginService {
    public Long kakoLogin(String code){
        ResponseEntity<String> response = getAccessToken(code);

        OAuthToken oAuthToken = JsonToObject(response);

        //토큰으로 id 얻기
        ResponseEntity<String> userInfo = getUserInfo(oAuthToken.getAccess_token());

        KakaoId kakaoId = JsonToKakao(userInfo);

        return kakaoId.getId();
    }
    private ResponseEntity<String> getAccessToken(String code){
        //Post방식으로 key=value 데이터를 요청(카카오 쪽으로)
        RestTemplate rt = new RestTemplate();//android -> Retrofit2
        HttpHeaders headers = new HttpHeaders();
        //header
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        //body
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", LoginArgs.grant_type);
        params.add("client_id",LoginArgs.api_key);
        params.add("redirect_uri",LoginArgs.redirect_uri);
        params.add("code",code);

        //header body 연결
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest =
                new HttpEntity<>(params,headers);

        //요청과 응답을 동시에
        ResponseEntity<String> response = rt.exchange(
                LoginArgs.get_token_uri,
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        return response;
    }

    private KakaoId JsonToKakao(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        KakaoId kakaoId = null;
        try {
            kakaoId = mapper.readValue(response.getBody(), KakaoId.class);
        }catch(JsonMappingException e){
            e.printStackTrace();
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return kakaoId;
    }

    private OAuthToken JsonToObject(ResponseEntity<String>response){
        ObjectMapper mapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = mapper.readValue(response.getBody(), OAuthToken.class);
        }catch(JsonMappingException e){
            e.printStackTrace();
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return oAuthToken;
    }

    private ResponseEntity<String> getUserInfo(String access_token){
        //Post방식으로 key=value 데이터를 요청(카카오 쪽으로)
        RestTemplate rt = new RestTemplate();//android -> Retrofit2
        HttpHeaders headers = new HttpHeaders();
        //header
        headers.add("Authorization", "Bearer "+access_token);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //header body 연결
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        //요청과 응답을 동시에
        ResponseEntity<String> response = rt.exchange(
                LoginArgs.get_profile_uri,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        return response;
    }

}
