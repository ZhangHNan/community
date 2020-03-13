package wanzhi.gulu.community.util;

import org.springframework.stereotype.Component;
import wanzhi.gulu.community.dto.PageDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class PageUtils {

    public List<Integer> countButton(int currentPage, int totalPage) {
        List<Integer> showButton = new ArrayList<>();
        if (totalPage < 5) {
            for (int i = 1; i <= currentPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        if(currentPage <= 3){
            for (int i = 1 ; i <= 5; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        if (totalPage - currentPage == 0) {
            for (int i = currentPage - 4; i <= totalPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        if (totalPage - currentPage == 1) {
            for (int i = currentPage - 3; i <= totalPage; i++) {
                showButton.add(i);
            }
            return showButton;
        }
        for (int i = currentPage - 2; i <= currentPage+2; i++) {
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
