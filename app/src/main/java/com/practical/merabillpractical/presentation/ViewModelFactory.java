package com.practical.merabillpractical.presentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.practical.merabillpractical.data.repository.PaymentRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final PaymentRepository repository;

    public ViewModelFactory(PaymentRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PaymentViewModel.class)) {
            return (T) new PaymentViewModel(repository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
