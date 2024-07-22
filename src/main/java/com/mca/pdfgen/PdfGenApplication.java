package com.mca.pdfgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.mca.pdfgen.bootstrap.AppBootstrap;

// TODO: SIVA - Do we need to add all other packages as well in the scan list of packages
@ComponentScan(basePackages = {"com.mca.pdfgen, com.mca.pdfgen.dataaccess, com.mca.pdfgen.utils"})
@SpringBootApplication
public class PdfGenApplication 
{
	public static void main(final String[] args) 
	{
		AppBootstrap.initialize();
		
		SpringApplication.run(PdfGenApplication.class, args);
	}
}
