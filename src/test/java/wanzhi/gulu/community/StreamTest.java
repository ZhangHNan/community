package wanzhi.gulu.community;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Java8新特性 Stream API ：将数组、集合、文件的io流可以转换为流的操作源，然后可以使用Stream API 作过滤、转换等操作。
 */
public class StreamTest {

    @Test//使用Stream API实例
    public void streamExample () {
        //定义一个List集合
        List<String> nameStrs= Arrays.asList("Monkey","Lion","Giraffe","Lemur");

        List<String> list = nameStrs.stream()
                .filter(s -> s.startsWith("L"))//过滤：留下以“L”开头的元素
                .map(String::toUpperCase)      //遍历：将所有元素变为大写
                .sorted()                      //排序：默认按照字母排序
                .collect(Collectors.toList()); //收集：将流转换为List集合

        System.out.println(list);
    }

    @Test//使用Stream
    public void streamTest01() {
        //定义一个List集合
        List<String> nameStrs= Arrays.asList("Monkey","Lion","Giraffe","Lemur","KiMi","MoBout");

        List<String> list = nameStrs.stream()
                .filter(s -> s.startsWith("M"))//过滤：留下以“M”开头的元素
                .map(String::toUpperCase)      //遍历：将所有元素变为大写
                .sorted()                      //排序：默认按照字母排序
                .collect(Collectors.toList()); //收集：将流转换为List集合

        System.out.println(list);
    }

    @Test//使用Stream：如果是数组
    public void streamTest02() {
        //定义一个数组
        String[] nameStrs= {"Monkey","Lion","Giraffe","Lemur","KiMi","MoBout"};
        //将数组转换成流
        //Stream.of(nameStrs);
        //如果是数组就这样写：
        List<String> list = Stream.of(nameStrs)
                .filter(s -> s.startsWith("M"))//过滤：留下以“M”开头的元素
                .map(String::toUpperCase)      //遍历：将所有元素变为大写
                .sorted()                      //排序：默认按照字母排序
                .collect(Collectors.toList()); //收集：将流转换为List集合

        System.out.println(list);
    }

    @Test//使用Stream：如果是数组
    public void streamTest03() {
        //定义一个Set集合
        List<String> nameStrs= Arrays.asList("Monkey","Lion","Giraffe","Lemur","KiMi","MoBout");
        Set<String> set = new HashSet<>(nameStrs);
        //如果是Set集合,最后并转为Set就这样写：
        Set<String> list = set.stream()
                .filter(s -> s.startsWith("M"))//过滤：留下以“M”开头的元素
                .map(String::toUpperCase)      //遍历：将所有元素变为大写
                .sorted()                      //排序：默认按照字母排序
                .collect(Collectors.toSet()); //收集：将流转换为List集合

        System.out.println(list);
    }

    @Test//使用Stream：如果是文件
    public void streamTest04() {
        try {
            //注意:如果是文件的话，元素以换行作分割。
            Stream<String> lines = Files.lines(Paths.get("src","test","java","wanzhi","gulu","community","file.txt"));
            //如果是文件就这样写：
            List<String> list = lines
                    .filter(s -> s.startsWith("A"))//过滤：留下以“M”开头的元素
                    .map(String::toUpperCase)      //遍历：将所有元素变为大写
                    .sorted()                      //排序：默认按照字母排序
                    .collect(Collectors.toList()); //收集：将流转换为List集合

            System.out.println(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
