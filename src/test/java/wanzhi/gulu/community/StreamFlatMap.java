package wanzhi.gulu.community;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 展开的map：用于处理多维数组，多重管道流
 */
public class StreamFlatMap {
    @Test
    public void flatMap01(){
        List<String> words = Arrays.asList("hello", "world");
        words.stream()
                .map(w -> Arrays.stream(w.split("")))//管道里面还有管道，即二维数组（集合）List<String,List<String>>
                .forEach(System.out::println);//打印了两个流
        //这种双重管道流不能用map处理
    }

    @Test
    public void flatMap02(){
        List<String> words = Arrays.asList("hello", "world");
        words.stream()
                .flatMap(w -> Arrays.stream(w.split("")))//管道里面还有管道，即二维数组（集合）List<String,String>
                .forEach(System.out::println);
        //这种双重管道流不能用map处理,要用flatMap来处理
    }
}
