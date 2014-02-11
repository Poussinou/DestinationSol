package com.miloshpetrov.sol2.game.planet;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Tile {
  public final TextureAtlas.AtlasRegion reg;
  public final List<Vector2> points;
  public final SurfDir to;
  public final SurfDir from;

  public Tile(TextureAtlas.AtlasRegion reg, List<Vector2> points, SurfDir to, SurfDir from) {
    this.reg = reg;
    this.points = points;
    this.to = to;
    this.from = from;
  }
}
