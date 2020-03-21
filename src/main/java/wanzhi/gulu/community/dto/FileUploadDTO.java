package wanzhi.gulu.community.dto;

import lombok.Data;

//文件上传返回的信息
@Data
public class FileUploadDTO {
    private int success;
    private String massage;
    private String url;
}
