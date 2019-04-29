package com.anbang.p2p;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.anbang.p2p.conf.FilePathConfig;
import com.anbang.p2p.cqrs.c.repository.SingletonEntityFactoryImpl;
import com.anbang.p2p.cqrs.c.service.disruptor.CoreSnapshotFactory;
import com.anbang.p2p.cqrs.c.service.disruptor.ProcessCoreCommandEventHandler;
import com.anbang.p2p.cqrs.c.service.disruptor.SnapshotJsonUtil;
import com.anbang.p2p.init.InitProcessor;
import com.dml.users.UserSessionsManager;
import com.highto.framework.ddd.SingletonEntityRepository;

import javax.servlet.MultipartConfigElement;

@SpringBootApplication
@EnableScheduling
public class P2PFinancialApplication {

	@Autowired
	private SnapshotJsonUtil snapshotJsonUtil;

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private CoreSnapshotFactory coreSnapshotFactory;

	@Autowired
	private FilePathConfig filePathConfig;

	@Bean
	public HttpClient httpClient() {
		HttpClient client = new HttpClient();
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	@Bean
	public HttpClient sslHttpClient() {

		HttpClient client = new HttpClient(new SslContextFactory());
		try {
			client.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;

	}

	@Bean
	public SingletonEntityRepository singletonEntityRepository() {
		SingletonEntityRepository singletonEntityRepository = new SingletonEntityRepository();
		singletonEntityRepository.setEntityFactory(new SingletonEntityFactoryImpl());
		return singletonEntityRepository;
	}

	@Bean
	public UserSessionsManager userSessionsManager() {
		return new UserSessionsManager();
	}

	@Bean
	public ProcessCoreCommandEventHandler processCoreCommandEventHandler() {
		return new ProcessCoreCommandEventHandler(coreSnapshotFactory, snapshotJsonUtil, filePathConfig);
	}

	@Bean
	public InitProcessor initProcessor() {
		InitProcessor initProcessor = new InitProcessor(httpClient(), sslHttpClient(), snapshotJsonUtil,
				processCoreCommandEventHandler(), singletonEntityRepository(), applicationContext);
		initProcessor.init();
		return initProcessor;
	}

	public static void main(String[] args) {
		SpringApplication.run(P2PFinancialApplication.class, args);
	}
}
