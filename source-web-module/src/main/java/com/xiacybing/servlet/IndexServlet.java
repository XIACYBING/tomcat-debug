package com.xiacybing.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 主页Servlet
 *
 * @author wang.yubin
 * @since 2023/1/31
 */
@WebServlet("/index")
public class IndexServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("正在访问：" + req.getRequestURI());

        // todo 如果项目启动后，第一次就直接访问/source-web-module/index，会被跳转到Tomcat的首页，只有在访问过/source-web-module/index
        //  .jsp后，再访问/source-web-module/index，才能正确的跳转到/source-web-module/index.jsp
        //  从catalina-home/work/Catalina目录下看，访问不到/source-web-module/index
        //  .jsp时，目录下的source-web-module文件夹为空，只有ROOT文件夹下有一个index_jsp类，和相应的index_jsp
        //  .java文件，在直接访问/source-web-module/index.jsp后，source-web-module文件夹下才出现相关的index_jsp类和index_jsp
        //  .java文件，怀疑和当初直接在org.apache.catalina.startup.ContextConfig.configureStart中增加context
        //  .addServletContainerInitializer(new JasperInitializer(), null)有关
        resp.sendRedirect("redirect:index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("正在访问：" + req.getRequestURI());
        System.out.println(new BufferedReader(new InputStreamReader(req.getInputStream())).readLine());

        // 设置相应类型，以及编码，编码不设置可能会乱码
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("欢迎通过Post请求访问");
    }

}
