package com.mj.brewer.utils;

import java.text.Normalizer;

public class Utils {

	public static float FATOR_REDIMENSIONAMENTO_IMAGEM = 0.20f;

	public static Dimensoes redimensionarProporcionalmente(int width, int height, int newWidth) {
		return new Dimensoes(newWidth, (newWidth * height) / width);
	}

	public static class Dimensoes {
		public int largura, altura;

		public Dimensoes(int largura, int altura) {
			this.largura = largura;
			this.altura = altura;
		}
	}

	public static String removerMascaraCpfCnpj(String cpfCnpj) {
		return cpfCnpj != null ? cpfCnpj.replaceAll("\\.|-|/", "") : null;
	}

	public static String substituirCaracteresEspeciais(String input) {
		return Normalizer.normalize(input, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
}
