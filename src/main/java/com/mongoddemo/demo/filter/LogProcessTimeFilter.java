package com.mongoddemo.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//WebFilter is from Servlet not Spring ,so you have to add @ServletComponentScan on DemoApplication
@WebFilter(urlPatterns = "/*", filterName = "logProcessFilter")
public class LogProcessTimeFilter extends OncePerRequestFilter {

	//FilterChain 會將現有的 Filter 給串連起來，當請求進入後端，需要依序經過它們才會到達 Controller。
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		long startTime = System.currentTimeMillis();
		//將請求送達 Controller。再執行到下一行，意味著回應已經從 Controller 離開
		filterChain.doFilter(request, response);
		long processTime = System.currentTimeMillis() - startTime;

		System.out.println(processTime + " ms");
	}
}
