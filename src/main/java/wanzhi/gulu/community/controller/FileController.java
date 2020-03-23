package wanzhi.gulu.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import wanzhi.gulu.community.dto.FileUploadDTO;
import wanzhi.gulu.community.provider.UcloudProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    UcloudProvider ucloudProvider;

    //图片从前端上传到服务器
    @ResponseBody
    @PostMapping("/file/upload")
    public FileUploadDTO fileUpload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest =(MultipartHttpServletRequest) request;
        //使用multipartRequest接收图片
        MultipartFile file = multipartRequest.getFile("editormd-image-file");
        try {
            String url = ucloudProvider.upload(file.getInputStream(), file.getContentType(), file.getOriginalFilename());
            FileUploadDTO fileUploadDTO = new FileUploadDTO();
            fileUploadDTO.setSuccess(1);
            //图片在服务器存储的地址
            fileUploadDTO.setUrl(url);
            fileUploadDTO.setMassage("上传成功！");
            return fileUploadDTO;
            //当前端显示图片地址的时候，图片就已经上传到UCloud了
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
