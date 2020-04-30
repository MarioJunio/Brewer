package com.mj.brewer.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mj.brewer.utils.Utils;
import com.mj.brewer.utils.Utils.Dimensoes;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

@Profile("local")
@Component
public class FotoStorageLocal implements FotoStorage {

	private Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);

	private Path local;

	public FotoStorageLocal() {
		this.local = Paths.get(System.getenv("HOME"), ".brewerfotos");
		criarPastas();
	}

	private void criarPastas() {

		try {
			Files.createDirectories(this.local);
			logger.info("Diretório criado: " + this.local.toAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível criar os diretórios para armazenas as fotos ", e);
		}
	}

	@Override
	public byte[] recuperar(String nome) {

		try {
			return Files.readAllBytes(local.resolve(nome));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao recuperar foto " + nome, e);
		}
	}

	@Override
	public String salvar(MultipartFile[] files) {

		if (files != null && files.length > 0) {
			MultipartFile arquivo = files[0];

			String arquivoRenomeado = UUID.randomUUID().toString() + "_" + arquivo.getOriginalFilename();

			try {
				// salva arquivo na pasta de fotos
				arquivo.transferTo(new File(this.local.toAbsolutePath().toString(), arquivoRenomeado));

				// cria thumbnail da foto
				criarThumb(arquivoRenomeado);

			} catch (IllegalStateException e) {
				e.printStackTrace();
				throw new RuntimeException("Erro ao salvar a foto", e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Erro ao salvar a foto", e);
			}

			return arquivoRenomeado;
		}

		return null;
	}

	@Override
	public void excluir(String foto) {
		try {
			Files.deleteIfExists(local.resolve(foto));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao excluir foto", e);
		}
	}

	@Override
	public byte[] recuperarThumbnail(String foto) {
		return recuperar(PREFIX_DOT_THUMBNAIL + foto);
	}

	private void criarThumb(String nome) {

		BufferedImage bufferImage;

		try {
			bufferImage = ImageIO.read(new File(local.resolve(nome).toString()));
			Dimensoes dimensoes = Utils.redimensionarProporcionalmente(bufferImage.getWidth(), bufferImage.getHeight(),
					(int) (bufferImage.getWidth() * Utils.FATOR_REDIMENSIONAMENTO_IMAGEM));

			Thumbnails.of(local.resolve(nome).toString()).size(dimensoes.largura, dimensoes.altura).outputFormat("jpg")
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		} catch (IOException e) {
			throw new RuntimeException("Não foi possivel criar o thumbnail", e);
		}

	}

	@Override
	public String url(String foto) {
		return "http://localhost:8080/brewer/fotos/cerveja/".concat(foto);
	}

	@Override
	public String urlThumb(String foto) {
		return "http://localhost:8080/brewer/fotos/cerveja/".concat(FotoStorage.PREFIX_DOT_THUMBNAIL + foto);
	}

}
