package org.cyclops.integrateddynamics.core.evaluate.variable;

import com.google.common.collect.Iterables;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.cyclops.cyclopscore.persist.nbt.INBTProvider;

/**
 * A list proxy for the inventory of an entity.
 */
public class ValueTypeListProxyEntityArmorInventory extends ValueTypeListProxyEntityBase<ValueObjectTypeItemStack, ValueObjectTypeItemStack.ValueItemStack> implements INBTProvider {

    public ValueTypeListProxyEntityArmorInventory(World world, Entity entity) {
        super(ValueTypeListProxyFactories.ENTITY_ARMORINVENTORY.getName(), ValueTypes.OBJECT_ITEMSTACK, world, entity);
    }

    protected ItemStack[] getInventory() {
        Entity e = getEntity();
        if(e != null) {
            ItemStack[] inventory = Iterables.toArray(e.getArmorInventoryList(), ItemStack.class);
            if(inventory != null) {
                return inventory;
            }
        }
        return new ItemStack[0];
    }

    @Override
    public int getLength() {
        return getInventory().length;
    }

    @Override
    public ValueObjectTypeItemStack.ValueItemStack get(int index) {
        return ValueObjectTypeItemStack.ValueItemStack.of(getInventory()[index]);
    }

    @Override
    public void writeGeneratedFieldsToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readGeneratedFieldsFromNBT(NBTTagCompound tag) {

    }
}
