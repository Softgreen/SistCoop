package org.sistemafinanciero.jobs;

import javax.batch.api.chunk.ItemProcessor;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named
@Dependent
public class Processor_EnviarEstadoCuentaEmailChunk implements ItemProcessor {

    @Override
    public Object processItem(Object item) throws Exception {
        return item;
    }
}
