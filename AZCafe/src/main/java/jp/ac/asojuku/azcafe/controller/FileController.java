package jp.ac.asojuku.azcafe.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jp.ac.asojuku.azcafe.config.AZCafeConfig;


@RestController
public class FileController {
	@Autowired
	ResourceLoader resourceLoader;
	@Autowired
	AZCafeConfig config;
	
	@ResponseBody
	@RequestMapping(value = "/getImage/{id}", method = {RequestMethod.GET })
	public HttpEntity<byte[]> getImage(@PathVariable String id) throws IOException {
		
		// リソースファイルを読み込み
		String imgdir = config.getAvaterbasedir();
		//Resource resource = resourceLoader.getResource("classpath:" + "/static/image/" + id);
		Resource resource = resourceLoader.getResource("file:C:\\img\\" + id);
		InputStream image = resource.getInputStream();
		
		// byteへ変換
		byte[] b = IOUtils.toByteArray(image);
		
		// レスポンスデータとして返却
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);		
		headers.setContentLength(b.length);
		return new HttpEntity<byte[]>(b, headers);
	}
}
