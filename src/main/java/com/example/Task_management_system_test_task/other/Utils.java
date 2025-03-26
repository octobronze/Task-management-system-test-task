package com.example.Task_management_system_test_task.other;

import com.example.Task_management_system_test_task.exceptions.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class Utils {
    public static <T> Page<T> getPageImpl(List<T> list, Integer pageIndex, Integer pageSize) {
        if (pageIndex  * pageSize >= list.size()) {
            throw new BadRequestException("Incorrect page data");
        }

        return new PageImpl<>(
                list.subList(pageIndex * pageSize, Math.max(pageIndex * pageSize, list.size())),
                PageRequest.of(pageIndex, pageSize),
                list.size()
        );
    }
}
