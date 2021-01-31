package ru.netology.moneytransferservice.repository;

import org.springframework.stereotype.Repository;
import ru.netology.moneytransferservice.entity.OperationData;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class OperationRepository {
    private ConcurrentMap<String, OperationData> operationRepository;

    public OperationRepository() {
        this.operationRepository = new ConcurrentHashMap<>();
    }

    public OperationData getOperation(String operationId) {
        return operationRepository.get(operationId);
    }

    public void addOperation(OperationData operationData) {
        operationRepository.put(operationData.getOperationId(), operationData);
    }

    public void removeOperation(String operationId) {
        operationRepository.remove(operationId);
    }
}
