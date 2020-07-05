package com.daliu.sample;

import com.light.generator.GeneratorStarter;
import com.light.generator.GeneratorStarter.OutputType;

public class CodeGenerator {

	public static void main(String[] args) throws ClassNotFoundException {
		
		String basePackage = "com.daliu.sample.model";
		
		String[] entities = new String[] {"Course", "Student", "StudentProfile", "Teacher"};
		
		OutputType output = OutputType.file;
		
		boolean override = false;
		boolean isLongTypeId = true;
		
		GeneratorStarter.instance().generateCode(basePackage , entities, output, override, isLongTypeId);
	}
}
