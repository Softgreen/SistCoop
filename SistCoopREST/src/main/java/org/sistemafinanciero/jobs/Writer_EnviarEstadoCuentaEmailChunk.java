package org.sistemafinanciero.jobs;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named
@Dependent
public class Writer_EnviarEstadoCuentaEmailChunk extends AbstractItemWriter {

    @Override
    public void writeItems(List<Object> items) throws Exception {
        System.out.println(items);
    }

}
