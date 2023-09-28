package com.ead.authuser.domain.validator.impl;

import com.ead.authuser.api.dtos.request.UserRequest;
import com.ead.authuser.domain.exceptions.CpflAlreadyExistsException;
import com.ead.authuser.domain.exceptions.EmailAlreadyExistsException;
import com.ead.authuser.domain.exceptions.UsernameAlreadyExistsException;
import com.ead.authuser.domain.repositories.UserRepository;
import com.ead.authuser.domain.validator.UserValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationStrategyImpl implements UserValidationStrategy {

    private final UserRepository userRepository;

    @Override
    public void validate(UserRequest request) {
        existsByCpf(request.getCpf());
        existsByEmail(request.getEmail());
        existsByUerName(request.getUsername());
    }

    private void existsByUerName(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }
    }

    private void existsByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
    }

    private void existsByCpf(String cpf) {
        if (userRepository.existsByCpf(cpf)) {
            throw new CpflAlreadyExistsException(cpf);
        }
    }
}
