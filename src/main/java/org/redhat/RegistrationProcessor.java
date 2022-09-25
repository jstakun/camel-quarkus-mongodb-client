package org.redhat;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("RegistrationProcessor")
@RegisterForReflection
public class RegistrationProcessor implements Processor {

	private static final Logger LOG = Logger.getLogger(RegistrationProcessor.class.getName());
	
	@Override
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		LOG.info("Processing ...");
		if (body instanceof org.bson.Document) {
			org.bson.Document reg = (org.bson.Document) body;
			final String nick = reg.getString("nick");
			if (nick == null || nick.length() == 0) {
				reg.put("nick", exchange.getIn().getHeader("x-redhat-nick"));
				exchange.getIn().setHeader("CamelHttpResponseCode", 200);
				LOG.info("Setting nick");
			} else if (!nick.equals(exchange.getIn().getHeader("x-redhat-nick"))) {
				LOG.severe("Nick is set!");
				exchange.getIn().setHeader("CamelHttpResponseCode", 401);
				exchange.getIn().setBody("{\"auth\":\"failed\"}");
			} else {
				LOG.info("Nick is ok");
				exchange.getIn().setBody("{\"auth\":\"ok\"}");
			}
		}
	}
}
