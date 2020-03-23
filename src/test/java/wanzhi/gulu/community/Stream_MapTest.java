package wanzhi.gulu.community;

import org.junit.Test;
import wanzhi.gulu.community.model.Employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * StreamAPI-Map：转换 流 ，将数据从一种格式转换成另一种格式
 */
public class Stream_MapTest {

    @Test//将所有元素转换成大写
    public void mapExample01() {
        List<String> alpha = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");
        //不使用Stream管道流
        List<String> alphaUpper = new ArrayList<>();
        for (String s : alpha) {
            alphaUpper.add(s.toUpperCase());
        }
        System.out.println(alphaUpper); //[MONKEY, LION, GIRAFFE, LEMUR]
    }

    @Test//将所有元素转换成大写
    public void mapExample02() {
        List<String> alpha = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");
        //使用Stream管道流，将所有元素转换成大写
        List<String> alphaUpper = alpha.stream()
                .map(e -> e.toUpperCase())
                .collect(Collectors.toList());
        System.out.println(alphaUpper); //[MONKEY, LION, GIRAFFE, LEMUR]
    }

    //函数引用（方法引用）
    @Test//将所有元素转换成大写
    public void mapExample03() {
        List<String> alpha = Arrays.asList("Monkey", "Lion", "Giraffe", "Lemur");
        //使用Stream管道流，将所有元素转换成大写//或者使用方法引用同上面lambda表达式
        List<String> alphaUpper = alpha.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println(alphaUpper); //[MONKEY, LION, GIRAFFE, LEMUR]
    }

    //mapToInt//遍历输出
    @Test//取出每个字符串的长度
    public void mapExample04() {
        Stream.of("Monkey", "Lion", "Giraffe", "Lemur")
                .mapToInt(String::length)
                .forEach(System.out::println);
    }

    @Test//将每个员工的年龄增加一岁并且互换年龄
    public void mapExample05() {
        Employee e1 = new Employee(1,23,"M","Rick","Beethovan");
        Employee e2 = new Employee(2,13,"F","martina","Hengis");
        Employee e3 = new Employee(3,43,"M","Ricky","Martin");
        Employee e4 = new Employee(4,26,"M","Jon","Lowman");
        Employee e5 = new Employee(5,19,"F","Cristine","Maria");
        Employee e6 = new Employee(6,15,"M","David","Feezor");
        Employee e7 = new Employee(7,68,"F","Melissa","Roy");
        Employee e8 = new Employee(8,79,"M","Alex","Gussin");
        Employee e9 = new Employee(9,15,"F","Neetu","Singh");
        Employee e10 = new Employee(10,45,"M","Naveen","Jain");

        List<Employee> employees = Arrays.asList(e1,e2,e3,e4,e5,e6,e7,e8,e9,e10);

        List<Object> collect = employees.stream()
                .map(e -> {
                    e.setAge(e.getAge() + 1);
                    e.setGender(e.getGender().equals("M")?"F":"M");
                    return e;//对象改变了，要return对象才能改变
                })
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    //也可以使用peek,使用peek时参数是引用数据类型，所以不用return就可改变对象
    @Test//将每个员工的年龄增加一岁并且互换年龄
    public void mapExample06() {
        Employee e1 = new Employee(1,23,"M","Rick","Beethovan");
        Employee e2 = new Employee(2,13,"F","martina","Hengis");
        Employee e3 = new Employee(3,43,"M","Ricky","Martin");
        Employee e4 = new Employee(4,26,"M","Jon","Lowman");
        Employee e5 = new Employee(5,19,"F","Cristine","Maria");
        Employee e6 = new Employee(6,15,"M","David","Feezor");
        Employee e7 = new Employee(7,68,"F","Melissa","Roy");
        Employee e8 = new Employee(8,79,"M","Alex","Gussin");
        Employee e9 = new Employee(9,15,"F","Neetu","Singh");
        Employee e10 = new Employee(10,45,"M","Naveen","Jain");

        List<Employee> employees = Arrays.asList(e1,e2,e3,e4,e5,e6,e7,e8,e9,e10);

        List<Object> collect = employees.stream()
                .peek(e -> {
                    e.setAge(e.getAge() + 1);
                    e.setGender(e.getGender().equals("M")?"F":"M");
                    //不用return对象才能改变
                })
                .collect(Collectors.toList());
        System.out.println(collect);
    }


}
