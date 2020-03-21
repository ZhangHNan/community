package wanzhi.gulu.community.dto;

import lombok.Data;

import java.util.List;

//标签库
@Data
public class TagDTO {
    private String categoryName;  //分类名称
    private List<String> tags;    //该分类的标签集合
}
