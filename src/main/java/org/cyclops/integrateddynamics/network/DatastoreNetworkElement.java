package org.cyclops.integrateddynamics.network;

import lombok.Data;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.helper.TileHelpers;
import org.cyclops.integrateddynamics.core.network.INetworkElement;
import org.cyclops.integrateddynamics.core.network.Network;
import org.cyclops.integrateddynamics.tileentity.TileDatastore;

import java.util.List;

/**
 * Network element for data stores.
 * @author rubensworks
 */
@Data
public class DatastoreNetworkElement implements INetworkElement {

    private final DimPos pos;

    protected TileDatastore getTile() {
        return TileHelpers.getSafeTile(getPos().getWorld(), getPos().getBlockPos(), TileDatastore.class);
    }

    @Override
    public int getUpdateInterval() {
        return 0;
    }

    @Override
    public boolean isUpdate() {
        return false;
    }

    @Override
    public void update(Network network) {

    }

    @Override
    public void beforeNetworkKill(Network network) {

    }

    @Override
    public void afterNetworkAlive(Network network) {

    }

    @Override
    public void addDrops(List<ItemStack> itemStacks) {
        TileDatastore tile = getTile();
        if(tile != null) {
            InventoryHelper.dropInventoryItems(getPos().getWorld(), getPos().getBlockPos(), tile.getInventory());
        }
    }

    @Override
    public int compareTo(INetworkElement o) {
        if(o instanceof DatastoreNetworkElement) {
            return getPos().compareTo(((DatastoreNetworkElement) o).getPos());
        }
        return Integer.compare(hashCode(), o.hashCode());
    }
}