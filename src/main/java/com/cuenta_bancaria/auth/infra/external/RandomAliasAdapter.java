package com.cuenta_bancaria.auth.infra.external;

import com.cuenta_bancaria.auth.domain.AliasGeneratorPort;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RandomAliasAdapter implements AliasGeneratorPort {

    private final List<String> words = new ArrayList<>();
    private final Random random = new Random();

    // El constructor carga las palabras del archivo ubicado en resources
    public RandomAliasAdapter(ResourceLoader resourceLoader) {
        try {
            Resource resource = resourceLoader.getResource("classpath:alias_words.txt");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            )) {
                reader.lines()
                        .filter(line -> !line.isBlank())
                        .map(String::trim)
                        .map(String::toUpperCase)
                        .forEach(words::add);
            }
        } catch (IOException e) {
            // En caso de que el archivo no esté, se cargan unas palabras por defecto
            words.addAll(List.of("BANCO", "CUENTA", "PAGO"));
        }
    }

    @Override
    public String generate() {
        if (words.size() < 3) return "ALIAS.POR.DEFECTO";

        Set<String> alias = new LinkedHashSet<>();

        while(alias.size() < 3) {
            alias.add(getRandomWord());
        }

        return String.join(".", alias);
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

}
