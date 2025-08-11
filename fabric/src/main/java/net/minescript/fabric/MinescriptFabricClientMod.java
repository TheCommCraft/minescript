// SPDX-FileCopyrightText: © 2022-2025 Greg Christiana <maxuser@minescript.net>
// SPDX-License-Identifier: GPL-3.0-only

package net.minescript.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minescript.common.Minescript;
import net.minescript.common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MinescriptFabricClientMod implements ClientModInitializer {
  private static final Logger LOGGER = LoggerFactory.getLogger("MinescriptFabricClientMod");

  @Override
  public void onInitializeClient() {
    LOGGER.info("(minescript) Minescript mod starting...");

    ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> Minescript.onChunkLoad(world, chunk));
    ClientChunkEvents.CHUNK_UNLOAD.register(
        (world, chunk) -> Minescript.onChunkUnload(world, chunk));

    Minescript.init(new FabricPlatform());
    ClientTickEvents.START_WORLD_TICK.register(world -> Minescript.onClientWorldTick());
    ScreenEvents.AFTER_INIT.register(this::afterInitScreen);
    WorldRenderEvents.END.register(this::onRender);
  }

  private void afterInitScreen(Minecraft client, Screen screen, int windowWidth, int windowHeight) {
    if (screen instanceof ChatScreen) {
      ScreenKeyboardEvents.allowKeyPress(screen)
          .register(
              (_screen, key, scancode, modifiers) -> {
                int keyCode = key;
                if (keyCode >= 32 && keyCode < 127 && (modifiers & 1) > 0) {
                  if (keyCode >= 97 && keyCode < 123) {
                    keyCode -= 32;
                  }
                  switch (keyCode) {
                    case 48:
                      keyCode = 61;
                      break;
                    case 49:
                      keyCode = 33;
                      break;
                    case 50:
                      keyCode = 34;
                      break;
                    case 51:
                      keyCode = 167;
                      break;
                    case 52:
                      keyCode = 36;
                      break;
                    case 53:
                      keyCode = 37;
                      break;
                    case 54:
                      keyCode = 38;
                      break;
                    case 55:
                      keyCode = 47;
                      break;
                    case 56:
                      keyCode = 40;
                      break;
                    case 57:
                      keyCode = 41;
                      break;
                    case 223:
                      keyCode = 63;
                      break;
                    case 35:
                      keyCode = 39;
                      break;
                    case 43:
                      keyCode = 42;
                      break;
                    case 45:
                      keyCode = 95;
                      break;
                    case 46:
                      keyCode = 58;
                      break;
                    case 44:
                      keyCode = 59;
                      break;
                  }
                }
                return !Minescript.onKeyboardKeyPressed(_screen, keyCode);
              }
          );
    }
  }

  private void onRender(WorldRenderContext context) {
    Minescript.onRenderWorld(context);
  }
}
