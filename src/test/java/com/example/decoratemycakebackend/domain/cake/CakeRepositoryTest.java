package com.example.decoratemycakebackend.domain.cake;

import com.example.decoratemycakebackend.domain.cake.entity.Cake;
import com.example.decoratemycakebackend.domain.cake.repository.CakeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class CakeRepositoryTest {
    @Autowired
    private CakeRepository cakeRepository;

    @Test
    public void testCake() {
        Cake cake = Cake.builder()
                .cakeName("blue_cake")
                .build();

        Cake savedCake = cakeRepository.save(cake);

        assertNotNull(savedCake.getId());
        assertEquals("blue_cake", savedCake.getCakeName());
    }
}
