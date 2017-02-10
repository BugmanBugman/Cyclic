package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemTrader extends BaseItem {
  int radius = 5;
  public ItemTrader() {
    super();
    this.setMaxStackSize(1);
  }
  public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World world, EntityPlayer player, EnumHand hand) {
    BlockPos p = player.getPosition();

//    List<EntityVillager> all =UtilEntity.getVillagers(world, p,radius);
//    System.out.println(all.size());
//    if (!all.isEmpty()) {
      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_VILLAGER, world, p.getX(), p.getY(), p.getZ());
//      return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
//    }
    //    }
    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemStackIn);
  }
}
