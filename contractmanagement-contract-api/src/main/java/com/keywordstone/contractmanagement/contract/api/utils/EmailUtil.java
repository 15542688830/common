package com.keywordstone.contractmanagement.contract.api.utils;

import com.keywordstone.contractmanagement.contract.api.config.EmailConfig;
import com.keywordstone.contractmanagement.contract.api.module.EmailVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;


@Component
@Slf4j
@Data
public class EmailUtil {
	private static LinkedBlockingQueue<EmailVO> emailQueue = new LinkedBlockingQueue<EmailVO>();

	// 默认设置
	@Autowired
	private EmailConfig emailConfig;

	private String emailServer;
	private String username;
	private String password;
	private String fromname;
	private Executor executor = null;

	@PostConstruct
	public void init() {

		try {
			emailServer = emailConfig.getServer();
			username = emailConfig.getUsername();
			password = emailConfig.getPassword();
			fromname = emailConfig.getFromname();

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

		executor = Executors.newCachedThreadPool();

		initSendEmialThread();
	}

	public void sendEmail(EmailVO vo) {
		emailQueue.add(vo);
	}

	// 启动发送邮件的线程
	private void initSendEmialThread() {
		Thread writePvMongoThread = new Thread() {
			public void run() {
				for (; !Thread.interrupted();) {
					EmailVO vo = null;
					try {
						vo = emailQueue.take();
						if (vo == null) {
							continue;
						}

						executor.execute(new SendThread(vo));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		writePvMongoThread.setDaemon(true);
		writePvMongoThread.start();
	}

	private class SendThread implements Runnable {

		private EmailVO vo;

		public SendThread(EmailVO vo) {
			super();
			this.vo = vo;
		}

		@Override
		public void run() {
			try {

				HtmlEmail email = new HtmlEmail();
				email.setHostName(emailServer);
				email.addTo(vo.getToEmail(), vo.getToName());
				email.setFrom(username, fromname);
				email.setAuthentication(username, password);
				email.setSubject(vo.getSubject());
				String htmlMsg = vo.getHtmlMsg();
				String msg = new String(htmlMsg.getBytes(),"ISO8859_1");
				email.setMsg(msg);
				String rs = email.send();
				log.info("发送邮件："+rs);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

	}

}
