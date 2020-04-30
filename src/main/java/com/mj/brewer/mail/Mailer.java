package com.mj.brewer.mail;

import java.io.IOException;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.mj.brewer.model.ItemVenda;
import com.mj.brewer.model.Venda;
import com.mj.brewer.storage.FotoStorage;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine thymeleaf;

	@Autowired
	private FotoStorage fotoStorage;

	@Async
	public void enviar(Venda venda) throws IOException {
		Context context = new Context(new Locale("pt", "br"));
		context.setVariable("venda", venda);

		String html = thymeleaf.process("mail/resumo-venda", context);

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			helper.setFrom("mario.mj.95@gmail.com");
			helper.setTo(venda.getCliente().getEmail());
			helper.setSubject(String.format("Brewer - Compra nº: %d", venda.getId()));
			helper.setText(html, true);

			helper.addInline("logo", new ClassPathResource("static/images/logo-gray.png"));

			for (ItemVenda iv : venda.getItens()) {

				if (iv.getCerveja().temFoto()) {
					String foto = iv.getCerveja().getFoto();
					String contentType = iv.getCerveja().getContentType();

					helper.addInline("thumb-" + iv.getId(), new ByteArrayResource(fotoStorage.recuperarThumbnail(foto)), contentType);
				} else
					helper.addInline("thumb-" + iv.getId(), new ClassPathResource("static/images/thumb.cerveja-mock.png"));
			}

			mailSender.send(mimeMessage);

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}
