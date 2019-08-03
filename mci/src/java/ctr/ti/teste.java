/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ctr.ti;

import dao.Dao;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Iterator;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.LDAPSearchResults;
import com.novell.ldap.util.Base64;
import java.util.Hashtable;

import java.util.Hashtable;
import javax.naming.ldap.*;
import javax.naming.directory.*;
import javax.naming.*;
import javax.net.ssl.*;
import java.io.*;

/**
 *
 * @author enascimento
 */
@ManagedBean
@ViewScoped
public class teste implements Serializable {

    private Dao dao;
    int ldapVersion = LDAPConnection.LDAP_V3;
    int ldapPort = LDAPConnection.DEFAULT_PORT;
    String ldapHost = "172.16.159.152";
    String loginDN = "CN=ora_dba,CN=Users,DC=usinacerradao,DC=com";
    String password = "eter$3uh%Y%Y";

    public teste() {
        CreateUser();
        ad();
    }

    @SuppressWarnings("unchecked")
    public void ad() {
        LDAPConnection conn = new LDAPConnection();
        try {
            conn.connect(ldapHost, ldapPort);
            conn.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
        } catch (LDAPException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // --> realiza a pesquisa
        try {
            LDAPSearchResults searchResults
                    = conn.search("OU=Users,DC=usinacerradao,DC=com,DC=br",
                            LDAPConnection.SCOPE_ONE,
                            "(objectclass=Users)",
                            null,
                            false);
            while (searchResults.hasMore()) {
                LDAPEntry nextEntry = null;
                try {
                    nextEntry = searchResults.next();
                } catch (LDAPException e) {
                    System.out.println("Error: " + e.toString());
                    if (e.getResultCode() == LDAPException.LDAP_TIMEOUT || e.getResultCode() == LDAPException.CONNECT_ERROR) {
                        break;
                    } else {
                        continue;
                    }
                }
                System.out.println("DN -> " + nextEntry.getDN());

                LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
                Iterator allAttributes = attributeSet.iterator();
                while (allAttributes.hasNext()) {
                    LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
                    String attributeName = attribute.getName();
                    Enumeration allValues = attribute.getStringValues();
                    if (allValues != null) {
                        while (allValues.hasMoreElements()) {
                            String Value = (String) allValues.nextElement();
                            if (Base64.isLDIFSafe(Value)) {
                                System.out.println(attributeName + " -> " + Value);
                            } else {
                                Value = Base64.encode(Value.getBytes());
                                System.out.println(attributeName + " -> " + Value);
                            }
                        }
                    }
                }
                System.out.println("\n");
            }

            // --> Desconecta
            conn.disconnect();
        } catch (LDAPException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public void CreateUser() {

        Hashtable env = new Hashtable();
        String adminName = "cn=administrator,OU=Users,dc=usinacerradao,dc=com,dc=br";
        String adminPassword = "eter$3uh%Y";
        String userName = "CN=usuario1,ou=VISITANTES,dc=usinacerradao,dc=com,dc=br";
        //String groupName = "CN=grupoTeste,ou=app1,ou=Sistemas,dc=ims,dc=EDU,dc=DTI";

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, "cn=administrator,dc=usinacerradao,dc=com,dc=br");
        env.put(Context.SECURITY_CREDENTIALS, "eter$3uh%Y");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldaps://172.16.159.152:389");
        env.put(Context.SECURITY_PROTOCOL, "ssl");

        try {

            LdapContext ctx = new InitialLdapContext(env, null);

            Attributes attrs = new BasicAttributes(true);

            BasicAttribute ocattr = new BasicAttribute("objectclass");
            ocattr.add("top");
            ocattr.add("person");
            ocattr.add("organizationalPerson");
            ocattr.add("user");
            attrs.put(ocattr);
            attrs.put("cn", userName);

            int UF_ACCOUNTDISABLE = 0x0002;
            int UF_PASSWD_NOTREQD = 0x0020;
            int UF_PASSWD_CANT_CHANGE = 0x0040;
            int UF_NORMAL_ACCOUNT = 0x0200;
            int UF_DONT_EXPIRE_PASSWD = 0x10000;
            int UF_PASSWORD_EXPIRED = 0x800000;

            attrs.put("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWD_NOTREQD + UF_PASSWORD_EXPIRED + UF_ACCOUNTDISABLE));

            Context result = ctx.createSubcontext(userName, attrs);
            System.out.println("Created disabled account for: " + userName);

            //Para modificar a senha de um usuário no AD, a conexão     //deve ser SSL e a senha em Unicode
            StartTlsResponse tls = (StartTlsResponse) ctx.extendedOperation(new StartTlsRequest());
            tls.negotiate();

            ModificationItem[] mods = new ModificationItem[2];

            String newQuotedPassword = "\"321\"";
            byte[] newUnicodePassword = newQuotedPassword.getBytes("UTF-16LE");

            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", newUnicodePassword));
            mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl", Integer.toString(UF_NORMAL_ACCOUNT + UF_PASSWORD_EXPIRED)));

            ctx.modifyAttributes(userName, mods);
            System.out.println("Set password & updated userccountControl");

            ModificationItem member[] = new ModificationItem[1];
            member[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", userName));
            tls.close();
            //ctx.close();

            System.out.println("Successfully created User: " + userName);

        } catch (NamingException e) {
            System.err.println("Problem creating object: " + e);
        } catch (IOException e) {
            System.err.println("Problem creating object: " + e);
        }
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

}
