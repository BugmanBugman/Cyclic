package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.component.autouser.PacketTileIncrementField;
import com.lothrazar.cyclicmagic.component.builder.PacketTileBuildSize;
import com.lothrazar.cyclicmagic.component.builder.PacketTileBuildType;
import com.lothrazar.cyclicmagic.component.controlledminer.PacketTileMineHeight;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellBuildSize;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellFromServer;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellShiftLeft;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketSpellShiftRight;
import com.lothrazar.cyclicmagic.component.cyclicwand.PacketWandGui;
import com.lothrazar.cyclicmagic.component.enderbook.PacketDeleteWaypoint;
import com.lothrazar.cyclicmagic.component.enderbook.PacketNewButton;
import com.lothrazar.cyclicmagic.component.enderbook.PacketWarpButton;
import com.lothrazar.cyclicmagic.component.entitydetector.PacketTileDetector;
import com.lothrazar.cyclicmagic.component.fan.PacketTileFan;
import com.lothrazar.cyclicmagic.component.merchant.PacketSyncVillagerToClient;
import com.lothrazar.cyclicmagic.component.merchant.PacketSyncVillagerToServer;
import com.lothrazar.cyclicmagic.component.merchant.PacketVillagerTrade;
import com.lothrazar.cyclicmagic.component.password.PacketTilePassword;
import com.lothrazar.cyclicmagic.component.pattern.PacketTilePatternBuilder;
import com.lothrazar.cyclicmagic.component.pattern.PacketTilePatternSwap;
import com.lothrazar.cyclicmagic.component.playerext.PacketOpenExtendedInventory;
import com.lothrazar.cyclicmagic.component.playerext.PacketOpenFakeWorkbench;
import com.lothrazar.cyclicmagic.component.playerext.PacketOpenNormalInventory;
import com.lothrazar.cyclicmagic.component.playerext.PacketSyncExtendedInventory;
import com.lothrazar.cyclicmagic.component.pylonexp.PacketTilePylon;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketDepositContainerToPlayer;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketDepositPlayerToNearby;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketQuickStack;
import com.lothrazar.cyclicmagic.component.terrariabuttons.PacketRestockContainerToPlayer;
import com.lothrazar.cyclicmagic.component.vector.PacketTileVector;
import com.lothrazar.cyclicmagic.net.PacketChestSack;
import com.lothrazar.cyclicmagic.net.PacketItemToggle;
import com.lothrazar.cyclicmagic.net.PacketMoveBlock;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerColumn;
import com.lothrazar.cyclicmagic.net.PacketMovePlayerHotbar;
import com.lothrazar.cyclicmagic.net.PacketParticleAtPosition;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclicmagic.net.PacketRandomize;
import com.lothrazar.cyclicmagic.net.PacketSleepClient;
import com.lothrazar.cyclicmagic.net.PacketSound;
import com.lothrazar.cyclicmagic.net.PacketSwapBlock;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerData;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerFlying;
import com.lothrazar.cyclicmagic.net.PacketSyncPlayerHealth;
import com.lothrazar.cyclicmagic.net.PacketTileRedstoneToggle;
import com.lothrazar.cyclicmagic.net.PacketTileSizeToggle;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketRegistry {
  public static void register(SimpleNetworkWrapper network) {
    int packetID = 0;
    network.registerMessage(PacketOpenExtendedInventory.class, PacketOpenExtendedInventory.class, packetID++, Side.SERVER);
    network.registerMessage(PacketOpenNormalInventory.class, PacketOpenNormalInventory.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncExtendedInventory.class, PacketSyncExtendedInventory.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSpellShiftLeft.class, PacketSpellShiftLeft.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSpellShiftRight.class, PacketSpellShiftRight.class, packetID++, Side.SERVER);
    network.registerMessage(PacketParticleAtPosition.class, PacketParticleAtPosition.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSpellFromServer.class, PacketSpellFromServer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketWandGui.class, PacketWandGui.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMovePlayerColumn.class, PacketMovePlayerColumn.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMovePlayerHotbar.class, PacketMovePlayerHotbar.class, packetID++, Side.SERVER);
    network.registerMessage(PacketWarpButton.class, PacketWarpButton.class, packetID++, Side.SERVER);
    network.registerMessage(PacketNewButton.class, PacketNewButton.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDeleteWaypoint.class, PacketDeleteWaypoint.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDepositPlayerToNearby.class, PacketDepositPlayerToNearby.class, packetID++, Side.SERVER);
    network.registerMessage(PacketDepositContainerToPlayer.class, PacketDepositContainerToPlayer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketQuickStack.class, PacketQuickStack.class, packetID++, Side.SERVER);
    network.registerMessage(PacketRestockContainerToPlayer.class, PacketRestockContainerToPlayer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketOpenFakeWorkbench.class, PacketOpenFakeWorkbench.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSpellBuildSize.class, PacketSpellBuildSize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerData.class, PacketSyncPlayerData.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTileBuildType.class, PacketTileBuildType.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileBuildSize.class, PacketTileBuildSize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerHealth.class, PacketSyncPlayerHealth.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTilePassword.class, PacketTilePassword.class, packetID++, Side.SERVER);
    network.registerMessage(PacketMoveBlock.class, PacketMoveBlock.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileMineHeight.class, PacketTileMineHeight.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSwapBlock.class, PacketSwapBlock.class, packetID++, Side.SERVER);
    network.registerMessage(PacketRandomize.class, PacketRandomize.class, packetID++, Side.SERVER);
    network.registerMessage(PacketChestSack.class, PacketChestSack.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileRedstoneToggle.class, PacketTileRedstoneToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileSizeToggle.class, PacketTileSizeToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileIncrementField.class, PacketTileIncrementField.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTilePatternBuilder.class, PacketTilePatternBuilder.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTilePatternSwap.class, PacketTilePatternSwap.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileDetector.class, PacketTileDetector.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTileVector.class, PacketTileVector.class, packetID++, Side.SERVER);
    network.registerMessage(PacketPlayerFalldamage.class, PacketPlayerFalldamage.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSyncPlayerFlying.class, PacketSyncPlayerFlying.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSyncVillagerToClient.class, PacketSyncVillagerToClient.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketSyncVillagerToServer.class, PacketSyncVillagerToServer.class, packetID++, Side.SERVER);
    network.registerMessage(PacketVillagerTrade.class, PacketVillagerTrade.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSleepClient.class, PacketSleepClient.class, packetID++, Side.CLIENT);
    network.registerMessage(PacketTileFan.class, PacketTileFan.class, packetID++, Side.SERVER);
    network.registerMessage(PacketItemToggle.class, PacketItemToggle.class, packetID++, Side.SERVER);
    //    network.registerMessage(PacketTileFan.class, PacketTileFan.class, packetID++, Side.SERVER);
    //    network.registerMessage(PacketItemToggle.class, PacketItemToggle.class, packetID++, Side.SERVER);
    network.registerMessage(PacketTilePylon.class, PacketTilePylon.class, packetID++, Side.SERVER);
    network.registerMessage(PacketSound.class, PacketSound.class, packetID++, Side.CLIENT);
  }
}
