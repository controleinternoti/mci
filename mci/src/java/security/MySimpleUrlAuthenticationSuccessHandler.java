/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import java.io.IOException;
import java.util.Collection;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 *
 * @author Eder
 */
public class MySimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private String mac, macap,t;
    protected Log logger = LogFactory.getLog(this.getClass());
 
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
 
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication)
      throws IOException {
        request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println("===ap--" + request.getParameter("ap"));
        System.out.println("===id--" + request.getParameter("id"));
        System.out.println("===t--" + request.getParameter("t"));
        
        handle(request, response, authentication);
        clearAuthenticationAttributes(request);
        //pegandoParametros();
    }
 
    protected void handle(HttpServletRequest request, 
      HttpServletResponse response, Authentication authentication)
      throws IOException {
        String targetUrl = determineTargetUrl(authentication);
 
        if (response.isCommitted()) {
            logger.debug(
              "A resposta já foi confirmada. Não é possível redirecionar para"
              + targetUrl);
            return;
        }
        
        redirectStrategy.sendRedirect(request, response, targetUrl);
    }
      public void pegandoParametros() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //setStart(Integer.valueOf(t));
         
        System.out.println("===ap--" + request.getParameter("ap"));
        System.out.println("===id--" + request.getParameter("id"));
        System.out.println("===t--" + request.getParameter("t"));
      }
      
 
    protected String determineTargetUrl(Authentication authentication) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        //setStart(Integer.valueOf(t));
        
        boolean isFor = false;
        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities
         = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals("VISITANTE")) {
                isFor = true;
                break;
            } else if (grantedAuthority.getAuthority().equals("ADMINISTRADOR")) {
                isAdmin = true;
                break;
            }
        }
 
        if (isFor) {
            return "/sistema/termoAceite.xhtml?ap="+getMacap()+"&id="+getMac()+"&t="+getT();
        } else if (isAdmin) {
            return "/sistema/dashboard.xhtml";
        } else {
            throw new IllegalStateException();
        }
    }
 
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
 
    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
    protected RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMacap() {
        return macap;
    }

    public void setMacap(String macap) {
        this.macap = macap;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public Log getLogger() {
        return logger;
    }

    public void setLogger(Log logger) {
        this.logger = logger;
    }
    
}
