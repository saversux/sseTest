package de.julien.sseTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class TestEntity {
    private String name;
    private int age;
    private boolean human;
}
