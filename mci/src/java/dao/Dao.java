package dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.load;
import model.ti.Usuario;

public class Dao implements Serializable {

    private EntityManagerFactory emf;
    private EntityManager em;
    private String user;

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

//    public void teste()  {
//        Query query = em.createNativeQuery("DECLARE\n"
//                + "BEGIN\n"
//                + "  USINAS.pb_agricola.PB_DIF_DT_HR (?,?,?);\n"
//                + "END;");
//        query.setParameter(1, "01/07/2019");
//        query.setParameter(2, "10/07/2019");
//        String teste = "";
//        query.setParameter(3, teste);
//        //query.executeUpdate();
//        List<Object[]> result = query.getResultList();
//
//
//    }
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
//----------------TI-----------------------

    //----------------Login----------------
    public Usuario buscarUsuarioSpring(String usuario) {
        Usuario user = null;
        try {
            user = (Usuario) em.createNativeQuery("Select * from ti.usuario_unifi where cpf = '" + usuario + "'", Usuario.class).getSingleResult();
            System.out.println("===============" + user.getCpf());
            System.out.println("===============" + user.getPerfil());
            usuarioLogado(user.getCpf());
            setUser(user.getNome());
        } catch (Exception e) {
        }

        return user;

    }

    public Usuario usuarioLogado(String nome) {
        return (Usuario) em.createNativeQuery("Select *  from ti.usuario_unifi where cpf = '" + nome + "'", Usuario.class).getSingleResult();
    }
//    public List<Usuario> usuarioLogado2() {
//        return (List<Usuario>) em.createNativeQuery("SELECT * FROM TI.USUARIO  where APELIDO = '" + getUser() + "'", Usuario.class).getResultList();
//    }

    public List<Object[]> usuarioLogado2() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select NOME, ID_USUARIO, PERFIL  from ti.usuario_unifi where cpf  = '" + getUser() + "'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> procedimento() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("pb_agricola.PB_DIF_DT_HR ('01/07/2019', '10/07/2019')");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Computador----------------
//    public Computador buscarFuncionario(String nome) {
//        return (Computador) em.createNativeQuery("Select * from usinas.v_colab where nome_colab like '%" + nome + "%'", Computador.class).getResultList();
//    }
    //----------------converter-----------------
//    public Computador buscarFuncionarioConverter(String nome) {
//        return (Computador) em.createNativeQuery("Select * from usinas.v_colab where nome_colab like '%" + nome + "%'", Computador.class).getResultList();
//    }
    //----------------Colaborador----------------
    public List<Object[]> buscarColab() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select nome_colab, cd_colab from v_colab where dt_demis is null");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarColaboradores(BigDecimal mat) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select colab_id, cd_colab, nome_colab from USINAS.v_colab where dt_demis is null and cd_colab = " + mat);
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------EMAIL----------------
//    public List<Email> buscarEmailFuncionario() {
//        return (List<Email>) em.createNativeQuery("Select * from ti.email where tipo = 'FUNCIONARIO' ORDER BY NOME_COLAB", Email.class).getResultList();
//    }
    public List<Object[]> buscarEmailFuncionario() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT B.CD_COLAB\n"
                + ",A.NOME_COLAB\n"
                + ",A.EMAIL\n"
                + ",A.SENHA\n"
                + ",A.STATUS\n"
                + ",B.DT_DEMIS\n"
                + ",A.ID_EMAIL\n"
                + ",A.COLAB_ID\n"
                + ",A.tipo\n"
                + "FROM TI.EMAIL A\n"
                + ", V_COLAB B\n"
                + "WHERE A.TIPO = 'FUNCIONARIO'\n"
                + "AND A.COLAB_ID = B.COLAB_ID\n"
                + "ORDER BY NOME_COLAB");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarEmailFuncionarioDemitido() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT B.CD_COLAB ,\n"
                + "  A.NOME_COLAB ,\n"
                + "  A.EMAIL ,\n"
                + "  A.SENHA ,\n"
                + "  A.STATUS ,\n"
                + "  B.DT_DEMIS ,\n"
                + "  A.ID_EMAIL ,\n"
                + "  A.COLAB_ID ,\n"
                + "  A.TIPO\n"
                + "FROM TI.EMAIL A ,\n"
                + "  V_COLAB B\n"
                + "WHERE A.TIPO   = 'FUNCIONARIO'\n"
                + "AND A.COLAB_ID = B.COLAB_ID\n"
                + "AND B.DT_DEMIS is not null\n"
                + "ORDER BY NOME_COLAB");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarColaboradoresComDataDemisao(BigDecimal mat, BigDecimal emp) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select colab_id, cd_colab, nome_colab from USINAS.v_colab where cd_colab = " + mat + "and cd_empr = " + emp);
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Fornecedor----------------
    public List<Object[]> buscarFornecedor(String nome) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT NOME, NOME_FANT, CORR_ID FROM CORR WHERE NOME like '%" + nome + "%'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Marca----------------
    
    //----------------Nota Fiscal----------------
    public List<Object[]> buscarNF() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.NRO,a.SERIE,a.DT_EMISS,a.RAZAO_SOCIAL,a.NRO_NFE,b.CD_PROD,b.DESCR_PROD FROM NF_ENT a, ITNF_ENT b WHERE a.NFENT_ID = b.NFENT_ID AND b.CD_PROD  in ('54578','54579','54160','54164') order by a.DT_EMISS");
        //TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.NRO,a.SERIE,a.DT_EMISS,a.RAZAO_SOCIAL,a.NRO_NFE,b.CD_PROD,b.DESCR_PROD FROM NF_ENT a, ITNF_ENT b WHERE a.NFENT_ID = b.NFENT_ID AND b.CD_PROD  in ('54578','54579','54160','54164') and a.nro = 575842 and a.DT_EMISS = '27/04/2012' order by a.DT_EMISS");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarNfPorCodigo(int nf, String data) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.NRO,a.SERIE,a.DT_EMISS,a.RAZAO_SOCIAL,a.NRO_NFE,b.CD_PROD,b.DESCR_PROD FROM NF_ENT a, ITNF_ENT b WHERE a.NFENT_ID = b.NFENT_ID AND b.CD_PROD  in ('54578','54579','54160','54164') and a.nro = " + nf + " and a.DT_EMISS  = '" + data + "' order by a.DT_EMISS");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------ip----------------
    public List<Object[]> buscarColaboradoresId(BigDecimal id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select colab_id, cd_colab, nome_colab from v_colab where dt_demis is null and colab_id = " + id);
        List<Object[]> results = query.getResultList();
        return results;
        //public List<Object[]> buscarColaboradores(BigDecimal mat) usa esse metodo tbm
    }

    //----------------ComputadorSoftwareMB----------------


    public List<Object[]> buscarCompuatdorRelacionado(String micro) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM V_COMPUTADOR_SOFTWARE WHERE COMPUTADOR = '" + micro + "'");
        List<Object[]> results = query.getResultList();
        return results;
    }
