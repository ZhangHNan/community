package wanzhi.gulu.community.provider;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.dto.AccessTokenDTO;
import wanzhi.gulu.community.dto.GithubUser;

import java.io.IOException;

//用于模拟浏览器发送请求（使用okhttp）（调用github第三登录需要的API）
@Service
public class GithubProvider {

    //以post方式发送请求（调用access_token接口）并接收数据
    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8"); //设置JSON类型的MediaType
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        //JSON.toJSONString()：将对象转换为JSON数据（使用fastJson）
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")  //设置请求的url
                .post(body)  //设置请求方式为post
                .build(); //模式：xxx.Builder().xx().build()：创建xxx，中间的xx()设置某些参数
        try {
            Response response = client.newCall(request).execute(); //发送请求并接收Response（返回的JSON数据）
            String string = response.body().string();
//            System.out.println(string);
            //access_token=cdaf902b9a039fa8f751f6ba18a1e124c9f82d26&scope=user&token_type=bearer
            //获取token并返回
            String[] split = string.split("&");
            String tokenString = split[0];
            String token = tokenString.split("=")[1];
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;//出异常时返回null
    }

    //以get请求方式调用userAPI获取用户信息
    public GithubUser getGithubUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);//将字符串的json数据转为GithubUser类（JSON数据的解析）
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;//出异常时返回null
    }

}
