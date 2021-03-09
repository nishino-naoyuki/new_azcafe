package jp.ac.asojuku.azcafe.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.azcafe.service.AssignmentService;
import jp.ac.asojuku.azcafe.util.FileUtils;


@RestController
@RequestMapping(value= {"/dummy"})
public class DummyController {
	@Autowired
	AssignmentService assignmentService;

	@RequestMapping(value = "/init", method = {RequestMethod.GET })
	public Object getImage() throws IOException {
		List<String> strList = FileUtils.readLine("C:\\Users\\nishino\\Desktop\\testcase.csv");
		
		for(String line : strList ) {
			String[] data = line.split(";");
			String name = data[0];
			//input,outputデータを読み込む
			String input = FileUtils.read(new File("c:\\answer\\input",data[1]));
			String output = FileUtils.read(new File("c:\\answer\\output",data[2]));
			//System.out.println("title="+name);
			assignmentService.addTestCase(name, input, output);
		}
		
		return "OK";
	}
	
}
