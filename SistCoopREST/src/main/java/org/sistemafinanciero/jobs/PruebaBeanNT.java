package org.sistemafinanciero.jobs;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;

@Named
@Stateless
@Local(PruebaNT.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PruebaBeanNT implements PruebaNT {

    private String prueba;

    public String getPrueba() {
        return prueba;
    }

    public void setPrueba(String prueba) {
        this.prueba = prueba;
    }

}
