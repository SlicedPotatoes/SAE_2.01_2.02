package com.batobleu.sae_201_202;

import com.batobleu.sae_201_202.model.entity.Sheep;
import com.batobleu.sae_201_202.model.entity.Wolf;
import com.batobleu.sae_201_202.model.tile.MapTile;

import java.util.Objects;

public class CycleData {
    private final Wolf w;
    private final Sheep s;
    private final int ml;
    private final MapTile mt;

    public CycleData(Wolf w, Sheep s, int ml, MapTile mt) {
        this.w = w;
        this.s = s;
        this.ml = ml;
        this.mt = mt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CycleData cycleData = (CycleData) o;
        return ml == cycleData.ml &&
                w.getX() == cycleData.w.getX() &&
                w.getY() == cycleData.w.getY() &&
                s.getX() == cycleData.s.getX() &&
                s.getY() == cycleData.s.getY() &&
                Objects.equals(mt, cycleData.mt);
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.w.getX();
        result = 31 * result + this.w.getY();
        result = 31 * result + this.s.getX();
        result = 31 * result + this.s.getY();
        result = 31 * result + ml;
        result = 31 * result + Objects.hashCode(mt);
        return result;
    }
}
