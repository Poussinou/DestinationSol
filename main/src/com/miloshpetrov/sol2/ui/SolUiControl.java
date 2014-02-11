package com.miloshpetrov.sol2.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.miloshpetrov.sol2.SolCmp;
import com.miloshpetrov.sol2.common.Col;

public class SolUiControl {
  private final int[] myKeys;
  private final Rectangle myScreenArea;

  private String myDisplayName;

  private boolean myEnabled = true;

  private boolean myKeyPressed;
  private boolean myKeyPressedPrev;
  private boolean myKeyFlash;

  private boolean myAreaPressed;
  private boolean myAreaFlash;
  private boolean myAreaJustUnpressed;

  private boolean myMouseHover;

  public SolUiControl(Rectangle screenArea, int ... keys) {
    myKeys = keys == null ? new int[0] : keys;
    myScreenArea = screenArea;
  }

  public boolean maybeFlashPressed(int keyCode) {
    if (!myEnabled) return false;
    for (int key : myKeys) {
      if (key != keyCode) continue;
      myKeyFlash = true;
      return true;
    }
    return false;
  }

  public boolean maybeFlashPressed(SolInputMan.Ptr ptr) {
    if (!myEnabled) return false;
    boolean pressed = myScreenArea != null && myScreenArea.contains(ptr.x, ptr.y);
    if (pressed) myAreaFlash = true;
    return pressed;
  }

  public void update(SolInputMan.Ptr[] ptrs, boolean cursorShown, boolean canBePressed) {
    if (!myEnabled) canBePressed = false;

    updateKeys(canBePressed);
    updateArea(ptrs, canBePressed);

    updateHover(ptrs, cursorShown);
  }

  private void updateHover(SolInputMan.Ptr[] ptrs, boolean cursorShown) {
    if (myScreenArea == null || myAreaPressed || ptrs[0].pressed) return;
    myMouseHover = cursorShown && myScreenArea.contains(ptrs[0].x, ptrs[0].y);
  }

  private void updateKeys(boolean canBePressed) {
    myKeyPressedPrev = myKeyPressed;
    if (myKeyFlash) {
      myKeyPressed = true;
      myKeyFlash = false;
    } else {
      myKeyPressed = false;
      if (canBePressed) {
        for (int key : myKeys) {
          if (!Gdx.input.isKeyPressed(key)) continue;
          myKeyPressed = true;
          break;
        }
      }
    }
  }

  private void updateArea(SolInputMan.Ptr[] ptrs, boolean canBePressed) {
    if (myScreenArea == null) return;
    myAreaJustUnpressed = false;
    if (myAreaFlash) {
      myAreaPressed = true;
      myAreaFlash = false;
    } else {
      myAreaPressed = false;
      if (canBePressed) {
        for (SolInputMan.Ptr ptr : ptrs) {
          if (!myScreenArea.contains(ptr.x, ptr.y)) continue;
          myAreaPressed = ptr.pressed;
          myAreaJustUnpressed = !ptr.pressed && ptr.prevPressed;
          break;
        }
      }
    }
  }

  // poll to perform continuous actions
  public boolean isOn() {
    return myEnabled && (myKeyPressed || myAreaPressed);
  }

  // poll to perform one-off actions
  public boolean isJustOff() {
    return myEnabled && (!myKeyPressed && myKeyPressedPrev || myAreaJustUnpressed);
  }

  public void setDisplayName(String displayName) {
    myDisplayName = displayName;
  }

  public void drawButton(UiDrawer uiDrawer, SolCmp cmp) {
    if (myScreenArea == null) return;
    Color tint = Col.W05;
    if (myEnabled) {
      if (isOn()) tint = Col.W25;
      else if (myMouseHover) tint = Col.W15;
      else tint = Col.W10;
    }
    uiDrawer.draw(myScreenArea, tint);
  }

  public void drawDisplayName(UiDrawer uiDrawer, SolCmp cmp) {
    if (myScreenArea == null) return;
    Color tint = myEnabled ? Col.W : Col.G;
    uiDrawer.drawString(myDisplayName, myScreenArea.x + myScreenArea.width/2, myScreenArea.y + myScreenArea.height/2,
      FontSize.MENU, true, tint);
  }

  public void setEnabled(boolean enabled) {
    myEnabled = enabled;
  }

  public void blur() {
    myKeyPressed = false;
    myKeyPressedPrev = false;
    myAreaPressed = false;
    myAreaJustUnpressed = false;
  }


  public boolean isEnabled() {
    return myEnabled;
  }


  public Rectangle getScreenArea() {
    return myScreenArea;
  }
}
