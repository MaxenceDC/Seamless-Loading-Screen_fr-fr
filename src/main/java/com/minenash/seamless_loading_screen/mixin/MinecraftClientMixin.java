package com.minenash.seamless_loading_screen.mixin;

import com.minenash.seamless_loading_screen.ScreenshotWithTextScreen;
import com.minenash.seamless_loading_screen.SeamlessLoadingScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Shadow protected abstract void reset(Screen screen);

	@Redirect(method = "joinWorld", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/MinecraftClient;reset(Lnet/minecraft/client/gui/screen/Screen;)V"))
	private void changeScreen(MinecraftClient _client, Screen _screen) {
		if (SeamlessLoadingScreen.changeWorldJoinScreen) {
			reset(new ScreenshotWithTextScreen(new TranslatableText("connect.joining")));
			SeamlessLoadingScreen.changeWorldJoinScreen = false;
		}
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;disconnect()V"), method = "startIntegratedServer(Ljava/lang/String;Lnet/minecraft/util/registry/DynamicRegistryManager$Impl;Ljava/util/function/Function;Lcom/mojang/datafixers/util/Function4;ZLnet/minecraft/client/MinecraftClient$WorldLoadAction;)V")
	private void changeScreen(MinecraftClient client) {
		client.disconnect(new ScreenshotWithTextScreen(new LiteralText("")));
	}

//	@Inject(method = "openScreen", at = @At("HEAD"))
//	public void openScreen(Screen screen, CallbackInfo _info) {
//		if (screen != null)
//			System.out.println("Screen: " + screen.getTitle().asString() + ", " + screen);
//		else
//			System.out.println("Screen: null");
//	}

}