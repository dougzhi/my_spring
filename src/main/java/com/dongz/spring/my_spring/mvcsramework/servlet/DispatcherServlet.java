package com.dongz.spring.my_spring.mvcsramework.servlet;

import com.dongz.spring.my_spring.mvcsramework.annotation.Autowired;
import com.dongz.spring.my_spring.mvcsramework.annotation.Controller;
import com.dongz.spring.my_spring.mvcsramework.annotation.RequestMapping;
import com.dongz.spring.my_spring.mvcsramework.annotation.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author dongzhi
 * @date 2019/8/30
 * @desc
 */
public class DispatcherServlet extends HttpServlet {

    private Properties properties = new Properties();

    private List<String> classNames = new ArrayList<String>();

    private Map<String, Object> ioc = new HashMap<String, Object>();

    private Map<String, Method> handlerMapping = new HashMap<String, Method>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //1,加载配置文件
        doLoadConifg(config.getInitParameter("contextConfigLocation"));

        //2,根据配置文件扫描所有的相关的类
        doScanner(properties.getProperty("scanPackage"));

        //3，初始化所有的相关的类的实例，并且将其放入到IOC容器中， 也就是map中
        doInstance();

        //4,实现自动依赖注入
        doAutoWired();

        //5,初始化 HandleMapping
        initHandlerMapping();

        System.out.println("my spring is running");
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry :
                ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();

            if (!clazz.isAnnotationPresent(Controller.class)) {
                continue;
            }

            String baseUrl = "";
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping annotation = clazz.getAnnotation(RequestMapping.class);
                baseUrl = annotation.value();
            }

            Method[] methods = clazz.getMethods();
            for (Method method :
                    methods) {
                if (!method.isAnnotationPresent(RequestMapping.class)) {
                    continue;
                }
                RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                String url = (baseUrl + annotation.value()).replaceAll("/+", "/");
                handlerMapping.put(url, method);
                System.out.println("method: "+ url);
            }
        }
    }

    private void doAutoWired() {
        if (ioc.isEmpty()) {
            return;
        }

        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            //获取所有的字段

            // 不管是private还是protect 都要强制注入
            Field[] fields = entry.getValue().getClass().getDeclaredFields();
            for (Field field :
                    fields) {
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }

                Autowired annotation = field.getAnnotation(Autowired.class);
                String beanName = annotation.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }

                field.setAccessible(true);
                try {
                    field.set(entry.getValue(),ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) {
            return;
        }

        //如果不为空，利用反射机制将刚刚扫描进来的所有className初始化
        try {
            for (String className : classNames) {
                Class<?> clazz = Class.forName(className);
                //进入Bean实例化阶段，初始化IOC容器
                //判断
                //1，ioc 默认类名小写
                //2, 如果用户自定义名字， 那么优先选择自定义名字
                //3, 接口时用接口类型做key
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String beanName = clazz.getSimpleName();
                    ioc.put(lowerFirstCaseName(beanName), clazz.newInstance());
                } else if (clazz.isAnnotationPresent(Service.class)) {
                    Service annotation = clazz.getAnnotation(Service.class);
                    String value = annotation.value();
                    //如果自定义了名字
                    if ("".equals(value.trim())) {
                        value = lowerFirstCaseName(clazz.getSimpleName());
                    }

                    ioc.put(value, clazz.newInstance());
                    //3，如果有接口
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> i :
                            interfaces) {
                        //将接口的类型作为key
                        ioc.put(i.getName(), clazz.newInstance());
                    }

                } else {
                    continue;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String lowerFirstCaseName(String beanName) {
        char[] chars = beanName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String packageName) {
        //进行递归扫描
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
        File classDir = new File(url.getFile());

        for (File file : classDir.listFiles()) {
            //文件夹
            if (file.isDirectory()) {
                doScanner(packageName + "." + file.getName());
            }
            //文件
            else {
                classNames.add(packageName + "." + file.getName().replace(".class", ""));

            }
        }
    }

    private void doLoadConifg(String location) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);

        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        String url = req.getRequestURI().replaceAll(contextPath, "").replaceAll("/+", "/");

        if (!handlerMapping.containsKey(url)) {
            resp.getWriter().write("404 page not found!!!");
            return;
        }
        Method method = handlerMapping.get(url);
        System.out.println(method);

    }
}
