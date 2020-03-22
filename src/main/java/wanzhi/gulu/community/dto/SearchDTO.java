package wanzhi.gulu.community.dto;

import lombok.Data;

//搜索查数据需要的参数
@Data
public class SearchDTO {
    private String regxpSearch;
    private Integer start;
    private Integer rows;
}
