
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class teste {

    private File classes[];
    private File diretorio;
    private List<String> lista;
    private List<String> listaTabela;
    private BufferedReader br;
    private StringBuffer bufSaida;
    private StringBuffer projeto;
    private Pattern padrao;
    private Pattern objeto;
    private Pattern nomeCampo;
    private Pattern tamanhoCampo;
    private Pattern padraoColumn;
    private Pattern padraoTemporal;
    private Pattern padraoRelacionamento;
    private Pattern padraoMany;
    private Pattern padraoJoin;
    private Pattern padraoOne;
    private Pattern padraoLista;
    private Pattern nomeObjeto;
    private Matcher pesquisa;
    private Matcher auxiliar;
    private String linha;
    private String codigoXhtml;
    private String codigoCtr;
    private String nomeCtr;
    private String metodosCtr;
    private String nomeClasse;
    private String getsSets;
    private String gravar;

    public static void main(String[] args) throws FileNotFoundException, IOException {
        new teste().executa();
    }

    public void executa() throws FileNotFoundException, IOException {
        File arquivo = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "java" + File.separator + "ctr" + File.separator);
        arquivo.mkdir();

        diretorio = new File(System.getProperty("user.dir") + File.separator + "src"
                + File.separator + "java" + File.separator + "model");
        classes = diretorio.listFiles();
        lista = new ArrayList<String>();
        // nomeClasse = Pattern.compile("([a-zA-Z]+).+");
        padrao = Pattern.compile("(.+@[IGCTMJO].+)|(.+private.+)");

        //Listar classes da model
        for (File f : classes) {
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            br = new BufferedReader(new FileReader(f));
            bufSaida = new StringBuffer();

            while ((linha = br.readLine()) != null) {
                pesquisa = padrao.matcher(linha);
                if (pesquisa.matches()) {
                    bufSaida.append(linha).append("\n");

                    //remover espaços
                    String removeEspaco = "";
                    if (pesquisa.group(1) != null) {
                        for (char c : linha.toCharArray()) {
                            if (c != ' ') {
                                removeEspaco += c;
                            }
                        }
                        System.out.println("===" + removeEspaco);
                        lista.add(removeEspaco);
                    } else {
                        System.out.println("===" + linha);
                        lista.add(linha);
                    }

                }

            }

            organizarLinhas();
            nomeClasse = f.getName().substring(0, f.getName().length() - 5);
            criaCtr(nomeClasse.substring(0, 1).toLowerCase() + nomeClasse.substring(1, nomeClasse.length()));
            criarView(lista, nomeClasse.substring(0, 1).toLowerCase() + nomeClasse.substring(1, nomeClasse.length()));
            //codigoCtr += "}";

            gravarDisco(codigoCtr + metodosCtr + (gravar.concat("}\n\n")) + getsSets + "}", System.getProperty("user.dir") + File.separator + "src" + File.separator + "java" + File.separator + "ctr" + File.separator + nomeClasse + "MB.java");

            lista = new ArrayList<String>();
            br.close();
        }
        criarDao();
    }

    //Organizar linhas parentese
    private void organizarLinhas() {
        int contador1 = 0;
        int contador2 = 0;
        for (int c = 0; c < lista.size() - 1; c++) {

            for (char letra : lista.get(c).toCharArray()) {
                if (letra == '(') {
                    contador1++;
                } else if (letra == ')') {
                    contador2++;
                }
            }
            if (contador1 != contador2) {
                lista.set(c, lista.get(c) + lista.get(c + 1));
                lista.remove(c + 1);
                c--;
            }
            contador1 = 0;
            contador2 = 0;
        }
    }

    //Gravar as os arquivos gerados
    private void gravarDisco(String dado, String url) {
        try {
            FileOutputStream salvar = new FileOutputStream(new File(url), true);
            salvar.write(dado.getBytes());
            salvar.close();
        } catch (Exception ex) {
            System.err.println("ERRO" + ex.getMessage());
        }
    }

    //criar os arquivos da camada view(xhtml)
    private void criarView(List<String> lista, String classe) throws FileNotFoundException, IOException {
        codigoXhtml = "";
        nomeCampo = Pattern.compile(".+name=\"(.+)\".+");
        tamanhoCampo = Pattern.compile(".+length=(.+),.+|.+length=(.+).+");
        padraoColumn = Pattern.compile("@Column.+");
        padraoTemporal = Pattern.compile("@Temporal.+");
        padraoRelacionamento = Pattern.compile("@.+mappedBy.+");
        padraoMany = Pattern.compile("(@ManyToMany.+)|(@OneToMany.+)");
        padraoJoin = Pattern.compile("(@JoinColumn.+)|(@JoinTable.+name=\"(.+)\".+)");
        padraoOne = Pattern.compile("(@OneToOne.+)|(@ManyToOne.+)");
        padraoLista = Pattern.compile(".+<(.+)>.+");
        objeto = Pattern.compile(".+private\\s(.+)\\s.+");

        codigoXhtml = "<?xml version='1.0' encoding='UTF-8' ?>\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\"\n"
                + "xmlns:h=\"http://java.sun.com/jsf/html\"\n"
                + "xmlns:f=\"http://java.sun.com/jsf/core\">\n"
                + "<h:head>\n"
                + "<title>" + classe + "</title>\n"
                + "</h:head>\n"
                + "<h:body>\n"
                + "<h:form>\n"
                + "<h:panelGrid columns=\"2\">";

        for (int c = 0; c < lista.size(); c++) {
            //Codigo
            if (lista.get(c).equals("@Id")) {

                while (nomeCampo.matcher(lista.get(++c)).matches()) {
                    c++;
                }
                if (nomeCampo.matcher(lista.get(++c)).matches()) {
                    pesquisa = nomeCampo.matcher(lista.get(c));
                    if (pesquisa.matches()) {
                        codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ": \"/>\n"
                                + "<h:outputText id=\"" + pesquisa.group(1) + "\" value=\"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\"/>\n";
                    }
                }
                //@column
            } else if (padraoColumn.matcher(lista.get(c)).matches()) {

                //@Temporal
                if ((c + 1 != lista.size() && padraoTemporal.matcher(lista.get(c + 1)).matches()) || (c - 1 != lista.size() && padraoTemporal.matcher(lista.get(c - 1)).matches())) {
                    pesquisa = nomeCampo.matcher(lista.get(c));
                    if (pesquisa.matches()) {
                        codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ": \"/>\n"
                                + "<h:inputText id=\"" + pesquisa.group(1) + "\" value=\"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\">\n"
                                + "<f:convertDateTime pattern=\"dd/MM/yyyy\" ></f:convertDateTime>\n"
                                + "</h:inputText>";
                    }
                } else {

                    //verifica o nome do campo
                    pesquisa = nomeCampo.matcher(lista.get(c));
                    if (pesquisa.matches()) {
                        System.out.println("entrou");
                        //System.out.println(pesquisa.group(1));
                        auxiliar = tamanhoCampo.matcher(lista.get(c));

                        if (auxiliar.matches()) {
                            if (auxiliar.group(1) != null) {
                                if ((Integer.parseInt(auxiliar.group(1)) < 200)) {
                                    codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ":\"/>\n"
                                            + "<h:inputText id=\"" + pesquisa.group(1) + "\" value = \"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\" size =\"" + auxiliar.group(1) + "\"/>\n ";

                                } else {
                                    codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ":\"/>\n"
                                            + "<h:inputTextarea id=\"" + pesquisa.group(1) + "\" value = \"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\"/> \n";
                                }
                            } else if (auxiliar.group(2) != null) {
                                // System.out.println("grupo 1"+auxiliar.group(2));
                                if ((Integer.parseInt(auxiliar.group(2)) < 200)) {
                                    codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ":\"/>\n"
                                            + "<h:inputText id=\"" + pesquisa.group(1) + "\" value = \"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\" size =\"" + auxiliar.group(2) + "\"/>\n ";

                                } else {
                                    codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ":\"/>\n"
                                            + "<h:inputTextarea id=\"" + pesquisa.group(1) + "\" value = \"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\"/> \n";
                                }

                            }
                        } else {
                            codigoXhtml += "<h:outputText value=\"" + pesquisa.group(1) + ":\"/>\n"
                                    + "<h:inputText id=\"" + pesquisa.group(1) + "\" value = \"#{" + classe + "MB." + classe + "." + pesquisa.group(1) + "}\"/>\n ";
                        }
                    }
                    //verifica o tamanho do campo
                    /* pesquisa = tamanhoCampo.matcher(lista.get(c));
                    if (pesquisa.matches()) {
                    if (pesquisa.group(1) != null) {
                    codigoXhtml += " size =\"" + pesquisa.group(1) + "\"/>\n";
                    } else {
                    codigoXhtml += " size =\"" + pesquisa.group(2) + "\"/>\n";
                    }
                    } else {
                    codigoXhtml += "/>\n";
                    }*/
                }
            } else if (padraoRelacionamento.matcher(lista.get(c)).matches()) {
                // System.out.println(lista.get(c));
                //System.out.println(padraoRelacionamento.matcher(lista.get(c)).group(1));
                //@OneToMany ou ManyToMany
            } else if (padraoMany.matcher(lista.get(c)).matches()) {

                while (c + 1 != lista.size()) {
                    c++;
                }
                if (padraoJoin.matcher(lista.get(c)).matches()) {

                    pesquisa = padraoLista.matcher(lista.get(c + 1));
                    if (pesquisa.matches()) {
                        criarTabelaRelacionamento(classe, pesquisa.group(1));
                    }

                    //codigoCtr += lista.get(c + 1) + "\n";
                }
            } else if (padraoOne.matcher(lista.get(c)).matches()) {
                pesquisa = padraoOne.matcher(lista.get(c));

                if (pesquisa.matches()) {
                    //pega o objeto declarado(private Objeto objeto)
                    pesquisa = objeto.matcher(lista.get(c + 2));
                    if (pesquisa.matches()) {
                        //verificar se ja existe metodos declarados

                        if (metodosCtr.contains("getSelect" + pesquisa.group(1)) != true) {
                            codigoXhtml += "<h:outputText id=\"" + pesquisa.group(1) + "\" value=\"" + pesquisa.group(1) + "\"/>\n"
                                    + "<h:selectOneMenu value=\"#{" + classe + "MB." + pesquisa.group(1).substring(0, 1).toLowerCase().concat(pesquisa.group(1).substring(1, pesquisa.group(1).length())) + "Cod}\">\n"
                                    + "<f:selectItems value=\"#{" + classe + "MB.select" + pesquisa.group(1) + "}\"/>\n</h:selectOneMenu>\n";
                            //codigoCtr += lista.get(c + 2);

                            codigoCtr += "private List<" + pesquisa.group(1) + "> lista" + pesquisa.group(1) + ";\n"
                                    + "     private List<SelectItem> select" + pesquisa.group(1) + ";\n"
                                    + "         private int " + pesquisa.group(1).substring(0, 1).toLowerCase().concat(pesquisa.group(1).substring(1, pesquisa.group(1).length())) + "Cod;\n";
                            metodosCtr += "     \npublic List<SelectItem> getSelect" + pesquisa.group(1) + "(){\n"
                                    + "         select" + pesquisa.group(1) + " = new ArrayList<SelectItem>();\n"
                                    + "         lista" + pesquisa.group(1) + " =(List<" + pesquisa.group(1) + ">) dao.buscarTodos(" + pesquisa.group(1) + ".class);\n"
                                    + "         for(" + pesquisa.group(1) + " " + pesquisa.group(1).substring(0, 1).toLowerCase() + " : lista" + pesquisa.group(1) + "){\n"
                                    + "             select" + pesquisa.group(1) + ".add(new SelectItem(" + pesquisa.group(1).substring(0, 1).toLowerCase() + ".get" + buscarNomesObjeto(pesquisa.group(1), "Codigo") + "()," + pesquisa.group(1).substring(0, 1).toLowerCase() + ".get" + buscarNomesObjeto(pesquisa.group(1), "String") + "()));\n"
                                    + "         \n}return select" + pesquisa.group(1) + ";\n}\n\n";
                            getsSets("List<" + pesquisa.group(1) + ">", "lista" + pesquisa.group(1));
                            sets("List<SelectItem>", "select" + pesquisa.group(1));
                            getsSets("int", pesquisa.group(1).substring(0, 1).toLowerCase().concat(pesquisa.group(1).substring(1, pesquisa.group(1).length())) + "Cod");
                            gravar += classe + ".set" + buscarNomeObjeto(classe.substring(0, 1).toUpperCase().concat(classe.substring(1, classe.length())), pesquisa.group(1)) + "((" + pesquisa.group(1) + ")dao.buscar(" + pesquisa.group(1) + ".class," + pesquisa.group(1).substring(0, 1).toLowerCase().concat(pesquisa.group(1).substring(1, pesquisa.group(1).length())) + "Cod));\n";

                        } else {
                            // codigoCtr += lista.get(c + 2);
                        }
                    }
                }
            }
        }

        codigoXhtml
                += "<h:commandButton value=\"Gravar\" actionListener=\"#{" + classe + "MB.gravar}\"/>\n"
                + "<h:commandButton value=\"Alterar\" actionListener=\"#{" + classe + "MB.alterarRegistro}\"/>\n"
                + "</h:panelGrid >\n";
        criarTabela(
                classe);
        codigoXhtml += "</h:form>\n"
                + "</h:body>\n"
                + "</html>\n";

        gravarDisco(
                codigoXhtml, System.getProperty("user.dir") + File.separator + "web" + File.separator + classe + ".xhtml");

    }

    private void criarTabela(String classe) throws FileNotFoundException, IOException {

        listaTabela = new ArrayList<String>();

        for (File f : classes) {
            if (f.getName().equals(nomeClasse + ".java")) {
                br = new BufferedReader(new FileReader(f));
                bufSaida = new StringBuffer();

                while ((linha = br.readLine()) != null) {
                    if (padrao.matcher(linha).matches()) {
                        String removeEspaco = "";

                        for (char c : linha.toCharArray()) {
                            if (c != ' ') {
                                removeEspaco += c;
                            }
                        }
                        listaTabela.add(removeEspaco);
                        //System.out.println(removeEspaco);
                    }
                }
            }
        }

        codigoCtr += "private List<" + nomeClasse + "> lista" + nomeClasse + " = new ArrayList<" + nomeClasse + ">();\n";

        codigoXhtml += "<h:dataTable value=\"#{" + classe + "MB.lista" + nomeClasse + "}\" var=\"" + classe + "\">\n";

        for (String str : listaTabela) {
            if (padraoColumn.matcher(str).matches()) {
                pesquisa = nomeCampo.matcher(str);

                if (pesquisa.matches()) {
                    codigoXhtml += "<h:column>\n<f:facet name=\"header\">" + pesquisa.group(1) + "</f:facet>\n"
                            + "<h:outputText value=\"#{" + classe + "." + pesquisa.group(1) + "}\"/>\n"
                            + "</h:column>\n";
                    //  System.out.println(pesquisa.group(1));
                }
            }
        }
        codigoXhtml += "<h:column>\n<f:facet name=\"header\">Remover</f:facet>\n"
                + "<h:commandLink value=\"Remover\" actionListener=\"#{" + classe + "MB.remover}\"/>"
                + "</h:column>\n"
                + "<h:column>\n<f:facet name=\"header\">Alterar</f:facet>\n"
                + "<h:commandLink value=\"Alterar\" actionListener=\"#{" + classe + "MB.alterar}\"/>"
                + "</h:column>\n"
                + "</h:dataTable>\n";
        metodosCtr += "     public void remover(ActionEvent evt){\n"
                + classe + "= (" + nomeClasse + ") FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(\"" + classe + "\");"
                + "dao.remover(" + classe + ");\n"
                + "lista" + nomeClasse + "=(List<" + nomeClasse + ">) dao.buscarTodos(" + nomeClasse + ".class);\n"
                + classe + " = new " + nomeClasse + "();\n}\n\n"
                + "     public void alterar(ActionEvent evt){\n"
                + classe + "= (" + nomeClasse + ") FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(\"" + classe + "\");\n}\n\n"
                + "     public void alterarRegistro(ActionEvent evt){\n"
                + "         dao.alterar(" + classe + ");\n"
                + classe + " = new " + nomeClasse + "();\n}\n";
        gravar += "         dao.gravar(" + classe + ");\n"
                + "         lista" + nomeClasse + "=(List<" + nomeClasse + ">) dao.buscarTodos(" + nomeClasse + ".class);\n"
                + classe + " = new " + nomeClasse + "();\n";

    }

    private void criarTabelaRelacionamento(String classe, String objeto) throws FileNotFoundException, IOException {

        listaTabela = new ArrayList<String>();

        for (File f : classes) {
            if (f.getName().equals(objeto + ".java")) {
                br = new BufferedReader(new FileReader(f));
                bufSaida = new StringBuffer();

                while ((linha = br.readLine()) != null) {
                    if (padrao.matcher(linha).matches()) {
                        String removeEspaco = "";

                        for (char c : linha.toCharArray()) {
                            if (c != ' ') {
                                removeEspaco += c;

                            }
                        }
                        listaTabela.add(removeEspaco);
                        // System.out.println(removeEspaco);

                    }

                }
            }
        }

        codigoCtr += "private List<" + objeto + "> lista" + objeto + " = new ArrayList<" + objeto + ">();\n"
                + "private " + objeto + " " + objeto.toLowerCase() + ";\n";

        codigoXhtml += "<h:dataTable value=\"#{" + classe + "MB.lista" + objeto + "}\" var=\"" + objeto.toLowerCase() + "\">\n";

        for (String str : listaTabela) {
            if (padraoColumn.matcher(str).matches()) {
                pesquisa = nomeCampo.matcher(str);

                if (pesquisa.matches()) {
                    codigoXhtml += "<h:column>\n<f:facet name=\"header\">" + pesquisa.group(1) + "</f:facet>\n"
                            + "<h:outputText value=\"#{" + objeto.toLowerCase() + "." + pesquisa.group(1) + "}\"/>\n"
                            + "</h:column>\n";
                    //  System.out.println(pesquisa.group(1));

                }
            }
        }
        codigoXhtml += "<h:column>\n<f:facet name=\"header\">Remover</f:facet>\n"
                + "<h:commandLink value=\"Remover\" actionListener=\"#{" + classe + "MB.remover" + objeto + "}\"/>"
                + "</h:column>\n"
                + "</h:dataTable>\n";
        metodosCtr += "public void remover" + objeto + "(ActionEvent evt){\n"
                + objeto.toLowerCase() + "= (" + objeto + ") FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(\"" + objeto.toLowerCase() + "\");\n"
                + "lista" + objeto + ".remove(" + objeto.toLowerCase() + ");\n}\n\n";

    }

    private void criaCtr(String nome) {
        metodosCtr = "";
        getsSets = "";
        gravar = "";
        codigoCtr = "package ctr;\n\n"
                + "import model.*;\n"
                + "import java.io.Serializable;\n"
                + "import javax.faces.model.SelectItem;\n"
                + "import java.util.List;\n"
                + "import java.util.ArrayList;\n"
                + "import javax.faces.event.ActionEvent;\n"
                + "import javax.faces.context.FacesContext;\n"
                + "import dao.Dao;\n"
                + "import javax.faces.bean.*;\n\n"
                + "@ManagedBean\n"
                + "@ViewScoped\n"
                + "public class " + nomeClasse + "MB implements Serializable{\n\n"
                + "     private " + nomeClasse + " " + nome + ";\n"
                + "     private Dao dao = new Dao();\n";
        getsSets(
                nomeClasse, nome);
        getsSets(
                "Dao", "dao");
        getsSets(
                "List<" + nomeClasse + ">", "lista" + nomeClasse);
        metodosCtr += "     public " + nomeClasse + "MB(){\n"
                + nome + " = new " + nomeClasse + "();\n"
                + "             lista" + nomeClasse + " = new ArrayList<" + nomeClasse + ">();\n"
                + "             lista" + nomeClasse + " = (List<" + nomeClasse + ">) dao.buscarTodos(" + nomeClasse + ".class);\n"
                + "     }\n\n";
        gravar += "public void gravar(ActionEvent evt){\n";
        // gravarDisco(codigoCtr, System.getProperty("user.dir") + File.separator + "src" + File.separator + "java" + File.separator + "ctr" + File.separator + pesquisa.group(1) + "MB.java");

    } //Encontra o nome do projeto para criar o ctr

    private void nomeCtr() {
        //nome do projeto
        nomeCtr = "";

        for (int i = System.getProperty("user.dir").length() - 1; i
                >= 0; i--) {
            if (System.getProperty("user.dir").charAt(i) != File.separatorChar) {
                nomeCtr += System.getProperty("user.dir").charAt(i);

            } else {
                break;

            }
        }
        projeto = new StringBuffer(nomeCtr);
        nomeCtr = projeto.reverse().toString() + "Ctr";
        projeto.setCharAt(0, Character.toLowerCase(nomeCtr.charAt(0)));

    }

    private void getsSets(String tipoObjeto, String nome) {
        getsSets += "    public void set" + nome.substring(0, 1).toUpperCase().concat(nome.substring(1, nome.length())) + "(" + tipoObjeto + " " + nome + "){\n"
                + "        this." + nome + " = " + nome + ";\n}\n\n"
                + "    public " + tipoObjeto + " get" + nome.substring(0, 1).toUpperCase().concat(nome.substring(1, nome.length())) + "(){\n"
                + "        return this." + nome + ";\n}\n";

    }

    private void sets(String tipoObjeto, String nome) {
        getsSets += "    public void set" + nome.substring(0, 1).toUpperCase().concat(nome.substring(1, nome.length())) + "(" + tipoObjeto + " " + nome + "){\n"
                + "        this." + nome + " = " + nome + ";\n}\n";

    }

    private void criarDao() {
        String dao = "package dao;\n\n"
                + "import java.util.List;\n"
                + "import javax.persistence.EntityManager;\n"
                + "import javax.persistence.EntityManagerFactory;\n"
                + "import javax.persistence.Persistence;\n\n"
                + "    public class Dao {\n   "
                + "        private EntityManagerFactory emf;\n"
                + "        private EntityManager em;\n\n"
                + "    public Dao() {\n"
                + "        emf = Persistence.createEntityManagerFactory(\"" + System.getProperty("dir") + "\");\n"
                + "        em = emf.createEntityManager();\n"
                + "    }\n\n"
                + "    public void gravar(Object objeto) {\n"
                + "        em.getTransaction().begin();\n"
                + "        em.persist(objeto);\n"
                + "        em.getTransaction().commit();\n"
                + "    }\n\n"
                + "    public void alterar(Object objeto) {\n"
                + "        em.getTransaction().begin();\n"
                + "        em.merge(objeto);\n"
                + "        em.getTransaction().commit();\n"
                + "    }\n\n"
                + "    public void remover(Object objeto) {\n"
                + "        em.getTransaction().begin();\n"
                + "        em.remove(objeto);\n"
                + "        em.getTransaction().commit();\n "
                + "   }\n\n"
                + "    public Object buscar(Object objeto, int id) {\n"
                + "        return em.find(objeto.getClass(), id);\n"
                + "    }"
                + "\n\n"
                + "    public List<?> buscarTodos(Class classe){"
                + "        return em.createQuery(\"From \"+classe.getName()).getResultList();\n"
                + "    }\n"
                + "}";

        File arquivo = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "java" + File.separator + "dao" + File.separator);
        arquivo.mkdir();
        gravarDisco(
                dao, arquivo.toString() + File.separator + "Dao.java");

    }

    private String buscarNomeObjeto(String classe, String objeto) throws FileNotFoundException, IOException {
        nomeObjeto = Pattern.compile(".+private\\s" + objeto + "\\s(.+).+");
        String retorno = "";
        //  System.out.println(classe);
        //System.out.println(objeto);

        for (File f : classes) {
            if (f.getName().equals(classe + ".java")) {
                br = new BufferedReader(new FileReader(f));
                bufSaida = new StringBuffer();

                while ((linha = br.readLine()) != null) {
                    auxiliar = nomeObjeto.matcher(linha);
                    // System.out.println(linha);
                    //System.out.println(">>> =="+auxiliar.matches());
                    if (auxiliar.matches()) {
                        //System.out.println(">>>" + auxiliar.group(1));
                        retorno = auxiliar.group(1).substring(0, 1).toUpperCase().concat(auxiliar.group(1).substring(1, auxiliar.group(1).length()));
                        //System.out.println("=============================" + auxiliar.group(1).substring(0, 1).toUpperCase().concat(auxiliar.group(1).substring(1, auxiliar.group(1).length())));
                        break;
                    }
                }
            }
        }
        return retorno;
    }

    private String buscarNomesObjeto(String classe, String objeto) throws IOException {
        int cont = 0;
        String retorno = "";
        System.out.println(objeto);
        if (objeto.equals("Codigo")) {
            nomeObjeto = Pattern.compile("(.+private\\sInteger\\s(.+).+)|(.+private\\sint\\s(.+).+)");
        } else {
            nomeObjeto = Pattern.compile(".+private\\s" + objeto + "\\s(.+).+");
        }

        for (File f : classes) {

            if (f.getName().equals(classe + ".java")) {
                br = new BufferedReader(new FileReader(f));
                bufSaida = new StringBuffer();

                while ((linha = br.readLine()) != null) {
                    auxiliar = nomeObjeto.matcher(linha);

                    if (auxiliar.matches()) {
                        if (objeto.equals("Codigo")) {
                            if (auxiliar.group(1) != null) {
                                System.out.println("group" + auxiliar.group(2));
                                retorno = auxiliar.group(2).substring(0, 1).toUpperCase().concat(auxiliar.group(2).substring(1, auxiliar.group(2).length()));
                                System.out.println("=============================" + auxiliar.group(1).substring(0, 1).toUpperCase().concat(auxiliar.group(1).substring(1, auxiliar.group(1).length())));
                                break;
                            } else {
                                System.out.println(auxiliar.group());
                                retorno = auxiliar.group(4).substring(0, 1).toUpperCase().concat(auxiliar.group(4).substring(1, auxiliar.group(4).length()));
                                System.out.println("=============================" + auxiliar.group(4).substring(0, 1).toUpperCase().concat(auxiliar.group(4).substring(1, auxiliar.group(4).length())));
                                break;
                            }
                        } else {
                            //   System.out.println(auxiliar.group());
                            retorno = auxiliar.group(1).substring(0, 1).toUpperCase().concat(auxiliar.group(1).substring(1, auxiliar.group(1).length()));
                            System.out.println("=============================" + auxiliar.group(1).substring(0, 1).toUpperCase().concat(auxiliar.group(1).substring(1, auxiliar.group(1).length())));
                            break;
                        }
                    }
                }
            }

        }
        return retorno;
    }
}
