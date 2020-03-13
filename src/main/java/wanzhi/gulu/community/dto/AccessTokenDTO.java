package wanzhi.gulu.community.dto;

import lombok.Data;

/**
 * 抽取调用access_tokenAPI需要携带的参数
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
