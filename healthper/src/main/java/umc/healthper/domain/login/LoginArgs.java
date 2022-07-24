package umc.healthper.domain.login;

public interface LoginArgs{
    String redirect_uri = "http://localhost:8080/login";
    String api_key = "afa5159145bb3e8458eef4afa7c0a662";
    String grant_type = "authorization_code";
    String get_token_uri = "https://kauth.kakao.com/oauth/token";
    String get_profile_uri = "https://kapi.kakao.com/v2/user/me";
}
