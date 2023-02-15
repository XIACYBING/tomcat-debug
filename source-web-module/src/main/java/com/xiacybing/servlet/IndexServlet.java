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
