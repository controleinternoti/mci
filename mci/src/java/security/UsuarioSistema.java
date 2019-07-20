/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package security;


import model.ti.Usuario;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author AJ Desenvolvimento
 */
public class UsuarioSistema extends User {
    
    private Usuario usuario;
    
    public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getCpf(),usuario.getSenha(), authorities);
        this.usuario= usuario;
    }

    public Usuario gUsuario() {
        return usuario;
    }
    
}
