package com.hakan.core.ui.sign.versions;

import com.hakan.core.HCore;
import com.hakan.core.ui.sign.SignGui;
import com.hakan.core.ui.sign.type.SignType;
import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayInUpdateSign;
import net.minecraft.network.protocol.game.PacketPlayOutBlockChange;
import net.minecraft.network.protocol.game.PacketPlayOutOpenSignEditor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntitySign;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_18_R1.block.CraftSign;
import org.bukkit.craftbukkit.v1_18_R1.util.CraftMagicNumbers;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * {@inheritDoc}
 */
public final class SignGui_v1_18_R1 extends SignGui {

    /**
     * {@inheritDoc}
     */
    private SignGui_v1_18_R1(@Nonnull Player player,
                             @Nonnull SignType type,
                             @Nonnull String... lines) {
        super(player, type, lines);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Nonnull
    @Override
    public SignGui open() {
        Location location = super.player.getLocation();
        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), LOWEST_Y_AXIS + 1, location.getBlockZ());

        HCore.sendPacket(super.player, new PacketPlayOutBlockChange(blockPosition, CraftMagicNumbers.getBlock(super.type.asMaterial()).n()));

        IChatBaseComponent[] components = CraftSign.sanitizeLines(super.lines);
        TileEntitySign sign = new TileEntitySign(new BlockPosition(blockPosition.u(), blockPosition.v(), blockPosition.w()), Blocks.cg.n());
        System.arraycopy(components, 0, sign.d, 0, sign.d.length);
        HCore.sendPacket(super.player, sign.c());

        HCore.sendPacket(super.player, new PacketPlayOutOpenSignEditor(blockPosition));

        return super.onOpen();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Nonnull
    @Override
    public <T> SignGui receiveInput(@Nonnull T packet) {
        PacketPlayInUpdateSign packetPlayInUpdateSign = (PacketPlayInUpdateSign) packet;

        BlockPosition position = packetPlayInUpdateSign.b();
        Block block = super.player.getWorld().getBlockAt(position.u(), position.v(), position.w());
        IBlockData data = CraftMagicNumbers.getBlock(block.getType()).n();
        HCore.sendPacket(super.player, new PacketPlayOutBlockChange(position, data));

        String[] b = packetPlayInUpdateSign.c();
        String[] lines = new String[b.length];
        System.arraycopy(b, 0, lines, 0, b.length);

        return super.onInputReceive(lines);
    }
}