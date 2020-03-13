package wanzhi.gulu.community.dto;

import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    private Integer currentPage; //当前页面
    private Integer rows;        //页面显示行数
    private Integer totalCount;  //总记录数
    private Integer totalPage;    //最后一页数(总页数)= totalCount%rows==0？totalCount%rows：totalCount%rows+1
    private Integer start;       //查询索引 =（currentPage-1）*rows
    private List<Integer> showButtons; //展示的5个按钮的值（根据当前页面和最后一页自定义逻辑）
    private List<QuestionDTO> questionDTOS; //展示的帖子内容
}
