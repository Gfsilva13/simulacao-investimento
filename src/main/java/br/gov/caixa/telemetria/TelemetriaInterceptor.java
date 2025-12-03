package br.gov.caixa.telemetria;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@MedirTelemetria
@Interceptor
public class TelemetriaInterceptor {

    @Inject
    TelemetriaMetrics metrics;

    @AroundInvoke
    public Object medir(InvocationContext ctx) throws Exception {
        long inicio = System.currentTimeMillis();
        Object resultado = ctx.proceed();
        long fim = System.currentTimeMillis();
        String servico = ctx.getMethod().getName();
        metrics.registrarChamada(servico, fim - inicio);
        return resultado;
    }
}