//    public List<ComputadorSoftware> buscarSoftRelacionadoMicro(String key) {
//        return (List<ComputadorSoftware>) em.createNativeQuery("SELECT CS.ID_COMPUTADOR_SOFTWARE,\n"
//                + "  CS.ID_COMPUTADOR,\n"
//                + "  CS.ID_SOFTWARE,\n"
//                + "(SELECT COUNT(*) FROM TI_SOFTWARE SO ,\n"
//                + "  TI_COMPUTADOR CO ,\n"
//                + "  TI_COMPUTADOR_SOFTWARE CS,\n"
//                + "  SERIAL_OPEN_LICENSE SL\n"
//                + "WHERE (SO.KEY = ''  OR SL.SERIAL       = 'WERWE23423WER23RWE34R3')\n"
//                + "and SL.ID_SERIAL     = SO.ID_SERIAL\n"
//                + "AND SO.ID_SOFTWARE   = CS.ID_SOFTWARE\n"
//                + "AND CS.ID_COMPUTADOR = CO.ID_COMPUTADOR\n"
//                + ") CONTADOR \n"
//                + "FROM TI_SOFTWARE SO ,\n"
//                + "  TI_COMPUTADOR CO ,\n"
//                + "  TI_COMPUTADOR_SOFTWARE CS,\n"
//                + "  SERIAL_OPEN_LICENSE SL\n"
//                + "WHERE (SO.KEY        = '" + key + "'  OR SL.SERIAL       = '" + key + "')"
//                + "\n and SL.ID_SERIAL(+)     = SO.ID_SERIAL\n"
//                + "AND SO.ID_SOFTWARE   = CS.ID_SOFTWARE\n"
//                + "AND CS.ID_COMPUTADOR = CO.ID_COMPUTADOR\n", ComputadorSoftware.class).getResultList();
//    }

    public List<Object[]> buscarSoftOpenRelacionadoMicro(String serial) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID_COMPUTADOR_SOFTWARE,\n"
                + "  SOFTWARE,\n"
                + "  COMPUTADOR,\n"
                + "  QTDE,\n"
                + "  B.DESCRICAO,\n"
                + "  (SELECT COUNT(*)\n"
                + "  FROM V_COMPUTADOR_SOFTWARE A,\n"
                + "    TI_TIPO_SOFTWARE B\n"
                + "  WHERE SERIAL = '" + serial + "'\n"
                + "  AND A.ID_TIPO_SOFTWARE = B.ID_TIPO_SOFTWARE\n"
                + "  )CONTADOR,\n"
                + "  A.SERIAL\n"
                + "FROM V_COMPUTADOR_SOFTWARE A,\n"
                + "    TI_TIPO_SOFTWARE B\n"
                + "  WHERE SERIAL = '" + serial + "'\n"
                + "AND A.ID_TIPO_SOFTWARE = B.ID_TIPO_SOFTWARE");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarContratoSoftRelacionadoMicro(String key) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT 0 CONTRATO\n"
                + "        , A.KEY\n"
                + "FROM TI_SOFTWARE A,\n"
                + "TI_COMPUTADOR_SOFTWARE B\n"
                + "WHERE A.KEY = '" + key + "'\n"
                + "AND A.ID_SOFTWARE = B.ID_SOFTWARE\n"
                + "UNION\n"
                + "SELECT OL.CONTRATO,\n"
                + "      SL.SERIAL\n"
                + "FROM TI_SOFTWARE SO ,\n"
                + "  TI_COMPUTADOR CO ,\n"
                + "  TI_COMPUTADOR_SOFTWARE CS,\n"
                + "  TI_SERIAL_OPEN_LICENSE SL,\n"
                + "  TI_TIPO_LICENSE TL,\n"
                + "  TI_OPEN_LICENSE OL\n"
                + "WHERE  SL.SERIAL   =   '" + key + "'\n"
                + "AND OL.ID_OPEN_LICENSE = TL.ID_OPEN_LICENSE\n"
                + "AND TL.ID_TIPO_LICENSE = SL.ID_TIPO_LICENSE\n"
                + "AND SL.ID_SERIAL   = SO.ID_SERIAL\n"
                + "AND SO.ID_SOFTWARE   = CS.ID_SOFTWARE\n"
                + "AND CS.ID_COMPUTADOR = CO.ID_COMPUTADOR");
        List<Object[]> results = query.getResultList();
        return results;
    }


    public List<Object[]> buscarMicroRelacionadoSoftwareOpen(String micro) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID_COMPUTADOR_SOFTWARE,\n"
                + "COMPUTADOR,\n"
                + "A.SOFTWARE, \n"
                + "A.QTDE,\n"
                + "B.DESCRICAO,\n"
                + "A.SERIAL,\n"
                + "A.CONTRATO\n"
                + "FROM V_COMPUTADOR_SOFTWARE A,\n"
                + "TI_TIPO_SOFTWARE B\n"
                + "WHERE A.ID_TIPO_SOFTWARE = B.ID_TIPO_SOFTWARE\n"
                + "AND A.COMPUTADOR = '" + micro + "'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Dashboard----------------
    public List<Object[]> totComputador() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID_COMPUTADOR FROM TI_COMPUTADOR WHERE TIPO = 'COMPUTADOR' AND SITUACAO ='ATIVO'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> totNotebook() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID_COMPUTADOR FROM TI_COMPUTADOR WHERE TIPO = 'NOTEBOOK' AND SITUACAO ='ATIVO'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> totWindows() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT \n"
                + "SUM(X.OPEN + X.OEM) cont, 0 OEM\n"
                + "--SUM(X.OPEN + X.OEM)\n"
                + "FROM (\n"
                + "SELECT SUM(B.QUANTIDADE) OPEN, 0 OEM\n"
                + "FROM TI_LICENSE A,\n"
                + "  TI_TIPO_LICENSE B\n"
                + "WHERE A.ID_LICENSE = B.ID_LICENSE\n"
                + "AND A.ID_LICENSE  IN (4,5,6)\n"
                + "------------ OPEN LICENSE\n"
                + "UNION \n"
                + "------------ OEM\n"
                + "SELECT 0 OPEN, COUNT(*)OEM\n"
                + "FROM TI_SOFTWARE A,\n"
                + "  TI_DESCR_SERIAL B,\n"
                + "  TI_TIPO_SOFTWARE C\n"
                + "WHERE A.ID_DESCR_SERIAL = B.ID_DESCR_SERIAL\n"
                + "AND A.ID_TIPO_SOFTWARE = C.ID_TIPO_SOFTWARE\n"
                + "AND C.DESCRICAO         = 'WINDOWS'\n"
                + "AND A.SITUACAO = 'ATIVO'\n"
                + ")X");
        List<Object[]> results = query.getResultList();
        return results;
    }

    // TOTAL DE WINDOWS OPEN
    public List<Object[]> windowsOpen() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT SUM(B.QUANTIDADE) OPEN,\n"
                + "    0 OEM\n"
                + "  FROM TI_LICENSE A,\n"
                + "    TI_TIPO_LICENSE B\n"
                + "  WHERE A.ID_LICENSE = B.ID_LICENSE\n"
                + "  AND A.ID_LICENSE  IN (4,5,6)");
        List<Object[]> results = query.getResultList();
        return results;
    }

    // TOTAL DE WINDOWS OEM
    public List<Object[]> windowsOem() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT 0 OPEN,\n"
                + "    COUNT(*)OEM\n"
                + "  FROM TI_SOFTWARE A,\n"
                + "    TI_DESCR_SERIAL B,\n"
                + "    TI_TIPO_SOFTWARE C\n"
                + "  WHERE A.ID_DESCR_SERIAL = B.ID_DESCR_SERIAL\n"
                + "  AND A.ID_TIPO_SOFTWARE  = C.ID_TIPO_SOFTWARE\n"
                + "  AND C.DESCRICAO         = 'WINDOWS'\n"
                + "  AND A.SITUACAO          = 'ATIVO'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> WindowsUsado() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM V_COMPUTADOR_SOFTWARE WHERE ID_DESCR_SERIAL IN(8,9,10,11,12,18,19)AND SITUACAO_SOFT ='ATIVO' ");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> officeUsado() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM V_COMPUTADOR_SOFTWARE WHERE ID_DESCR_SERIAL IN(3,4,20,21,22,23,24,25)AND SITUACAO_SOFT ='ATIVO'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> officeDisponivel() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.ID_SOFTWARE,\n"
                + "C.DESCRICAO,\n"
                + "A.KEY,\n"
                + "A.DATA,\n"
                + "A.NF\n"
                + "FROM TI_SOFTWARE A, \n"
                + "TI_TIPO_SOFTWARE B,\n"
                + "TI_DESCR_SERIAL C\n"
                + "WHERE A.ID_TIPO_SOFTWARE = B.ID_TIPO_SOFTWARE \n"
                + "AND C.ID_DESCR_SERIAL = A.ID_DESCR_SERIAL\n"
                + "AND B.ID_TIPO_SOFTWARE = 2\n"
                + "AND NOT EXISTS (SELECT 1 FROM V_COMPUTADOR_SOFTWARE C WHERE C.ID_SOFTWARE = A.ID_SOFTWARE)\n"
                + "UNION\n"
                + "SELECT A.ID_SOFTWARE,\n"
                + "D.DESCRICAO,\n"
                + "C.SERIAL,\n"
                + "A.DATA,\n"
                + "A.NF\n"
                + "FROM TI_SOFTWARE A, \n"
                + "TI_TIPO_SOFTWARE B,\n"
                + "TI_SERIAL_OPEN_LICENSE C,\n"
                + "TI_DESCR_SERIAL D\n"
                + "WHERE A.ID_TIPO_SOFTWARE = B.ID_TIPO_SOFTWARE \n"
                + "AND A.ID_SERIAL = C.ID_SERIAL\n"
                + "AND D.ID_DESCR_SERIAL = C.ID_DESCR_SERIAL\n"
                + "AND B.ID_TIPO_SOFTWARE = 2\n"
                + "AND NOT EXISTS (SELECT 1 FROM V_COMPUTADOR_SOFTWARE C WHERE C.ID_SOFTWARE = A.ID_SOFTWARE)");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windowsDisponivel() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT OP.CONTRATO,\n"
                + "       TIP.QUANTIDADE,\n"
                + "       sum((select count(*)  \n"
                + "          from ti_software  ts\n"
                + "             , ti_serial_open_license   tso\n"
                + "             , ti_computador_software    tc\n"
                + "         where tso.id_serial   = ts.id_serial\n"
                + "           and tc.id_software    = ts.id_software\n"
                + "           and tso.id_serial = ser.id_serial )) qde_usada       \n"
                + "FROM TI_SOFTWARE SO,\n"
                + "     TI_SERIAL_OPEN_LICENSE SER,\n"
                + "     TI_TIPO_LICENSE TIP,\n"
                + "     TI_OPEN_LICENSE OP\n"
                + "WHERE SO.ID_SERIAL = SER.ID_SERIAL\n"
                + "AND SER.ID_TIPO_LICENSE  = TIP.ID_TIPO_LICENSE\n"
                + "AND TIP.ID_OPEN_LICENSE  = OP.ID_OPEN_LICENSE\n"
                + "AND SER.ID_DESCR_SERIAL IN (8,9,10,11,12) \n"
                + "GROUP by OP.CONTRATO,\n"
                + "       TIP.QUANTIDADE\n"
                + "--and ser.serial ='3CG4Q-HN864-YJFCK-J49BX-YDVQQ'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> listarWindowsDisponivel(int contrato) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT a.id_software,\n"
                + "E.DESCRICAO,\n"
                + "B.SERIAL,\n"
                + "A.DATA,\n"
                + "A.NF,\n"
                + "C.QUANTIDADE\n"
                + "FROM TI_SOFTWARE A, \n"
                + "TI_SERIAL_OPEN_LICENSE B,\n"
                + "TI_TIPO_LICENSE C,\n"
                + "TI_OPEN_LICENSE D,\n"
                + "TI_DESCR_SERIAL e\n"
                + "WHERE A.ID_SERIAL = B.ID_SERIAL\n"
                + "AND B.ID_TIPO_LICENSE = C.ID_TIPO_LICENSE\n"
                + "AND C.ID_OPEN_LICENSE = D.ID_OPEN_LICENSE\n"
                + "AND B.ID_DESCR_SERIAL = E.ID_DESCR_SERIAL\n"
                + "AND D.CONTRATO IN ("+contrato+")\n"
                + "AND B.ID_DESCR_SERIAL IN(8,9,10,11,12)");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> totOffice() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT \n"
                + "SUM(X.OPEN + X.OEM) cont, 0 OEM\n"
                + "--SUM(X.OPEN + X.OEM)\n"
                + "FROM (\n"
                + "SELECT SUM(B.QUANTIDADE) OPEN, 0 OEM\n"
                + "FROM TI_LICENSE A,\n"
                + "  TI_TIPO_LICENSE B\n"
                + "WHERE A.ID_LICENSE = B.ID_LICENSE\n"
                + "AND A.ID_LICENSE  IN (1)\n"
                + "------------ OPEN LICENSE\n"
                + "UNION \n"
                + "------------ OEM\n"
                + "SELECT 0 OPEN, COUNT(*)OEM\n"
                + "FROM TI_SOFTWARE A,\n"
                + "  TI_DESCR_SERIAL B,\n"
                + "  TI_TIPO_SOFTWARE C\n"
                + "WHERE A.ID_DESCR_SERIAL = B.ID_DESCR_SERIAL\n"
                + "AND A.ID_TIPO_SOFTWARE = C.ID_TIPO_SOFTWARE\n"
                + "AND C.DESCRICAO         = 'OFFICE'\n"
                + "AND A.SITUACAO = 'ATIVO'\n"
                + ")X");
        List<Object[]> results = query.getResultList();
        return results;
    }

    // TOTAL DE OFFICE OPEN
    public List<Object[]> officeOpen() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT SUM(B.QUANTIDADE) OPEN,\n"
                + "    0 OEM\n"
                + "  FROM TI_LICENSE A,\n"
                + "    TI_TIPO_LICENSE B\n"
                + "  WHERE A.ID_LICENSE = B.ID_LICENSE\n"
                + "  AND A.ID_LICENSE  IN (1)");
        List<Object[]> results = query.getResultList();
        return results;
    }

    // TOTAL DE OFFICE FPP
    public List<Object[]> officeFpp() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT 0 OPEN,\n"
                + "    COUNT(*)OEM\n"
                + "  FROM TI_SOFTWARE A,\n"
                + "    TI_DESCR_SERIAL B,\n"
                + "    TI_TIPO_SOFTWARE C\n"
                + "  WHERE A.ID_DESCR_SERIAL = B.ID_DESCR_SERIAL\n"
                + "  AND A.ID_TIPO_SOFTWARE  = C.ID_TIPO_SOFTWARE\n"
                + "  AND C.DESCRICAO         = 'OFFICE'\n"
                + "  AND A.SITUACAO          = 'ATIVO'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> totEmail() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM EMAIL ");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> office2019() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('HOME AND BUSINESS 2019','OFFICE STANDARD 2019') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> office2016() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('HOME AND BUSINESS 2016','OFFICE STANDARD 2016') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> office2013() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('HOME AND BUSINESS 2013','OFFICE STANDARD 2013') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> office2010() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('HOME AND BUSINESS 2010') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> office2007() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('OFFICE STANDARD 2007') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windows10() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('WINDOWS 10 PRO') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windows8_1() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('WINDOWS 8.1') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windows8() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('WINDOWS 8') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windows7() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('WINDOWS 7','WINDOWS 7 ULTIMATE') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windowsVista() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('WINDOWS VISTA (TM) BUSINESS') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> windowsXp() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE IN ('WINDOWS XP') AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> autoCad() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO, SOFTWARE FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE LIKE '%AUTOCAD%' AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> project() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO, SOFTWARE FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE LIKE '%PROJECT%' AND SITUACAO_SOFT ='ATIVO' ORDER BY 4");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> agroCad() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT COMPUTADOR, SERIAL, CONTRATO, SOFTWARE FROM V_COMPUTADOR_SOFTWARE WHERE SOFTWARE LIKE '%AGROCAD%' AND SITUACAO_SOFT ='ATIVO' ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> remoteDesktopService() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT B.DESCRICAO, \n"
                + "SUM(A.QUANTIDADE) \n"
                + "FROM TI_TIPO_LICENSE A, \n"
                + "TI_LICENSE B\n"
                + "WHERE B.ID_LICENSE = A.ID_LICENSE \n"
                + "AND A.ID_LICENSE IN (7,8,9,10)\n"
                + "group by B.DESCRICAO \n"
                + "ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Metodo converter usado no AUTOCOMPLETE----------------



    //----------------Serial OpenLicense -----------------------






    //----------------Open License -----------------------
