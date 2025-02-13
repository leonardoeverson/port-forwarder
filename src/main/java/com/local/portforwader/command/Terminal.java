package com.local.portforwader.command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Terminal {
    public static boolean run(String origem, String destino, String endereco) {
        String command = String.format("listenport=%s listenaddress=0.0.0.0 connectport=%s connectaddress=%s", origem, destino, endereco);

        try {
            System.out.println(runner("netsh interface portproxy set v4tov4 " + command));
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static List<String[]> read() throws IOException {
        List<String[]> toReturn = new ArrayList<>(List.of());

        try {
            InputStreamReader input = runner("netsh interface portproxy show all");

            StringBuilder builder = new StringBuilder();
            for (int ch; (ch = input.read()) != -1; ) {
                builder.append((char) ch);
            }

            List<String> linhas = null;
            if (builder.toString().contains("\r\n")) {
                linhas = List.of(builder.toString().split("\r\n"));
            } else if (builder.toString().contains("\n")) {
                linhas = List.of(builder.toString().split("\n"));
            }

            if (linhas != null) {
                boolean extract = false;
                for (String linha : linhas) {
                    if (linha.contains("---")) {
                        extract = true;
                        continue;
                    }

                    if (extract) {
                        Object[] obj = Arrays.stream(linha.split(" "))
                                .filter(s -> !s.trim().isEmpty())
                                .toArray();

                        String[] item = Arrays.copyOf(obj, obj.length, String[].class);
                        toReturn.add(item);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return toReturn;
    }

    private static InputStreamReader runner(String command) throws IOException {
        ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe", "/c", command
        );

        builder.redirectErrorStream(true);

        Process process = builder.start();
        return new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
    }
}
