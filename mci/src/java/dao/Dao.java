package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import model.ti.Usuario;

public class Dao implements Serializable {

    private EntityManagerFactory emf;
    private EntityManager em;
    private String user;
    private Integer idUser;

    public Dao() {
        emf = Persistence.createEntityManagerFactory("testePU");
        em = emf.createEntityManager();
    }

    public void gravar(Object objeto) {
        em.getTransaction().begin();
        em.persist(objeto);
        em.getTransaction().commit();
    }

    public void alterar(Object objeto) {
        em.getTransaction().begin();
        em.merge(objeto);
        em.getTransaction().commit();
    }

    public void remover(Object objeto) {
        em.getTransaction().begin();
        em.remove(objeto);
        em.getTransaction().commit();
    }

    public void excluir(Object objeto) {
        em.getTransaction().begin();
        em.remove(em.contains(objeto) ? objeto : em.merge(objeto));
        em.getTransaction().commit();
    }

    public void flush() {
        em.flush();
    }

    public Object buscar(Object objeto, int id) {
        return em.find(objeto.getClass(), id);
    }

    public List<?> buscarTodos(Class classe) {
        return em.createQuery("From " + classe.getName()).getResultList();
    }
//----------------PORTAL FORNECEDOR-----------------------

    //----------------Login----------------
    public Usuario buscarUsuarioSpring(String usuario) {
        Usuario user = null;
        try {
            user = (Usuario) em.createNativeQuery("Select * from USINAS.WEB_PROP_USU where CD_USU_BD = '" + usuario + "'", Usuario.class).getSingleResult();
            System.out.println("===============" + user.getUsuario());
            System.out.println("===============" + user.getIdUsuario());
            usuarioLogado(user.getUsuario());
            setUser(user.getUsuario());
            setIdUser(user.getIdUsuario());
        } catch (Exception e) {
        }

        return user;

    }

    public Usuario usuarioLogado(String nome) {
        return (Usuario) em.createNativeQuery("Select * from USINAS.WEB_PROP_USU where CD_USU_BD = '" + nome + "'", Usuario.class).getSingleResult();
    }
//    public List<Usuario> usuarioLogado2() {
//        return (List<Usuario>) em.createNativeQuery("SELECT * FROM TI.USUARIO  where APELIDO = '" + getUser() + "'", Usuario.class).getResultList();
//    }

    public List<Object[]> usuarioLogado2() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select WEBPROPUSU_ID,CD_USU_BD FROM USINAS.WEB_PROP_USU where CD_USU_BD = '" + getUser() + "'");
        List<Object[]> results = query.getResultList();
        return results;
    }
    //----------------DASHBOARD----------------

    public List<Object[]> cota() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT TO_CHAR(M.DT,'MONTH'), M.VL_APURADO, M.VL_APURADO_FRENTE, M.VL_META FROM mov_ppf_temp M, WEB_PROP_USU U   WHERE M.WEBPROPUSU_ID = U.WEBPROPUSU_ID AND M.INDICADOR_CD = 1 AND M.WEBPROPUSU_ID ="+getIdUser());
        List<Object[]> results = query.getResultList();
        return results;
    }
    
    public List<Object[]> mineral() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT TO_CHAR(M.DT,'MONTH'), M.VL_APURADO, M.VL_APURADO_FRENTE, M.VL_META FROM mov_ppf_temp M, WEB_PROP_USU U   WHERE M.WEBPROPUSU_ID = U.WEBPROPUSU_ID AND M.INDICADOR_CD = 2 AND M.WEBPROPUSU_ID = "+getIdUser());
        List<Object[]> results = query.getResultList();
        return results;
    }
    

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    

    
    
}