//    public List<Object[]> buscarContrato(BigDecimal contrato) {
//        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID_OPEN_LICENSE, CONTRATO FROM OPEN_LICENSE where CONTRATO = " + contrato);
//        List<Object[]> results = query.getResultList();
//        return results;
//    }


    public List<Object[]> verificarOpenLicenseVinculado(BigDecimal id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT  A.ID_OPEN_LICENSE, A.CONTRATO FROM TI_OPEN_LICENSE A, TI_TIPO_LICENSE B WHERE B.ID_OPEN_LICENSE = A.ID_OPEN_LICENSE AND A.ID_OPEN_LICENSE = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> verificarTipoLicenseVinculado(Integer id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.ID_TIPO_LICENSE,\n"
                + "B.SERIAL\n"
                + "FROM TI_TIPO_LICENSE A,\n"
                + "TI_SERIAL_OPEN_LICENSE B\n"
                + "WHERE A.ID_TIPO_LICENSE = B.ID_TIPO_LICENSE\n"
                + "AND A.ID_TIPO_LICENSE = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> verificarSerialVinculado(Integer id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.ID_SERIAL,\n"
                + "B.ID_SOFTWARE\n"
                + "FROM TI_SERIAL_OPEN_LICENSE A,\n"
                + "  TI_SOFTWARE B,\n"
                + "  TI_COMPUTADOR_SOFTWARE C  \n"
                + "WHERE B.ID_SERIAL = A.ID_SERIAL\n"
                + "AND C.ID_SOFTWARE(+) = B.ID_SOFTWARE\n"
                + "AND A.ID_SERIAL = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> listarTabelaOpenLicense() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT distinct A.CONTRATO,\n"
                //    + "  C.DESCRICAO,\n"
                + "  A.FORNECEDOR,\n"
                //    + "  B.QUANTIDADE,\n"
                + "  A.ID_OPEN_LICENSE\n"
                + "FROM TI_OPEN_LICENSE A,\n"
                + "  TI_TIPO_LICENSE B,\n"
                + "  TI_LICENSE C\n"
                + "WHERE A.ID_OPEN_LICENSE = B.ID_OPEN_LICENSE(+)\n"
                + "AND C.ID_LICENSE(+)        = B.ID_LICENSE \n"
                + "ORDER BY A.ID_OPEN_LICENSE DESC");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> pesquisarTabelaOpenLicense(Integer contrato) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.CONTRATO,\n"
                + "  C.DESCRICAO,\n"
                + "  A.FORNECEDOR,\n"
                + "  B.QUANTIDADE,\n"
                + "  A.ID_OPEN_LICENSE\n"
                + "FROM TI_OPEN_LICENSE A,\n"
                + "  TI_TIPO_LICENSE B,\n"
                + "  TI_LICENSE C\n"
                + "WHERE A.ID_OPEN_LICENSE = B.ID_OPEN_LICENSE(+)\n"
                + "AND C.ID_LICENSE(+)        = B.ID_LICENSE\n"
                + "AND A.CONTRATO LIKE '%" + contrato + "%'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> viewOpenLicense(Integer contrato) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM V_OPEN_LICENSE WHERE ID_OPEN_LICENSE = " + contrato);
        List<Object[]> results = query.getResultList();
        return results;
    }

