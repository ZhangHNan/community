package wanzhi.gulu.community.util;

import org.springframework.stereotype.Component;
import wanzhi.gulu.community.dto.PageDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class PageUtils {

    public List<Integer> countButton(int currentPage, int totalPage,int buttonCount) {
        List<Integer> showButton = new ArrayList<>();
        //实现按钮小于想要展示的按钮数（buttonCount）
        if (totalPage <= buttonCount) {
            for (int i = 1; i <= totalPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        //当前按钮在前排
        if(currentPage <= buttonCount/2+1){
            for (int i = 1 ; i <= buttonCount; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        //当前按钮在后排
        if (totalPage - currentPage < buttonCount/2+1) {
            for (int i = totalPage - buttonCount + 1; i <= totalPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        //当前按钮在中间
        for (int i = currentPage -buttonCount/2; i <= currentPage+buttonCount/2; i++) {
            showButton.add(i);
        }
        return showButton;
    }

    public int countTotalPage(int totalCount, int rows) {
        return (totalCount % rows == 0) ? (totalCount / rows) : (totalCount / rows + 1);
    }

    public int countStart(Integer currentPage, Integer rows) {
        return (currentPage-1)*rows;
    }

    public PageDTO judgeShow(PageDTO pageDTO) {
        pageDTO.setShowFirst(true);
        pageDTO.setShowEnd(true);
        pageDTO.setShowLast(true);
        pageDTO.setShowNext(true);
        if(pageDTO.getCurrentPage()==1){
            pageDTO.setShowLast(false);
        }
        if (pageDTO.getCurrentPage().equals(pageDTO.getTotalPage())){
            pageDTO.setShowNext(false);
        }
        List<Integer> showButtons = pageDTO.getShowButtons();
        for(int i: showButtons){
            if (i==1){
                pageDTO.setShowFirst(false);
                break;
            }
        }
        for(int i: showButtons){
            if(i==pageDTO.getTotalPage()){
                pageDTO.setShowEnd(false);
                break;
            }
        }
        return pageDTO;
    }
}
