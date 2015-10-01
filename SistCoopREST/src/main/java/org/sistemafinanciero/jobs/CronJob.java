package org.sistemafinanciero.jobs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Named;

import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.mail.EmailSessionBean;
import org.sistemafinanciero.service.nt.CuentaBancariaServiceNT;
import org.sistemafinanciero.util.DateUtils;

@Named
@Singleton
public class CronJob {

    @EJB
    private CuentaBancariaServiceNT ctaBancServiceNT;

    @EJB
    private EmailSessionBean emailSessionBean;

    @Schedule(dayOfMonth = "1", hour = "3", minute = "0", second = "0", persistent = false)
    public void run() {
        // JobOperator jobOperator = BatchRuntime.getJobOperator();
        // Long executionId = jobOperator.start("SistcoopJob", new
        // Properties());
        // JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        // System.out.println("BatchStatus : " + jobExecution.getBatchStatus());
        // System.out.println("Enviando emails a los clientes");
        sendEmail();
    }

    private void sendEmail() {
        List<CuentaBancariaView> listCtas = ctaBancServiceNT.findAllView();
        for (CuentaBancariaView cuentaBancariaView : listCtas) {
            if (!cuentaBancariaView.getEstadoCuenta().equals(EstadoCuentaBancaria.INACTIVO)) {
                if (!cuentaBancariaView.getTipoCuenta().equals(TipoCuentaBancaria.PLAZO_FIJO)) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    calendar.add(Calendar.MONTH, -1);
                    Date systemDate = Calendar.getInstance().getTime();
                    Date desde = DateUtils.getDateIn00Time(calendar.getTime());
                    Date hasta = systemDate;

                    CuentaBancariaView ctaBancView = ctaBancServiceNT.findById(cuentaBancariaView
                            .getIdCuentaBancaria());
                    BigInteger idCtaBanc = ctaBancView.getIdCuentaBancaria();

                    Set<Titular> titulares = ctaBancServiceNT.getTitulares(idCtaBanc, true);
                    List<String> emails = new ArrayList<String>();
                    for (Titular titular : titulares) {
                        PersonaNatural personaNatural = titular.getPersonaNatural();
                        String emailTitular = personaNatural.getEmail();
                        if (emailTitular != null)
                            emails.add(emailTitular);
                    }
                    List<EstadocuentaBancariaView> list = ctaBancServiceNT.getEstadoCuenta(idCtaBanc, desde,
                            hasta, true);
                    emailSessionBean.sendMailPdf(ctaBancView, list, emails, desde, hasta);
                }
            }
        }
    }

}
