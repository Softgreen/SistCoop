package org.sistemafinanciero.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, dias); // numero de días a añadir, o
                                                  // restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos
                                   // días añadidos
    }
    
    public static Date sumarRestarHorasFecha(Date fecha, int horas) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.HOUR_OF_DAY, horas); // numero de días a añadir, o
                                                  // restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos
                                   // días añadidos
    }
    
    public static Date sumarRestarMinutosFecha(Date fecha, int minutos) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); // Configuramos la fecha que se recibe
        calendar.add(Calendar.MINUTE, minutos); // numero de días a añadir, o
                                                  // restar en caso de días<0
        return calendar.getTime(); // Devuelve el objeto Date con los nuevos
                                   // días añadidos
    }

    public static Date getDateIn00Time(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int getDayOfMoth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

}
