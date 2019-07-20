/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author aj
 */
public class FormatNumeroUtil {

    public static String formatDoubleToString(Double numero) {
        String retorno;
        retorno = NumberFormat.getCurrencyInstance().format(numero);
        return retorno;
    }
    public static String formatFloatToString(Float numero) {
        String retorno;
        retorno = NumberFormat.getCurrencyInstance().format(numero);
        return retorno;
    }
    public static String formatToString(String numero) {
        String retorno;
        retorno = NumberFormat.getCurrencyInstance().format(Double.parseDouble(numero));
        return retorno;
    }
    

}
