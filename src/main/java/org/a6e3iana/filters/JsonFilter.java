package org.a6e3iana.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JsonFilter extends HttpFilter {
    public static final String CONTENT_TYPE = "application/json";

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(req, res);
        res.setContentType(CONTENT_TYPE);
        ObjectMapper mapper = new JsonMapper();
        Object modelDto = req.getAttribute(Attributes.DTO);
        if(modelDto != null){
            res.getWriter().println(mapper.writeValueAsString(modelDto));
        }
    }
}
