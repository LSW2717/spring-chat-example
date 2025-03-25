package com.dcool.springchatexample.domain.auditing;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuditedAuditor implements AuditorAware<Audited> {

    @Override
    public Optional<Audited> getCurrentAuditor() {
        Audited entity = new Audited();
        try{
            entity.setUsername(getCurrentAuthentication().getName());
            entity.setTimestamp(System.currentTimeMillis());
            entity.setAddress(getCurrentHttpServletRequest().getRemoteAddr());
        }catch(Exception e){
//            e.printStackTrace();
        }
        return Optional.of(entity);
    }


    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public HttpServletRequest getCurrentHttpServletRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes request = (ServletRequestAttributes)attrs;
        return request.getRequest();
    }

}
