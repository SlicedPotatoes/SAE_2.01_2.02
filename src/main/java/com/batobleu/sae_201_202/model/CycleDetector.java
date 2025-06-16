package com.batobleu.sae_201_202.model;

import com.batobleu.sae_201_202.CycleData;
import java.util.HashSet;

public class CycleDetector {
    private final HashSet<CycleData> previousSeen;
    private int cycleIndex;

    public CycleDetector() {
        this.previousSeen = new HashSet<>();
        this.cycleIndex = -1;
    }

    public boolean isCycle(CycleData cd, int index) {
        if(previousSeen.contains(cd)) {
            this.cycleIndex = index;
            return true;
        }

        previousSeen.add(cd);
        return false;
    }

    public boolean getHaveDetectCycle(int index) {
        return this.cycleIndex == index;
    }
}
