package jp.ac.asojuku.azcafe;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

	/* (非 Javadoc)
	 * warファイルを作成するのに必要らしい
	 * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AzCafeApplication.class);
	}
}
