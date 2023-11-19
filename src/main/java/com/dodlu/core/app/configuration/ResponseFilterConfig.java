package com.dodlu.core.app.configuration;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Globals;

@RequiredArgsConstructor
@WebFilter()
public class ResponseFilterConfig implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    request.setAttribute(Globals.ASYNC_SUPPORTED_ATTR, true);
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig filterConfig) {}

  @Override
  public void destroy() {}
}
