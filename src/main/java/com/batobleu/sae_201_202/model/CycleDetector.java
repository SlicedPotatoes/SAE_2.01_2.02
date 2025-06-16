package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.CycleData;

import java.util.HashMap;
import java.util.HashSet;

public class CycleDetector {
    private final HashMap<CycleData, Integer> previousSeen;
    private int cycleIndex;

    public CycleDetector() {
        this.previousSeen = new HashMap<>();
        this.cycleIndex = -1;
    }

    public boolean isCycle(CycleData cd, int index) {
        previousSeen.put(cd, previousSeen.getOrDefault(cd, 0) + 1);

        if(previousSeen.get(cd) == 10) {
            this.cycleIndex = index;
            return true;
        }

        return false;
    }

    public boolean getHaveDetectCycle(int index) {
        return this.cycleIndex == index;
    }
}
