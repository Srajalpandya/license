package com.emailchecker.controller;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailchecker.exception.DNSLookupException;
@RestController
@RequestMapping("/check")
public class EmailCheckerController {
	
	@GetMapping("/mail-cheker/{email}")
	 public static boolean verifyEmail(@PathVariable String email) {
	        try {
	            // Extract domain from email
	        	System.out.println("this is emailchecker service");
	            String domain = email.substring(email.indexOf("@") + 1);

	            // Get MX records of the domain
	            String mxRecord = getMXRecord(domain);
	            if (mxRecord == null) {
	                System.out.println("No MX record found for domain: " + domain);
	                return false;
	            }

	            // Connect to SMTP server and verify email
	            return verifyEmailWithSMTP(mxRecord, email);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	

	    private static String getMXRecord(String domain)throws DNSLookupException {
	        try {
	            // DNS lookup for MX records
	            Hashtable<String, String> env = new Hashtable<>();
	            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
	            DirContext ctx = new InitialDirContext(env);
	            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});

	            Attribute attr = attrs.get("MX");
	            if (attr == null) 
	            	throw new DNSLookupException("No MX Record for Domain "+domain);

	            // Get the lowest priority MX record
	            String[] mxRecords = new String[attr.size()];
	            for (int i = 0; i < attr.size(); i++) {
	                mxRecords[i] = attr.get(i).toString().split(" ")[1];
	            }
	            Arrays.sort(mxRecords, Comparator.naturalOrder());
	            return mxRecords[0]; // Return the lowest priority MX record
	        } catch (Exception e) {
	            throw new DNSLookupException("Error looking up MX records for domain: " + domain, e);
	        }
	    }

	    private static boolean verifyEmailWithSMTP(String mxRecord, String email) {
	        try (java.net.Socket socket = new java.net.Socket(mxRecord, 25)) {
	            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
	            java.io.PrintWriter out = new java.io.PrintWriter(socket.getOutputStream(), true);

	            // Read the server's welcome message
	            String response = in.readLine();
	            System.out.println("Server: " + response);
	            if (!response.startsWith("220")) {
	                return false; // SMTP server not ready
	            }

	            // Send EHLO command
	            out.println("EHLO example.com");
	            response = in.readLine();
	            System.out.println("Server: " + response);
	            if (!response.startsWith("250")) {
	                return false; // EHLO command failed
	            }

	            // Read additional EHLO responses (if any)
	            while (response.startsWith("250-")) {
	                response = in.readLine();
	                System.out.println("Server: " + response);
	            }

	            // Send MAIL FROM command
	            out.println("MAIL FROM:<test@example.com>");
	            response = in.readLine();
	            System.out.println("Server: " + response);
	            if (!response.startsWith("250")) {
	                return false; // MAIL FROM command failed
	            }

	            // Send RCPT TO command to verify the email address
	            out.println("RCPT TO:<" + email + ">");
	            response = in.readLine();
	            System.out.println("Server: " + response);

	            // Check if the email address is valid
	            if (response.startsWith("250")) {
	                return true; // Email address is valid
	            } else {
	                return false; // Email address is invalid
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }

} 
	    
}
