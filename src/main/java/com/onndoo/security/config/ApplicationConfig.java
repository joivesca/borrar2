package com.onndoo.security.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@DatabaseIdentityStoreDefinition(
		
		dataSourceLookup = "${'java:comp/env/jdbc/H2DB'}", 
		callerQuery = "#{'select password from caller where name = ?'}", 
		groupsQuery = "select group_name from caller_groups where caller_name = ?", 
		hashAlgorithm = Pbkdf2PasswordHash.class, 
		priorityExpression = "#{100}", 
		hashAlgorithmParameters = {
				"Pbkdf2PasswordHash.Iterations=3072", 
				"${applicationConfig.dyna}" 
		} // just for test / example
)
@ApplicationScoped
@Named
public class ApplicationConfig {

	public String[] getDyna() {
		return new String[] { "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
				"Pbkdf2PasswordHash.SaltSizeBytes=64" };
	}
}
