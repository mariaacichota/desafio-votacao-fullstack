package com.cooperativa.votacao.validator;

import com.cooperativa.votacao.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class CpfValidator {

    public void validar(String cpf) {

        if (!isValido(cpf)) {
            throw new BusinessException("CPF inválido");
        }
    }

    private boolean isValido(String cpf) {

        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int primeiroDigito = calcularDigito(cpf, 9);
        int segundoDigito = calcularDigito(cpf, 10);

        return primeiroDigito == Character.getNumericValue(cpf.charAt(9))
                && segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    private int calcularDigito(String cpf, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (tamanho + 1 - i);
        }
        int resto = soma % 11;

        return resto < 2 ? 0 : 11 - resto;
    }
}