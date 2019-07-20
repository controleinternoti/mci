/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import model.ti.Usuario;
import javax.faces.context.FacesContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *
 * @author aj
 */
public class Seguranca {

    public static UsuarioSistema usuarioLogado() {
        UsuarioSistema usr = null;
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        if (auth != null && auth.getPrincipal() != null) {
            usr = (UsuarioSistema) auth.getPrincipal();
        }

        return usr;
    }
}
