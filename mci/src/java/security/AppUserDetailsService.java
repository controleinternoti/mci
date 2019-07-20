/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;

import dao.Dao;
import model.ti.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.context.FacesContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author AJ Desenvolvimento
 */
public class AppUserDetailsService implements UserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String usuario) throws UsernameNotFoundException {
       Dao dao = (Dao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dao");
        Usuario usr = dao.buscarUsuarioSpring(usuario);
        UsuarioSistema usuarioSistema = null;
        if (usr != null) {
            usuarioSistema = new UsuarioSistema(usr, getGrupos("VISITANTE"));
        }
        return usuarioSistema;
    }
    
    private Collection<? extends GrantedAuthority> getGrupos(String usuario) {
        List<SimpleGrantedAuthority> grupos = new ArrayList<>();
        grupos.add(new SimpleGrantedAuthority(usuario));
        return grupos;
        
    }
    
}
