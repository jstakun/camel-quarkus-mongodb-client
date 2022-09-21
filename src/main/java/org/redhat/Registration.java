package org.redhat;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Registration {
      public String email;
      public String nick;
      public Object _id;
      
      public String getEmail() {
    	   return email;
      }
      
      public String getNick() {
    	  return nick;
      }
      
      public Object get_id() {
    	  return _id;
      }
}
