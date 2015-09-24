package org.sistemafinanciero.jobs;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.batch.api.chunk.AbstractItemReader;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@Named
@Dependent
public class Reader_EnviarEstadoCuentaEmailChunk extends AbstractItemReader {

    ResultSet rs;

    @Override
    public void open(Serializable checkpoint) throws Exception {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement statement = connection.prepareStatement(getSQL());
                rs = statement.executeQuery();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public void close() throws Exception {
        try {
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            InitialContext context = new InitialContext();
            DataSource dataSource = (DataSource) context.lookup("java:jboss/datasources/OracleDS");
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    protected String getSQL() {
        return "SELECT CB.NUMERO_CUENTA, PN.EMAIL FROM CUENTA_BANCARIA CB INNER JOIN SOCIO SO ON SO.ID_SOCIO = CB.ID_SOCIO INNER JOIN PERSONA_NATURAL PN ON PN.ID_PERSONA_NATURAL = SO.ID_PERSONA_NATURAL WHERE PN.EMAIL IS NOT NULL UNION SELECT CB.NUMERO_CUENTA, PJ.EMAIL FROM CUENTA_BANCARIA CB INNER JOIN SOCIO SO ON SO.ID_SOCIO = CB.ID_SOCIO INNER JOIN PERSONA_JURIDICA PJ ON PJ.ID_PERSONA_JURIDICA = SO.ID_PERSONA_JURIDICA WHERE PJ.EMAIL IS NOT NULL";
    }

    @Override
    public Object readItem() throws Exception {
        if (rs.next()) {
            CuentaBancariaJob cta = new CuentaBancariaJob();
            cta.setNumeroCuenta(rs.getString(1));
            cta.setEmail(rs.getString(2));
            return cta;
        }
        return null;
    }

}
