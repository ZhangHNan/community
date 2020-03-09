package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wanzhi.gulu.community.dto.AccessTokenDTO;
import wanzhi.gulu.community.dto.GithubUser;
import wanzhi.gulu.community.provider.GithubProvider;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    //跳转到github授权页面授权后，github访问callback（redirect_uri）同时携带code和state参数
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam(name="state") String state){//接收code和state参数
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("c14b8a6a1a6b4e02b9a1");
        accessTokenDTO.setClient_secret("713fcb0df819c99df72926787dead1ae90cc85e4");
        //以post方式，请求https://github.com/login/oauth/access_token并携带某些必要参数（accessTokenDTO）
        //请求access_token接口后会返回access_token和scope=user和token_type，只需要access_token即可
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //将获取的access_token作为参数请求http://api.github.com/user会返回用户的信息，将需要使用的信息抽取为GithubUser用于接收
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getLogin());
        return "index";
    }
}
