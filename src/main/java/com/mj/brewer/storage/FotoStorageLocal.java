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
import org.springframework.web.multipart.MultipartFile;

import com.mj.brewer.utils.Utils;
import com.mj.brewer.utils.Utils.Dimensoes;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;

public class FotoStorageLocal implements IFotoStorage {

	private Logger logger = LoggerFactory.getLogger(FotoStorageLocal.class);
	public static final String PREFIX_DOT_THUMBNAIL = "thumbnail.";

	private Path local;
	private Path localTemporario;

	public FotoStorageLocal() {
		this.local = Paths.get(System.getenv("HOME"), ".brewerfotos");
		this.localTemporario = Paths.get(this.local.toAbsolutePath().toString(), "temp");
		criarPastas();
	}

	private void criarPastas() {

		try {
			Files.createDirectories(this.local);
			Files.createDirectories(this.localTemporario);
			logger.info("Diretório criado: " + this.local.toAbsolutePath());
			logger.info("Diretório criado: " + this.localTemporario.toAbsolutePath());
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível criar os diretórios para armazenas as fotos ", e);
		}
	}

	@Override
	public String salvarTemporariamente(MultipartFile multipartFile) {
		String novoNome = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

		try {
			multipartFile.transferTo(new File(this.localTemporario.toAbsolutePath().toString(), novoNome));
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException("Erro salvar temporariamente a foto", e);
		}

		return novoNome;
	}

	@Override
	public byte[] recuperarTemporario(String nome) {

		try {
			return Files.readAllBytes(Paths.get(this.localTemporario.toAbsolutePath().toString(), nome));
		} catch (IOException e) {
			throw new RuntimeException("Não foi possível recuperar a foto temporária", e);
		}

	}

	@Override
	public void salvar(String nome) {

		try {
			Files.move(localTemporario.resolve(nome), local.resolve(nome));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Não foi possível mover a foto da pasta temporária para a pasta corrente", e);
		}
	}

	@Override
	public byte[] recuperar(String nome) throws IOException {
		return Files.readAllBytes(local.resolve(nome));
	}

	@Override
	public byte[] recuperarThumbnail(String nome) throws IOException {
		return Files.readAllBytes(local.resolve(PREFIX_DOT_THUMBNAIL + nome));
	}

	@Override
	public void criarThumb(String nome) {

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
	public boolean excluir(String foto) throws IOException {
		return Files.deleteIfExists(local.resolve(foto));
	}

	@Override
	public boolean excluirThumb(String foto) throws IOException {
		return Files.deleteIfExists(local.resolve(PREFIX_DOT_THUMBNAIL + foto));
	}

}
