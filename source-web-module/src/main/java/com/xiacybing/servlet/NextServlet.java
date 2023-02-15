package com.xiacybing.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第二个Servlet
 *
 * @author wang.yubin
 * @since 2023/2/6
 */
@WebServlet("/next")
public class NextServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("[" + Thread.currentThread().getName() +  "] 正在访问：" + req.getRequestURI());
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("<h3>欢迎通过Get请求访问NextServlet</h3>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("正在访问：" + req.getRequestURI());

        // 设置相应类型，以及编码，编码不设置可能会乱码
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().write("欢迎通过Post请求访问");
    }

}
