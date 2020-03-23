package wanzhi.gulu.community.provider;

import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wanzhi.gulu.community.exception.CustomizeErrorCode;
import wanzhi.gulu.community.exception.CustomizeException;

import java.io.InputStream;
import java.util.UUID;

//UCloud图片上传相关业务
@Service
public class UcloudProvider {
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;//设置Ucloud公匙

    @Value("${ucloud.ufile.private-key}")
    private String privateKey;//设置Ucloud私匙

    @Value("${ucloud.ufile.bucketName}")
    private String bucketName;

    @Value("${ucloud.ufile.region}")
    private String region;

    @Value("${ucloud.ufile.suffix}")
    private String suffix;

    @Value("${ucloud.ufile.expiresDuration}")
    private Integer expiresDuration;

    //将图片上传到UCloud存储，并通过response生成访问地址并返回给前端
    public String upload(InputStream inputStream,String mimeType,String fileName){
        String generateFileName;
        String[] fileSplit = fileName.split("\\.");//注意这里要用\\转义
        if (fileSplit.length>1){
            generateFileName = UUID.randomUUID().toString() + "." + fileSplit[fileSplit.length - 1];//UUID.后缀
        }else {
            return null;
        }
        try {
            //创建对象授权器
            ObjectAuthorization OBJECT_AUTHORIZER = new UfileObjectLocalAuthorization(
                    publicKey, privateKey);
            // 对象操作需要ObjectConfig来配置您的地区和域名后缀
            ObjectConfig config = new ObjectConfig(region, suffix);
            PutObjectResultBean response = UfileClient.object(OBJECT_AUTHORIZER, config)
                    .putObject(inputStream, mimeType)
                    .nameAs(generateFileName)
                    .toBucket(bucketName)
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
            if(response !=null && response.getRetCode() ==0){
                //获取访问图片的url
                String url = UfileClient.object(OBJECT_AUTHORIZER,config)
                        .getDownloadUrlFromPrivateBucket(generateFileName,bucketName,expiresDuration)
                        .createUrl();
                return url;
            }else{
                throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException e) {
            e.printStackTrace();
            return null;
        } catch (UfileServerException e) {
            e.printStackTrace();
        }
        return generateFileName;
    }


}
