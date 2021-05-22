package com.cognizant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * 
 * @author POD5
 * Test class for AuditManagementAuthenticationApplication
 *
 */
@SpringBootTest
class AuditManagementAuthenticationApplicationTests {

	@Mock
	AuditManagementAuthenticationApplication application;

	@Test
	public void contextLoads() {
		assertNotNull(application);
	}

}
