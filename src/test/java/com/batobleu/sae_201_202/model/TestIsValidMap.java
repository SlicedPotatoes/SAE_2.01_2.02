package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.model.exception.invalidMap.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.batobleu.sae_201_202.controller.MainController.*;
import static org.junit.jupiter.api.Assertions.*;

class TestIsValidMap {
    private Simulation s;

    @BeforeEach
    public void beforeEach() {
        this.s = new Simulation(5, 5);
    }

    @AfterEach
    public void afterEach() {
        this.s = null;
    }

    @Test
    void test01() {
        assertThrows(NoExitException.class, () -> {
           this.s.isValidMap();
        });
    }

    @Test
    void test02() {
        this.s.getMap()[1][0] = EXIT;

        assertThrows(NoWolfException.class, () -> {
            this.s.isValidMap();
        });
    }

    @Test
    void test03() {
        this.s.getMap()[1][0] = EXIT;
        this.s.setEntity(WOLF, 1, 1);

        assertThrows(NoSheepException.class, () -> {
            this.s.isValidMap();
        });
    }

    @Test
    void test04() {
        this.s.getMap()[1][0] = EXIT;
        this.s.setEntity(WOLF, 1, 1);
        this.s.setEntity(SHEEP, 1, 2);
        this.s.getMap()[2][1] = ROCK;
        this.s.getMap()[3][2] = ROCK;

        assertThrows(UnconnectedGraphException.class, () -> {
           this.s.isValidMap();
        });
    }

    @Test
    void test05() throws InvalidMapException {
        this.s.getMap()[1][0] = EXIT;
        this.s.setEntity(WOLF, 1, 1);
        this.s.setEntity(SHEEP, 1, 2);

        this.s.isValidMap();
    }
}