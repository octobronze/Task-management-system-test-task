package com.example.Task_management_system_test_task.specifications;

import com.example.Task_management_system_test_task.enums.EntityFieldEnum;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;

import java.util.Arrays;
import java.util.List;

public class FetchService<T> {
    private final List<List<EntityFieldEnum>> fetchChainList;

    public FetchService(List<List<EntityFieldEnum>> fetchChainList) {
        this.fetchChainList = fetchChainList;
    }

    public FetchService(EntityFieldEnum... entityFields) {
        this.fetchChainList = List.of(Arrays.stream(entityFields).toList());
    }

    public void fetch(Root<T> root) {
        for (var fetchChain : fetchChainList) {
            if (fetchChain.isEmpty()) continue;

            var initialFetch = root.fetch(fetchChain.get(0).getName(), JoinType.LEFT);
            for (int i = 1; i < fetchChain.size(); i ++) {
                initialFetch = initialFetch.fetch(fetchChain.get(i).getName(), JoinType.LEFT);
            }
        }
    }
}