//----------------License -----------------------


    //----------------SERIAL OPEN LICENSE -----------------------


    public List<Object[]> buscarSerialCadastrado(String serial) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM TI_SERIAL_OPEN_LICENSE WHERE SERIAL = '" + serial + "'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarSerialVinculado(String serial) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM V_COMPUTADOR_SOFTWARE WHERE SERIAL  = '" + serial + "'");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------SOFTWARE -----------------------



    public List<Object[]> buscarSoftRelacionado(Integer id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT a.nf  FROM TI_SOFTWARE A, TI_COMPUTADOR_SOFTWARE B WHERE A.ID_SOFTWARE = B.ID_SOFTWARE AND A.ID_SOFTWARE = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------DESCRICAO SERIAL OPEN LICENSE -----------------------

//----------------RECURSOS HUMANOS----------------------- 
    //----------------Treinamento -----------------------
//    public List<Object[]> buscarTreinamento(Integer idTreinamento) {
//        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT TE.ID,\n"
//                + "  TE.DESCRICAO AS TREINAMENTO,\n"
//                + "  VC. CD_COLAB,\n"
//                + "  VC.NOME_COLAB,\n"
//                + "   VC.NOME_CARGO,\n"
//                + "   VC.DESCR_LOCAL_TRAB\n"
//                + "FROM USINAS.v_colab vc,\n"
//                + "-- TI.FUNC_SGI FS,\n"
//                + "  TI.FUNC_MATRIZ_SGI FMS,\n"
//                + "  TI.FUNC_MATRIZ FM,\n"
//                + "  TI.FUNC_MATRIZ_TREINAMENTO FT,\n"
//                + "  TI.TREINAMENTO TE\n"
//                + "WHERE FMS.ID_MATRIZ     = FM.ID\n"
//                + "AND FM.ID             = FT.ID_FUNC_MATRIZ\n"
//                + "AND FT.ID_TREINAMENTO = TE.ID\n"
//                + "AND FMS.ID_SGI      = vc.FUNCAO_ID\n"
//                + "AND vc.DT_DEMIS      IS NULL\n"
//                + "AND VC.SIND_ID_ASSOC = 4\n"
//                + "and TE.ID =" + idTreinamento + "\n"
//                + "ORDER BY 2,4");
//        List<Object[]> results = query.getResultList();
//        return results;
//    }
    //LISTAR TREINAMENTO TELA TREINAMENTO.XHTML
//    public List<Object[]> buscarTreinamento(Integer idTreinamento) {
//        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT  TE.ID,\n"
//                + "    TE.DESCRICAO AS TREINAMENTO,\n"
//                + "    VC.CD_COLAB,\n"
//                + "    VC.NOME_COLAB,\n"
//                + "    VC.NOME_CARGO,\n"
//                + "    VC.DESCR_LOCAL_TRAB,\n"
//                + "    TE.VALIDADE,\n"
//                + "-----\n"
//                + " (SELECT MAX(AC.DT_FIM) \n"
//                + "    FROM AGENDA_CURSO AC ,\n"
//                + "      CURSO C ,\n"
//                + "      CURSO_FORN CF ,\n"
//                + "      CURSO_COLAB CC ,\n"
//                + "      ti.TREINAMENTO_CURSO_SGI TRE \n"
//                + "    WHERE AC.CURSOFORN_ID = CF.CURSOFORN_ID\n"
//                + "    AND   CF.CURSO_ID       = C.CURSO_ID\n"
//                + "    AND   AC.AGENDACURS_ID  = CC.AGENDACURS_ID\n"
//                + "    AND   CC.COLAB_ID       = VC.COLAB_ID\n"
//                + "    AND   C.CURSO_ID        = TRE.ID_CURSO\n"
//                + "    AND   TE.ID             = TRE.ID_TREINAMENTO\n"
//                + "    ) as DATA_fim\n"
//                + " ----\n"
//                + "  FROM USINAS.v_colab vc,\n"
//                + "    TI.FUNC_MATRIZ_SGI FMS,\n"
//                + "    TI.FUNC_MATRIZ FM,\n"
//                + "    TI.FUNC_MATRIZ_TREINAMENTO FT,\n"
//                + "    TI.TREINAMENTO TE\n"
//                + "  WHERE FMS.ID_MATRIZ   = FM.ID\n"
//                + "  AND FM.ID             = FT.ID_FUNC_MATRIZ\n"
//                + "  AND FT.ID_TREINAMENTO = TE.ID\n"
//                + "  AND FMS.ID_SGI        = vc.FUNCAO_ID\n"
//                + "  AND vc.DT_DEMIS       IS NULL\n"
//                + "  AND VC.SIND_ID_ASSOC  = 4\n"
//                + "and TE.ID =" + idTreinamento + "\n"
//                + "ORDER BY 2,4");
//        List<Object[]> results = query.getResultList();
//        return results;
//    }
    public List<Object[]> buscarTreinamento(Integer idTreinamento) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT TE.ID,\n"
                + "  TE.TREINAMENTO,\n"
                + "  VC.CD_COLAB,\n"
                + "  VC.NOME_COLAB,\n"
                + "  VC.NOME_CARGO,\n"
                + "  VC.DESCR_LOCAL_TRAB,\n"
                + "  TE.VALIDADE,\n"
                + "  -----\n"
                + "  (\n"
                + "  SELECT MAX(AC.DT_FIM)\n"
                + "  FROM AGENDA_CURSO AC ,\n"
                + "    CURSO C ,\n"
                + "    CURSO_FORN CF ,\n"
                + "    CURSO_COLAB CC ,\n"
                + "    ti.TREINAMENTO_CURSO_SGI TRE\n"
                + "  WHERE AC.CURSOFORN_ID = CF.CURSOFORN_ID\n"
                + "  AND CF.CURSO_ID       = C.CURSO_ID\n"
                + "  AND AC.AGENDACURS_ID  = CC.AGENDACURS_ID\n"
                + "  AND CC.COLAB_ID       = VC.COLAB_ID\n"
                + "  AND C.CURSO_ID        = TRE.ID_CURSO\n"
                + "  AND TE.ID             = TRE.ID_TREINAMENTO\n"
                + "  ) AS DATA_fim\n"
                + "  ----\n"
                + "FROM USINAS.v_colab vc,\n"
                + "  TI.RH_FUNC_QUAL_SGI FQS,\n"
                + "  TI.RH_FUNCAO_QUALIDADE FQ,\n"
                + "  TI.RH_FUNCAO_TREINAMENTO FT,\n"
                + "  TI.RH_TREINAMENTO_QUALIDADE TE,\n"
                + "  RH_TREIN_LOCAL_TRAB LT\n"
                + "WHERE FQS.ID_FUNCAO_QUALIDADE   = FQ.ID\n"
                + "AND FQ.ID             = FT.ID_FUNCAO\n"
                + "AND FT.ID_TREINAMENTO = TE.ID\n"
                + "AND FQS.FUNCAO_SGI        = vc.FUNCAO_ID\n"
                + "AND LT.ID_TREINAMENTO = TE.ID\n"
                + "AND vc.CD_LOCAL_TRAB  = LT.ID_LOCAL_TRAB\n"
                + "AND vc.DT_DEMIS      IS NULL\n"
                + "AND VC.SIND_ID_ASSOC  = 4\n"
                + "AND VC.NOME_CARGO     IN ('OPERADOR CENTRIFUGA II','OPERADOR CENTRIFUGA I','AUXILIAR INDUSTRIAL')\n"
                + "AND TE.ID             = " + idTreinamento + "\n"
                + "UNION\n"
                + "SELECT DISTINCT TE.ID,\n"
                + "  TE.TREINAMENTO,\n"
                + "  VC.CD_COLAB,\n"
                + "  VC.NOME_COLAB,\n"
                + "  VC.NOME_CARGO,\n"
                + "  VC.DESCR_LOCAL_TRAB,\n"
                + "  TE.VALIDADE,\n"
                + "  -----\n"
                + "  (\n"
                + "  SELECT MAX(AC.DT_FIM)\n"
                + "  FROM AGENDA_CURSO AC ,\n"
                + "    CURSO C ,\n"
                + "    CURSO_FORN CF ,\n"
                + "    CURSO_COLAB CC ,\n"
                + "    ti.TREINAMENTO_CURSO_SGI TRE\n"
                + "  WHERE AC.CURSOFORN_ID = CF.CURSOFORN_ID\n"
                + "  AND CF.CURSO_ID       = C.CURSO_ID\n"
                + "  AND AC.AGENDACURS_ID  = CC.AGENDACURS_ID\n"
                + "  AND CC.COLAB_ID       = VC.COLAB_ID\n"
                + "  AND C.CURSO_ID        = TRE.ID_CURSO\n"
                + "  AND TE.ID             = TRE.ID_TREINAMENTO\n"
                + "  ) AS DATA_fim\n"
                + "  ----\n"
                + "FROM USINAS.v_colab vc,\n"
                + "  TI.RH_FUNC_QUAL_SGI FQS,\n"
                + "  TI.RH_FUNCAO_QUALIDADE FQ,\n"
                + "  TI.RH_FUNCAO_TREINAMENTO FT,\n"
                + "  TI.RH_TREINAMENTO_QUALIDADE TE\n"
                + "WHERE FQS.ID_FUNCAO_QUALIDADE   = FQ.ID\n"
                + "AND FQ.ID             = FT.ID_FUNCAO\n"
                + "AND FT.ID_TREINAMENTO = TE.ID\n"
                + "AND FQS.FUNCAO_SGI        = vc.FUNCAO_ID\n"
                + "AND vc.DT_DEMIS      IS NULL\n"
                + "AND VC.SIND_ID_ASSOC  = 4\n"
                + "AND VC.NOME_CARGO    != 'AUXILIAR INDUSTRIAL'\n"
                + "AND VC.NOME_CARGO    != 'OPERADOR CENTRIFUGA II'\n"
                + "AND VC.NOME_CARGO    != 'OPERADOR CENTRIFUGA I'\n"
                + "AND TE.ID             = " + idTreinamento + "\n"
                + "AND VC.CD_CCUSTO NOT LIKE '3%'\n"
                + "ORDER BY 5");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarTreinamentoFeito(Integer idTreinamento, BigDecimal cdColab) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT te.id\n"
                + ",c.CURSO_ID\n"
                + ",ac.AGENDACURS_ID\n"
                + ",c.DESCR\n"
                + ",VC.CD_COLAB\n"
                + ",VC.NOME_COLAB\n"
                + ",ac.DT_FIM\n"
                + "FROM agenda_curso ac\n"
                + "  ,curso c\n"
                + "  ,curso_forn cf\n"
                + "  ,curso_colab cc\n"
                + "  ,V_COLAB VC\n"
                + "  ,TREINAMENTO_CURSO_SGI tre\n"
                + "  ,RH_TREINAMENTO_QUALIDADE te\n"
                + "  WHERE ac.CURSOFORN_ID = cf.CURSOFORN_ID\n"
                + "   and cf.CURSO_ID = c.CURSO_ID\n"
                + "  and ac.AGENDACURS_ID = cc.AGENDACURS_ID\n"
                + "  and cc.COLAB_ID = VC.COLAB_ID\n"
                + "  and c.CURSO_ID = tre.ID_CURSO\n"
                + "  and te.ID = tre.ID_TREINAMENTO\n"
                + "  and vc.dt_demis is null\n"
                + "and TE.ID =" + idTreinamento + "\n"
                + "and vc.cd_colab =" + cdColab + "\n"
                + "ORDER BY 1,3"
        );
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Avaliação de Treinamento----------------
    public List<Object[]> buscarTurma(BigDecimal turma) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT \n"
                + "  vc.nome_colab,\n"
                + "  ac.AGENDACURS_ID  TURMA,\n"
                + "  c.descr CURSO,\n"
                + "  vc.COLAB_ID\n"
                + "FROM USINAS.agenda_curso ac,\n"
                + "  USINAS.curso c,\n"
                + "  USINAS.curso_forn cf,\n"
                + "  USINAS.curso_colab cc,\n"
                + "  USINAS.v_colab vc,\n"
                + "  USINAS.forn f,\n"
                + "  USINAS.corr co,\n"
                + "  USINAS.funcao fu\n"
                + "WHERE ac.cursoforn_id = cf.cursoforn_id\n"
                + "AND cf.curso_id = c.curso_id\n"
                + "AND ac.agendacurs_id = cc.agendacurs_id\n"
                + "AND vc.colab_id = cc.colab_id\n"
                + "AND cf.forn_id = f.forn_id\n"
                + "AND f.corr_id = co.corr_id\n"
                + "AND fu.funcao_id = vc.funcao_id\n"
                + "and vc.dt_demis is null\n"
                + "and ac.AGENDACURS_ID =" + turma + "\n"
                + "order by 1 ");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarTurma2(BigDecimal turma) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT \n"
                + "  ac.AGENDACURS_ID TURMA,\n"
                + "  c.descr CURSO,\n"
                + "  AC.INSTRUTOR\n"
                + "FROM USINAS.agenda_curso ac,\n"
                + "  USINAS.curso c,\n"
                + "  USINAS.curso_forn cf\n"
                + "WHERE ac.cursoforn_id = cf.cursoforn_id\n"
                + "AND C.CURSO_ID = CF.CURSO_ID\n"
                + "and ac.AGENDACURS_ID =" + turma + "\n"
                + "order by 1 ");
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------Treinamento Qualidade----------------
    public List<Object[]> buscarTreinamentoQualidade() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select ID,TREINAMENTO FROM RH_TREINAMENTO_QUALIDADE ORDER BY ID DESC");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarLocalTrab() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT CD_LOCAL_TRAB, DESCR_LOCAL_TRAB FROM V_COLAB WHERE DT_DEMIS IS NULL AND NOME_CARGO LIKE '%AUXILIAR INDUSTRIAL%'\n"
                + "UNION\n"
                + "SELECT DISTINCT CD_LOCAL_TRAB, DESCR_LOCAL_TRAB FROM V_COLAB WHERE DT_DEMIS IS NULL AND NOME_CARGO LIKE '%CENTRIFUGA%'");
        List<Object[]> results = query.getResultList();
        return results;
    }

