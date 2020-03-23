package wanzhi.gulu.community.provider;

import org.springframework.beans.factory.annotation.Value;

public class UcloudProvider {
    @Value("${page.index.rows}")
    private String indexRows;//设置首页每页展示数据行数
}
