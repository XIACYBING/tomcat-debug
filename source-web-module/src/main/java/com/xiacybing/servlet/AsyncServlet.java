package com.xiacybing.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 异步Servlet
 *
 * @author wang.yubin
 * @since 2023/4/10
 */
@Slf4j
@WebServlet(urlPatterns = "/async", asyncSupported = true)
public class AsyncServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        log.info("正在访问：[{}]", req.getRequestURI());

        AsyncContext asyncContext = req.startAsync();

        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) {
                log.info("监听到异步线程完成，uri：[{}]", ((HttpServletRequest)event.getSuppliedRequest()).getRequestURI());
            }

            @Override
            public void onTimeout(AsyncEvent event) {
                log.info("监听到异步线程超时，uri：[{}]", ((HttpServletRequest)event.getSuppliedRequest()).getRequestURI());
            }

            @Override
            public void onError(AsyncEvent event) {
                log.info("监听到异步线程异常，uri：[{}]", ((HttpServletRequest)event.getSuppliedRequest()).getRequestURI());
            }

            @Override
            public void onStartAsync(AsyncEvent event) {
                log.info("监听到异步Servlet的启动，uri：[{}]", ((HttpServletRequest)event.getSuppliedRequest()).getRequestURI());
            }
        });

        // 通过start提交任务，本质上是使用Tomcat内部的Processor中的线程池进行任务处理，请求量较大的情况下这种用法可能会导致Tomcat处理请求能力的降低
        // 建议可以自己定义线程池，提交任务，并在任务执行完成后通过AsyncContext.complete()通知Tomcat当前请求的处理完成
        asyncContext.start(() -> {

            try {

                long sleepMillis = ThreadLocalRandom.current().nextLong(1000);

                log.info("开始休眠[{}]毫秒", sleepMillis);

                Thread.sleep(sleepMillis);

                log.info("休眠结束，异步请求即将完成");

                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write("<h3>欢迎通过Get请求访问AsyncServlet</h3>");

                // 触发异步Servlet完成逻辑：会提交任务给endpoint的线程池进行处理，最终会走到processor的逻辑
                asyncContext.complete();
            } catch (InterruptedException | IOException exception) {
                log.error("异步请求异常：", exception);
            }
        });
    }
}
