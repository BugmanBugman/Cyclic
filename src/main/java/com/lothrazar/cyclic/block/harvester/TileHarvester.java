package com.lothrazar.cyclic.block.harvester;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.capability.CustomEnergyStorage;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilShape;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.StemBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileHarvester extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final INamedTag<Block> HARVEST_BREAK = BlockTags.makeWrapperTag(new ResourceLocation(ModCyclic.MODID, "harvester_break").toString());
  public static IntValue POWERCONF;
  private int radius = 9;
  private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
  BlockPos laserTarget;
  int laserTimer;
  private static final int ATTEMPTS_PERTICK = 16;
  static final int MAX = 640000;

  static enum Fields {
    REDSTONE, RENDER;
  }

  public TileHarvester() {
    super(TileRegistry.harvesterTile);
  }

  @Override
  public void tick() {
    this.syncEnergy();
    if (this.requiresRedstone() && !this.isPowered()) {
      setLitProperty(false);
      return;
    }
    setLitProperty(true);
    if (this.laserTimer > 0) {
      laserTimer--;
    }
    if (this.world.isRemote)
      return;
    IEnergyStorage cap = this.energy.orElse(null);
    if (cap == null) {
      return;
    }
    for (int i = 0; i < ATTEMPTS_PERTICK; i++) {
      BlockPos target = UtilWorld.getRandomPos(world.rand, this.getCurrentFacingPos(radius), radius);
      Integer cost = POWERCONF.get();
      if (cap.getEnergyStored() < cost && cost > 0) {
        break;//too broke
      }
      if (tryHarvestSingle(this.world, target)) {
        cap.extractEnergy(cost, false);
        laserTarget = target;
        laserTimer = 15;
        break;
      }
    }
  }

  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(this.getCurrentFacingPos(radius), radius);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public AxisAlignedBB getRenderBoundingBox() {
    return TileEntity.INFINITE_EXTENT_AABB;
  }

  public static boolean tryHarvestSingle(World world, BlockPos posCurrent) {
    BlockState blockState = world.getBlockState(posCurrent);
    if (TileHarvester.simpleBreakDrop(blockState)) {
      UtilItemStack.drop(world, posCurrent, blockState.getBlock());
      world.destroyBlock(posCurrent, false);
      return true;
    }
    //don't break stems see Issue #1601
    if (world.getBlockState(posCurrent).getBlock() instanceof StemBlock) {
      return false;
    }
    IntegerProperty propInt = TileHarvester.getAgeProp(blockState);
    if (propInt == null || !(world instanceof ServerWorld)) {
      return false;
    }
    int currentAge = blockState.get(propInt);
    int minAge = Collections.min(propInt.getAllowedValues());
    int maxAge = Collections.max(propInt.getAllowedValues());
    if (minAge == maxAge || currentAge < maxAge) {
      //not grown
      return false;
    }
    //update behavior to address Issue #1600
    List<ItemStack> drops = Block.getDrops(blockState, (ServerWorld) world, posCurrent, null);
    drops.forEach((dropStack) -> {
      if (dropStack.getItem() == blockState.getBlock().asItem()) {
        dropStack.shrink(1);
      }
      if (!dropStack.isEmpty()) {
        UtilWorld.dropItemStackInWorld(world, posCurrent, dropStack);
      }
    });
    blockState.spawnAdditionalDrops((ServerWorld) world, posCurrent, ItemStack.EMPTY);
    BlockState newState = blockState.with(propInt, minAge);
    world.setBlockState(posCurrent, newState);
    world.notifyBlockUpdate(posCurrent, newState, newState, 3);
    //        UtilWorld.flagUpdate(world, pos, this.getBlockState(), this.getBlockState());
    //    this.markDirty();
    return true;
  }

  private static boolean simpleBreakDrop(BlockState blockState) {
    boolean breakit = blockState.getBlock().isIn(HARVEST_BREAK);
    // the list tells all
    return breakit;
  }

  public static IntegerProperty getAgeProp(BlockState blockState) {
    if (blockState.getBlock() instanceof CropsBlock) {
      CropsBlock crops = (CropsBlock) blockState.getBlock();
      //better mod compatibility if they dont use 'age'
      return crops.getAgeProperty();
    }
    String age = "age";
    ResourceLocation bid = blockState.getBlock().getRegistryName();
    if ("resynth".equalsIgnoreCase(bid.getNamespace())) {
      //some silly old mods dont use age for compatibility
      // https://github.com/Resynth-Minecraft-Mod/Resynth-Mod/blob/a9f47439d103c1c17ca7a4ffd05c2dc0397e5e5f/src/main/java/com/ki11erwolf/resynth/plant/block/BlockBiochemicalPlant.java#L59
      //so we hack it
      age = "growth_stage";
    }
    for (Property<?> p : blockState.getProperties()) {
      if (p != null && p.getName() != null
          && p instanceof IntegerProperty &&
          p.getName().equalsIgnoreCase(age)) {
        return (IntegerProperty) p;
      }
    }
    //IGrowable is useless here, i tried. no way to tell if its fully grown, or what age/stage its in
    return null;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDER:
        return render;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case RENDER:
        this.render = value % 2;
      break;
    }
  }

  private IEnergyStorage createEnergy() {
    return new CustomEnergyStorage(MAX, MAX / 4);
  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityEnergy.ENERGY && POWERCONF.get() > 0) {
      return energy.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    this.laserTarget = UtilNBT.getBlockPos(tag);
    laserTimer = tag.getInt("lt");
    energy.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("energy")));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    if (laserTarget == null) {
      laserTarget = BlockPos.ZERO;
    }
    UtilNBT.putBlockPos(tag, laserTarget);
    tag.putInt("lt", laserTimer);
    energy.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("energy", compound);
    });
    return super.write(tag);
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerHarvester(i, world, pos, playerInventory, playerEntity);
  }
}
