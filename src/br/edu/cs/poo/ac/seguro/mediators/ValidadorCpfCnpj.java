package br.edu.cs.poo.ac.seguro.mediators;

import java.util.InputMismatchException;

public class ValidadorCpfCnpj {

    private static final int[] pesoCPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] pesoCNPJ = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }

    public static boolean ehCpfValido(String cpf) {
        if ((cpf == null) || (cpf.length() != 11) || !StringUtils.temSomenteNumeros(cpf)) return false;

        if (cpf.matches("(\\d)\\1{10}")) return false;


        int digito1 = calcularDigito(cpf.substring(0, 9), pesoCPF);
        int digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCPF);
        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }

    public static boolean ehCnpjValido(String cnpj) {
        if ((cnpj == null) || (cnpj.length() != 14) || !StringUtils.temSomenteNumeros(cnpj)) return false;

        if (cnpj.matches("(\\d)\\1{13}")) return false;

        int digito1 = calcularDigito(cnpj.substring(0, 12), pesoCNPJ);
        int digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCNPJ);
        return cnpj.equals(cnpj.substring(0, 12) + digito1 + digito2);
    }
}
