package ru.smmassistant.smmbackend.integration.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

@RequiredArgsConstructor
class TestExampleIt extends IntegrationTestBase {

    @Test
    void test1() {
        System.out.println("test1");
    }

    @Test
    void test2() {
        System.out.println("test2");
    }

    @Test
    void test3() {
        System.out.println("test3");
    }
}
