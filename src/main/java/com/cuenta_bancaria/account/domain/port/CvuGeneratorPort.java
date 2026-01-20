package com.cuenta_bancaria.account.domain.port;

public interface CvuGeneratorPort {

    String generate(Long accountId);

}