//    public List<TreinamentoQualidade> buscarTreinamentoQualidadeOrderAlfabetica() {
//        return (List<TreinamentoQualidade>) em.createNativeQuery("SELECT * FROM RH_TREINAMENTO_QUALIDADE ORDER BY 2 ", TreinamentoQualidade.class).getResultList();
//    }

    public List<Object[]> buscarTreinamentoOrden() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.AGENDACURS_ID,\n"
                + "C.DESCR,\n"
                + "A.DT_INIC,\n"
                + "A.DT_FIM\n"
                + "FROM USINAS.AGENDA_CURSO A,\n"
                + "  USINAS.CURSO_FORN B,\n"
                + "  USINAS.CURSO C\n"
                + "WHERE A.CURSOFORN_ID = B.CURSOFORN_ID\n"
                + "AND C.CURSO_ID       = B.CURSO_ID\n"
                + "AND A.DT_INIC > '01/01/2018'\n"
                + "ORDER BY 3 DESC,2");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarColabTreinamento(BigDecimal turma) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT \n"
                + "E.CD_COLAB, \n"
                + "E.NOME_COLAB,\n"
                + "E.NOME_CARGO,\n"
                + "E.DESCR_LOCAL_TRAB\n"
                + "FROM USINAS.AGENDA_CURSO A,\n"
                + "  USINAS.CURSO_FORN B,\n"
                + "  USINAS.CURSO C,\n"
                + "  USINAS.CURSO_COLAB D,\n"
                + "  USINAS.V_COLAB E\n"
                + "WHERE A.CURSOFORN_ID = B.CURSOFORN_ID\n"
                + "AND C.CURSO_ID       = B.CURSO_ID\n"
                + "AND A.AGENDACURS_ID       = D.AGENDACURS_ID\n"
                + "AND D.COLAB_ID       = E.COLAB_ID\n"
                + "AND A.AGENDACURS_ID = " + turma + "\n"
                + "ORDER BY 2 ");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarTreinamentoPorCodigo(BigDecimal id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID, TREINAMENTO FROM RH_TREINAMENTO_QUALIDADE WHERE ID =" + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

//    public List<Object[]> buscarFuncao() {
//        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM rh_funcao_qualidade order by id desc");
//        List<Object[]> results = query.getResultList();
//        return results;
//    }

    public List<Object[]> BuscarFuncaoSGI(BigDecimal funcaoId) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT FUNCAO_ID, NOME_CARGO FROM v_colab WHERE FUNCAO_ID =" + funcaoId);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> BuscarNovaFuncao() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT a.cd_colab,\n"
                + "                a.nome_colab,\n"
                + "                a.nome_cargo,\n"
                + "                a.CD_CCUSTO,\n"
                + "                a.FUNCAO_ID\n"
                + "FROM v_colab     a \n"
                + "WHERE a.dt_demis  IS NULL\n"
                + "AND SUBSTR(a.CD_CCUSTO,1,1) IN('1','2')\n"
                + "AND a.nome_cargo != 'APRENDIZ'\n"
                + "and not EXISTS (SELECT * FROM TI.RH_FUNC_QUAL_SGI b where a.FUNCAO_ID = b.FUNCAO_SGI)\n"
                + "ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> BuscarFuncaoFilha() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT\n"
                + "                a.nome_cargo,\n"
                + "                a.FUNCAO_ID\n"
                + "FROM v_colab     a \n"
                + "WHERE a.dt_demis  IS NULL\n"
                + "AND SUBSTR(a.CD_CCUSTO,1,1) IN('1','2')\n"
                + "and not EXISTS (SELECT * FROM TI.RH_FUNC_QUAL_SGI b where a.FUNCAO_ID = b.FUNCAO_SGI)\n"
                + "ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> BuscarFuncaoFilhaVinculadas(BigDecimal id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT\n"
                + "VC.NOME_CARGO\n"
                + ",VC.FUNCAO_ID\n"
                + "FROM V_COLAB VC\n"
                + ",RH_FUNC_QUAL_SGI RS\n"
                + ",RH_FUNCAO_QUALIDADE FQ\n"
                + "WHERE VC.FUNCAO_ID = RS.FUNCAO_SGI\n"
                + "AND RS.ID_FUNCAO_QUALIDADE = FQ.ID\n"
                + "AND VC.DT_DEMIS IS NULL\n"
                + "AND FQ.ID =" + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> ListarTreinamentoLocal() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT A.CD_LOCAL_TRAB\n"
                + ", A.DESCR_LOCAL_TRAB\n"
                + ", B.TREINAMENTO\n"
                + ", B.ID\n"
                + "FROM USINAS.V_COLAB A\n"
                + ", RH_TREINAMENTO_QUALIDADE B\n"
                + ", RH_TREIN_LOCAL_TRAB C\n"
                + "WHERE A.CD_LOCAL_TRAB = C.ID_LOCAL_TRAB\n"
                + "AND B.ID = C.ID_TREINAMENTO");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> ExcluirTreinamentoLocal() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarFuncaoFilhaETreinamentoVinculado(Integer id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.FUNCAO FROM RH_FUNCAO_QUALIDADE A, RH_FUNC_QUAL_SGI B WHERE A.ID = B.ID_FUNCAO_QUALIDADE AND A.ID = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarFuncaoFilhaETreinamentoVinculado2(Integer id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT A.FUNCAO FROM RH_FUNCAO_QUALIDADE A, RH_FUNCAO_TREINAMENTO B WHERE A.ID = B.ID_FUNCAO AND A.ID = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------TreinamentoColaborador----------------
    public List<Object[]> buscarColaboradoresT(BigDecimal mat, BigDecimal empresa) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select colab_id, cd_colab, nome_colab from USINAS.v_colab where dt_demis is null and cd_colab = " + mat + "and cd_empr = " + empresa);
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> ecluirTreinamentoVinculado(Integer id) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT * FROM RH_FUNCAO_TREINAMENTO WHERE ID_TREINAMENTO = " + id);
        List<Object[]> results = query.getResultList();
        return results;
    }

    //----------------TreinamentoColaborador----------------

    public List<Object[]> buscarTurmaExistente2(BigDecimal turma) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT ID_AVALIACAO, COLAB_ID, DATA, NOME_COLAB, NOTA1,NOTA2, NOTA3, TURMA, TRUNC((((NOTA1+NOTA2+NOTA3)/15)*100),2) total FROM RH_AVALIACAO  WHERE TURMA = " + turma + " order by nome_colab");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarAvaliacaoColab(Integer empresa, Integer matricula) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT D.DESCR,\n"
                + "  A.NOTA1,\n"
                + "  A.NOTA2,\n"
                + "  A.NOTA3,\n"
                + "  TRUNC((((NOTA1+NOTA2+NOTA3)/15)*100),2) total,\n"
                + "   A.NOME_COLAB,\n"
                + "   A.DATA\n"
                + "FROM RH_AVALIACAO A,\n"
                + "    USINAS.V_COLAB B,\n"
                + "    USINAS.AGENDA_CURSO C,\n"
                + "    USINAS.CURSO D,\n"
                + "    USINAS.CURSO_FORN E\n"
                + "WHERE A.COLAB_ID = B.COLAB_ID\n"
                + "AND A.TURMA = C.AGENDACURS_ID\n"
                + "AND C.CURSOFORN_ID = E.CURSOFORN_ID\n"
                + "AND D.CURSO_ID = E.CURSO_ID\n"
                + "AND B.CD_EMPR = " + empresa + "\n"
                + "AND B.CD_COLAB =" + matricula + "\n"
                + "order by 1 \n");
        List<Object[]> results = query.getResultList();
        return results;
    }


    public List<Object[]> buscarColabTreinamentoObrigatorio(BigDecimal matricula, BigDecimal empresa) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT VC. CD_COLAB,\n"
                + "  VC.NOME_COLAB,\n"
                + "  TE.ID,\n"
                + "  TE.TREINAMENTO AS TREINAMENTO,\n"
                + "  NULL data\n"
                + "FROM USINAS.v_colab vc,\n"
                + "  TI.RH_FUNC_QUAL_SGI FMS,\n"
                + "  TI.RH_FUNCAO_QUALIDADE FM,\n"
                + "  TI.RH_FUNCAO_TREINAMENTO FT,\n"
                + "  TI.RH_TREINAMENTO_QUALIDADE TE\n"
                + "WHERE vc.FUNCAO_ID    = FMS.FUNCAO_SGI\n"
                + "AND FMS.ID_FUNCAO_QUALIDADE     = FM.ID\n"
                + "AND FM.ID             = FT.ID_FUNCAO\n"
                + "AND FT.ID_TREINAMENTO = TE.ID\n"
                + "AND vc.DT_DEMIS      IS NULL\n"
                + "AND VC.CD_EMPR = " + empresa
                + "\n AND VC.CD_COLAB =" + matricula
                + "ORDER BY 2");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarColabTreinamentoRealizado(BigDecimal matricula, BigDecimal empresa) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT vc.cd_colab MAT,\n"
                + "  vc.nome_colab,\n"
                + "  ac.cursoforn_id  TURMA,\n"
                + "  c.descr CURSO,\n"
                + "  dt_fim\n"
                + "FROM USINAS.agenda_curso ac,\n"
                + "  USINAS.curso c,\n"
                + "  USINAS.curso_forn cf,\n"
                + "  USINAS.curso_colab cc,\n"
                + "  USINAS.v_colab vc,\n"
                + "  USINAS.forn f,\n"
                + "  USINAS.corr co,\n"
                + "  USINAS.funcao fu\n"
                + "WHERE ac.cursoforn_id = cf.cursoforn_id\n"
                + "AND cf.curso_id = c.curso_id\n"
                + "AND ac.agendacurs_id = cc.agendacurs_id\n"
                + "AND vc.colab_id = cc.colab_id\n"
                + "AND cf.forn_id = f.forn_id\n"
                + "AND f.corr_id = co.corr_id\n"
                + "AND fu.funcao_id = vc.funcao_id\n"
                + "and vc.dt_demis is null\n"
                + "AND VC.CD_EMPR = " + empresa
                + "\n AND VC.CD_COLAB =" + matricula
                + "order by 4");
        List<Object[]> results = query.getResultList();
        return results;
    }
