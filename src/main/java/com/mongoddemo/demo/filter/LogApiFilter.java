package com.mongoddemo.demo.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//WebFilter is from Servlet not Spring ,so you have to add @ServletComponentScan on DemoApplication
@WebFilter(urlPatterns = "/*", filterName = "logApiFilter")
public class LogApiFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		//將請求送達 Controller。再執行到下一行，意味著回應已經從 Controller 離開
		filterChain.doFilter(request, response);

		int httpStatus = response.getStatus();
		String httpMethod = request.getMethod();
		String uri = request.getRequestURI();
		String params = request.getQueryString();

		if (params != null) {
			uri += "?" + params;
		}
		System.out.println(String.join(" ", String.valueOf(httpStatus), httpMethod, uri));
	}
}
