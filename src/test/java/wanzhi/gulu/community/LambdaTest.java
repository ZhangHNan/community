package wanzhi.gulu.community;

import org.junit.Test;

/**
 * Java8新特性 lambda表达式 ：快速实现接口
 * 使用lambda表达式返回的是 实现这个接口并执行之后的返回值，即方法体中return的，方法体中没有return就没有返回值
 */
//不使用Springboot的注解虽然不能用spring ioc容器，但是运行更快
public class LambdaTest {

    interface Printer{
        void printer(String val);
    }

    public void pringSomething(String something,Printer printer){
        printer.printer(something);
    }

    @Test//未使用lambda表达式
    public void noLambda() {
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        Printer printer = new Printer() {
            @Override
            public void printer(String val) {
                System.out.println("printer...");
                System.out.println(val);
            }
        };
        lambdaTest.pringSomething(some,printer);
    }

    @Test//使用lambda表达式
    public void lambda01(){
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        //使用lambda表达式实现Printer接口的printer方法
        Printer printer = (String val) -> {
            System.out.println(val);
        };
        lambdaTest.pringSomething(some,printer);
    }

    @Test//简化：可以去除参数类型
    public void lambda02(){
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        //使用lambda表达式实现Printer接口的printer方法
        Printer printer = (val) -> {
            System.out.println(val);
        };
        lambdaTest.pringSomething(some,printer);
    }

    @Test//注意：参数可以随便写，根据上下文匹配
    public void lambda03(){
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        //使用lambda表达式实现Printer接口的printer方法
        Printer printer = (v) -> {
            System.out.println(v);
        };
        lambdaTest.pringSomething(some,printer);
    }

    @Test//简化：当只有一个参数的时候，参数的括号可以去掉
    public void lambda04(){
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        //使用lambda表达式实现Printer接口的printer方法
        Printer printer = val -> {
            System.out.println(val);
        };
        lambdaTest.pringSomething(some,printer);
    }

    @Test//简化：如果函数体只有一行的话，可以去除花括号
    public void lambda05(){
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        //使用lambda表达式实现Printer接口的printer方法
        Printer printer = val -> System.out.println(val);
        lambdaTest.pringSomething(some,printer);
    }

    @Test//简化：lambda表达式的参数类型和返回值根据接口的定义：接口参数类型和返回值自动地匹配。
    public void lambda06(){
        LambdaTest lambdaTest = new LambdaTest();
        String some = "lambda";
        //使用lambda表达式当接口的实参
        lambdaTest.pringSomething(some,val -> System.out.println(val));//一个参数，返回值是void

        //无参数的lambda表达式： () -> System.out.println("lambda");
    }
}
