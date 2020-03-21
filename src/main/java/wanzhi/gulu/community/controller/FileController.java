package wanzhi.gulu.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wanzhi.gulu.community.dto.FileUploadDTO;

@Controller
public class FileController {

    @ResponseBody
    @PostMapping("/file/upload")
    public FileUploadDTO fileUpload(){
        FileUploadDTO fileUploadDTO = new FileUploadDTO();
        fileUploadDTO.setSuccess(1);
        //图片在服务器存储的地址
        fileUploadDTO.setUrl("/img/微信图片_20200320195504.jpg");
        fileUploadDTO.setMassage("上传成功！");
        return fileUploadDTO;
    }
}
