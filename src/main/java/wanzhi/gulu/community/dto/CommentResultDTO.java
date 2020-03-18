package wanzhi.gulu.community.dto;

import lombok.Data;

@Data
public class CommentResultDTO {
    private Integer code;
    private String message;

    public static CommentResultDTO errorOf(Integer code,String message){
        CommentResultDTO resultDTO = new CommentResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }
}
