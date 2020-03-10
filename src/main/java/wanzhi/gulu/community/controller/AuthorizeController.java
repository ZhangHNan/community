package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${git.client.id}")
    private String clientId;
    @Value("${git.client.secret}")
    private String clientSecret;
    @Value("${git.redirect.uri}")
    private String redirectUri;

    //跳转到github授权页面授权后，github访问/callback（redirect_uri）同时携带code和state参数
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam(name="state") String state){//接收code和state参数
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        //以post方式，请求https://github.com/login/oauth/access_token并携带某些必要参数（accessTokenDTO）
        //请求access_token接口后会返回access_token和scope=user和token_type，只需要access_token参数即可
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //将获取的access_token作为参数请求http://api.github.com/user会返回用户的信息，将需要使用的信息抽取为GithubUser用于接收
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getLogin());
        return "index";//最终跳转到主页
    }
}
