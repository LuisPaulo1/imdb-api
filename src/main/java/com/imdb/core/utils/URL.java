package com.imdb.core.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class URL {

	public static Set<Long> decodeLongSet(String s) {
		try {
			return Arrays.asList(s.split(",")).stream().map(x -> Long.parseLong(x)).collect(Collectors.toSet());
		}catch (IllegalArgumentException e) {
			throw new NumberFormatException("O valor do parâmetro informado está com um padrão de formato inválido. "
					+ "Infome um formato válido. Ex: 1 ou 1,2");
		}
	}
}
