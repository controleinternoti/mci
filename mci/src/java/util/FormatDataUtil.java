/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author AJ Desenvolvimento
 */
public class FormatDataUtil {

     public static Date converterData(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();
        try {
            dt = df.parse(formatarDataString(data));
        } catch (ParseException ex) {
            Logger.getLogger(FormatDataUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dt;
    }

    public static String formatarDataString(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dt = "";
        dt = df.format(data);
        return dt;
    }
    public static String formatarDataStringPadrao(Date data) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String dt = "";
       
        dt = df.format(data);
        return dt;
    }


}
