package ar.edu.utn.dds.k3003.config.logging;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class FeignTraceIdInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {
    String traceId = MDC.get("traceId");
    if (traceId != null && !traceId.isBlank()) {
      template.header(RequestLoggingFilter.TRACE_ID_HEADER, traceId);
    }
  }
}