//----------------AGRICOLA-----------------------    
    //----------------CanaDiaFrenteMB----------------

    public List<Object[]> buscarEntradaDeCanaFrente() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT FRE.DESCR FRENTE\n"
                + "      ,FRC.CAPAC_COLHE\n"
                + "      ,(SELECT SUM(FRC.CAPAC_COLHE)\n"
                + "        FROM FRENTE_CAPAC_DIARIA FRC\n"
                + "          ,FRENTE FRE\n"
                + "        WHERE FRC.FRENTE_ID = FRE.FRENTE_ID\n"
                + "        AND '18/11/2018' BETWEEN FRC.DT_INIC and FRC.DT_FIM\n"
                + "        AND FRE.CD IN (1,2,3,4,5,57,72,76,77) \n"
                + "       ) total  \n"
                + "       ,FRE.CD\n"
                + "       ,FRC.DT_INIC\n"
                + "       ,FRC.DT_FIM\n"
                + "FROM FRENTE_CAPAC_DIARIA FRC\n"
                + "    ,FRENTE FRE\n"
                + "WHERE FRC.FRENTE_ID = FRE.FRENTE_ID\n"
                + "AND '18/11/2018' BETWEEN FRC.DT_INIC and FRC.DT_FIM\n"
                + "AND FRE.CD IN (1,2,3,4,5,57,72,76,77) \n"
                + "ORDER BY 1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarEntradaDeCanaFrente2(BigDecimal idFrente) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT (SUM(CC.PESO_BRUTO)-SUM(CC.TARA))/1000 TON_CANA\n"
                + "FROM CERT_CAMPO CC ,\n"
                + "  FRENTE FR ,\n"
                + "  LIB_COLH LC\n"
                + "WHERE LC.FRENTE_ID   = FR.FRENTE_ID\n"
                + "AND LC.LIBCOLH_ID  = CC.LIBCOLH_ID\n"
                + "AND TO_CHAR(CC.DT_HR_REF,'DD/MM/YYYY') = '18/11/2018'\n"
                + "AND FR.CD = " + idFrente + "\n"
                + "AND LC.MOTLIBER_ID = '1'\n"
                + "");
        List<Object[]> results = query.getResultList();
        return results;
    }
