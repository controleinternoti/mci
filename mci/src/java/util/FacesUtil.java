/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author AJ Desenvolvimento
 */
public class FacesUtil {

    public static void addInfoMessage(String message1, String message2) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,message1,message2));
    }
    public static void addErrorMessage(String message1, String message2) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message1, message2));
    }
    public static void addWarnMessage(String message1, String message2) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, message1, message2));
    }
    public static void addFatalMessage(String message1, String message2) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_FATAL, message1, message2));
    }
    public static String converterNumeroMes(Integer mes) {
        String retorno = null ;
        switch(mes){
            case 1:
               retorno = "Jan";
               break;
            case 2:
               retorno = "Fev";
               break;
            case 3:
               retorno = "Mar";
               break;
            case 4:
               retorno = "Abr";
               break;
            case 5:
               retorno = "Maio";
               break;
            case 6:
               retorno = "Jun";
               break;
            case 7:
               retorno = "Jul";
               break;
            case 8:
               retorno = "Ago";
               break;
            case 9:
               retorno = "Set";
               break;
            case 10:
               retorno = "Out";
               break;
            case 11:
               retorno = "Nov";
               break;
            case 12:
               retorno = "Dez";
               break;
        }
        return retorno;
    }
   
    

}
