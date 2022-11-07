package org.redhat;

import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import io.quarkus.runtime.annotations.RegisterForReflection;

@ApplicationScoped
@Named("RegistrationProcessor")
@RegisterForReflection
public class RegistrationProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		final Object body = exchange.getIn().getBody();
		Log.info("Processing document ...");
		if (body instanceof org.bson.Document) {
			org.bson.Document reg = (org.bson.Document) body;
			final String nick = reg.getString("nick");
			if (nick == null || nick.length() == 0) {
				reg.put("nick", exchange.getIn().getHeader("x-redhat-nick"));
				exchange.getIn().setHeader("CamelHttpResponseCode", 200);
				Log.info("Setting nick.");
			} else if (!nick.equals(exchange.getIn().getHeader("x-redhat-nick"))) {
				final String email = reg.getString("email");
				Log.error("Nick for email " + email + " was set!");
				exchange.getIn().setHeader("CamelHttpResponseCode", 401);
				exchange.getIn().setBody("{\"auth\":\"failed\"}");
			} else {
				Log.info("Nick is valid.");
				final String id = reg.getObjectId("_id").toHexString();
				exchange.getIn().setBody("{\"id\":\"" + id + "\"}");
			}
		}
	}
}
