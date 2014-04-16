package com.miloshpetrov.sol2.game.particle;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.JsonValue;
import com.miloshpetrov.sol2.TexMan;
import com.miloshpetrov.sol2.common.ColUtil;

import java.util.ArrayList;
import java.util.List;

public class EffectConfig {
  public final EffectType effectType;
  public final float sz;
  public final TextureAtlas.AtlasRegion tex;
  public final boolean floatsUp;
  public final Color tint;

  public EffectConfig(EffectType effectType, float sz, TextureAtlas.AtlasRegion tex, boolean floatsUp, Color tint) {
    this.effectType = effectType;
    this.sz = sz;
    this.tex = tex;
    this.floatsUp = floatsUp;
    this.tint = tint;
  }

  public static EffectConfig load(JsonValue node, EffectTypes types, TexMan texMan, FileHandle configFile) {
    if (node == null) return null;
    String effectFileName = node.getString("effectFile");
    EffectType effectType = types.forName(effectFileName);
    float sz = node.getFloat("size", 0);
    String texName = node.getString("tex");
    boolean floatsUp = node.getBoolean("floatsUp", false);
    Color tint = ColUtil.load(node.getString("tint"));
    TextureAtlas.AtlasRegion tex = texMan.getTex("particles/" + texName, configFile);
    return new EffectConfig(effectType, sz, tex, floatsUp, tint);
  }

  public static List<EffectConfig> loadList(JsonValue listNode, EffectTypes types, TexMan texMan, FileHandle configFile) {
    ArrayList<EffectConfig> res = new ArrayList<EffectConfig>();
    for (JsonValue node : listNode) {
      EffectConfig ec = load(node, types, texMan, configFile);
      res.add(ec);
    }
    return res;
  }

}