//----------------COMPRAS-----------------------    
    //----------------COMF0031----------------

//    public List<Object[]> buscarColf0031() {
//        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("select DISTINCT ct.forn_corr_id\n"
//                + "                      , ct.forn_nome\n"
//                + "                      , pc_nro\n"
//                + "                      , pc_dt\n"
//                + "                      , unidmed_sg\n"
//                + "                      , parcitpc_qt_forn\n"
//                + "                      , qt_recebida\n"
//                + "                      , dt_prev_entr\n"
//                + "                      , dias_atraso\n"
//                + "                       from comf0031_temp ct\n"
//                + "                  --    from teste ct\n"
//                + "                  where ct.comf031tmp_id = 1\n"
//                + "                  order by 9 desc,1");
//        List<Object[]> results = query.getResultList();
//        return results;
//    }
    public List<Object[]> buscarColf0031() {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT DISTINCT ct.forn_corr_id ,\n"
                + "  ct.forn_nome ,\n"
                + "  pc_nro ,\n"
                + "  pc_dt ,\n"
                + " -- unidmed_sg\n"
                + "  --, parcitpc_qt_forn\n"
                + "  -- , qt_recebida\n"
                + "   dt_prev_entr\n"
                + "  ,dias_atraso\n"
                + "  ,CT.CORR_COMPRADOR\n"
                + "  ,CT.CORR_EMPRESA\n"
                + "FROM comf0031_temp ct\n"
                + "--FROM teste ct\n"
                + "WHERE ct.comf031tmp_id = 1\n"
                + "AND CT.CORR_EMPRESA in (4,5,20,23)\n"
                + "ORDER BY 6 DESC,\n"
                + "  1");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public List<Object[]> buscarColf0031View(BigDecimal pedido) {
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNativeQuery("SELECT \n"
                + "  cd_prod ,\n"
                + "  descr_prod ,\n"
                + "  unidmed_sg ,\n"
                + "  parcitpc_qt_forn ,\n"
                + "  qt_recebida \n"
                + "  ,CT.CORR_COMPRADOR\n"
                + "  ,CT.CORR_EMPRESA\n"
                + "FROM comf0031_temp ct\n"
                + "WHERE ct.comf031tmp_id = 1\n"
                + "and CT.PC_NRO = " + pedido + "\n"
                + "AND CT.CORR_EMPRESA in (4,5,20,23)\n"
                + "ORDER BY 2 ");
        List<Object[]> results = query.getResultList();
        return results;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}
