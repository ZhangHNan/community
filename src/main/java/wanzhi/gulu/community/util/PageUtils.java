package wanzhi.gulu.community.util;

import org.springframework.stereotype.Component;

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
}
