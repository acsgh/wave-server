package com.acs.waveserver.provider.jetty;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.SessionHandler;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JettyHandler extends SessionHandler {

    private Servlet servlet;

    public JettyHandler(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    public void doHandle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        servlet.service(request, response);
        baseRequest.setHandled(true);
    }

}