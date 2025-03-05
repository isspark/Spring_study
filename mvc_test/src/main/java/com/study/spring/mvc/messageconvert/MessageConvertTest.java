package com.study.spring.mvc.messageconvert;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MessageConvertTest {

    public static void main(String[] args) throws IOException, HttpMediaTypeNotAcceptableException, NoSuchMethodException {

//        test1();

//        test2();

//        test3();
        test4();
    }

    /**
     *     - 首先看RequestMapping 上有没有指定
     *     - 然后看 request 的Accept头有没有指定
     *     - 最后根据MessageConvert 的顺序，谁先转换谁优先级更高。
     * @throws NoSuchMethodException
     * @throws HttpMediaTypeNotAcceptableException
     * @throws IOException
     */
    public static void test4() throws NoSuchMethodException, HttpMediaTypeNotAcceptableException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ServletWebRequest webRequest = new ServletWebRequest(request,response);

        request.addHeader("Accept","application/xml");
        request.setContentType("application/json");

        RequestResponseBodyMethodProcessor processor = new RequestResponseBodyMethodProcessor(List.of(
                new MappingJackson2HttpMessageConverter(),new MappingJackson2XmlHttpMessageConverter()
        ));
        processor.handleReturnValue(
                new User("xstar",10),
                new MethodParameter(MessageConvertTest.class.getMethod("user"),-1),
                new ModelAndViewContainer(),
                webRequest
        );
        System.out.println(new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
    }

    /**
     * 将消息转换成Java对象
     * @throws IOException
     */
    public static void test3() throws IOException {
        MockHttpInputMessage message = new MockHttpInputMessage("""
                {
                    "name":"xstar",
                    "age": 20
                }
                """.getBytes(StandardCharsets.UTF_8));
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if(converter.canRead(User.class, MediaType.APPLICATION_JSON)){
            Object read = converter.read(User.class,message);
            System.out.println(read);
        }
    }

    public User user(){
        return null;
    }

    /**
     * 将Java对象转换成XML消息
     * @throws IOException
     */

    public static void test2() throws IOException {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter();
        if(converter.canWrite(User.class, MediaType.APPLICATION_XML)){
            converter.write(new User("xstar",20),MediaType.APPLICATION_XML,message);
            System.out.println(message.getBodyAsString());
        }
    }
    /**
     * 将Java对象转换成Json消息
     * @throws IOException
     */

    public static void test1() throws IOException {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if(converter.canWrite(User.class, MediaType.APPLICATION_JSON)){
            converter.write(new User("xstar",20),MediaType.APPLICATION_JSON,message);
            System.out.println(message.getBodyAsString());
        }
    }

    private static  class User{
        private String name;

        private int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public User() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
