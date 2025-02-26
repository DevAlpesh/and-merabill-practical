package com.practical.merabillpractical.presentation;


import androidx.lifecycle.ViewModel;

import com.practical.merabillpractical.data.module.PaymentType;
import com.practical.merabillpractical.data.module.Payments;
import com.practical.merabillpractical.data.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PaymentViewModel extends ViewModel {

    private final PaymentRepository repository;
    private final List<Payments> payments;
    private boolean isSaveButtonEnabled = false;

    public PaymentViewModel(PaymentRepository repository) {
        this.repository = repository;
        payments = repository.getPayments();
    }

    public void addNewPayments(Payments payment) {
        payments.add(payment);
        setEnableSaveButton(true);
    }

    public void savePayments(List<Payments> payments) {
        repository.savePayments(payments);
        setEnableSaveButton(false);
    }

    private void setEnableSaveButton(boolean enable) {
        isSaveButtonEnabled = enable;
    }

    public boolean isEnabledSaveButton() {
        return isSaveButtonEnabled;
    }

    public List<Payments> getPayments() {
        return payments;
    }

    public void removePayment(Payments payment) {
        payments.remove(payment);
        isSaveButtonEnabled = true;
    }


    public List<PaymentType> getDropDownData() {
        List<PaymentType> filterPayments = new ArrayList<>();
        Set<String> existingEntries = new HashSet<>();
        for (Payments payment : getPayments()) {
            existingEntries.add(payment.getPaymentType().getType());
        }

        for (PaymentType type : getPaymentTypes()) {
            if (!existingEntries.contains(type.getType())) {
                filterPayments.add(type);
            }
        }
        return filterPayments;
    }

    public List<PaymentType> getPaymentTypes() {
        return Arrays.asList(new PaymentType("cash", "Cash"), new PaymentType("bank_transfer", "Bank Transfer"), new PaymentType("credit_card", "Credit Card"));
    }
}
