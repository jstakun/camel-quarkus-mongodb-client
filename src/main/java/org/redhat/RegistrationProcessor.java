package org.redhat;

import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.bson.types.ObjectId;

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
			//object exists in database
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
				final Object _id = reg.get("_id");
				String id;
				if (_id instanceof ObjectId) {
					id = ((ObjectId)_id).toHexString();
				} else {
					id = _id.toString();
				}
				exchange.getIn().setBody("{\"id\":\"" + id + "\"}");
			}
		} else if (body == null) {
			//object will be added to database
			final String email = (String)exchange.getIn().getHeader("x-redhat-email");
			final String nick = (String)exchange.getIn().getHeader("x-redhat-nick");
			final String reg = "{\"email\":\"" + email + "\",\"nick\":\"" + nick + "\"}";
			Log.info("Inserting new user " + email);
			exchange.getIn().setBody(reg);
			exchange.getIn().setHeader("CamelHttpResponseCode", 200);
		}
	}
}
