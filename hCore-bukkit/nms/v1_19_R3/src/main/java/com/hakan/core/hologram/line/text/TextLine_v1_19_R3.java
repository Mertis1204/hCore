package com.hakan.core.hologram.line.text;

import com.hakan.core.HCore;
import com.hakan.core.hologram.Hologram;
import com.hakan.core.utils.Validate;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * {@inheritDoc}
 */
public final class TextLine_v1_19_R3 implements TextLine {

    private String text;
    private final Hologram hologram;
    private final EntityArmorStand click;
    private final EntityArmorStand armorStand;

    /**
     * {@inheritDoc}
     */
    private TextLine_v1_19_R3(@Nonnull Hologram hologram, @Nonnull Location location) {
        World world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        this.hologram = Validate.notNull(hologram, "hologram class cannot be null!");
        this.click = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());
        this.armorStand = new EntityArmorStand(world, location.getX(), location.getY(), location.getZ());

        this.click.persistentInvisibility = true; //set invisibility to true
        this.click.b(5, true); //set invisibility to true
        this.click.n(false); //set custom name visibility to false
        this.click.r(false); //set arms to false
        this.click.s(true); //set no base-plate to true
        this.click.e(true); //set no gravity to true
        this.click.a(false); //set small to false
        this.click.c(114.13f); //set health to 114.13 float

        this.armorStand.persistentInvisibility = true; //set invisibility to true
        this.armorStand.b(5, true); //set invisibility to true
        this.armorStand.n(true); //set custom name visibility to true
        this.armorStand.t(true); //set marker to true
        this.armorStand.r(false); //set arms to false
        this.armorStand.s(true); //set no base-plate to true
        this.armorStand.e(true); //set no gravity to true
        this.armorStand.a(true); //set small to true
        this.armorStand.c(114.13f); //set health to 114.13 float
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(@Nonnull String text) {
        this.text = Validate.notNull(text, "text cannot be null!");
        this.armorStand.b(CraftChatMessage.fromStringOrNull(this.text));
        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityMetadata(this.armorStand.af(), this.armorStand.aj().c()));
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Hologram getHologram() {
        return this.hologram;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEntityID() {
        return this.click.af();
    }

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Location getLocation() {
        return this.armorStand.getBukkitEntity().getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(@Nonnull Location location) {
        Validate.notNull(location, "location cannot be null!");

        World world = ((CraftWorld) Validate.notNull(location.getWorld())).getHandle();
        if (!world.equals(this.click.H)) this.click.H = world;
        if (!world.equals(this.armorStand.H)) this.armorStand.H = world;
        this.click.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.armorStand.a(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityTeleport(this.armorStand),
                new PacketPlayOutEntityTeleport(this.click));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMarker(boolean marker) {
        this.armorStand.t(marker);
        this.click.t(marker);
        HCore.sendPacket(this.hologram.getRenderer().getShownPlayers(),
                new PacketPlayOutEntityMetadata(this.click.af(), this.click.aj().c()),
                new PacketPlayOutEntityMetadata(this.armorStand.af(), this.armorStand.aj().c()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutSpawnEntity(this.armorStand),
                new PacketPlayOutEntityMetadata(this.armorStand.af(), this.armorStand.aj().c()),
                new PacketPlayOutEntityTeleport(this.armorStand),

                new PacketPlayOutSpawnEntity(this.click),
                new PacketPlayOutEntityMetadata(this.click.af(), this.click.aj().c()),
                new PacketPlayOutEntityTeleport(this.click));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide(@Nonnull List<Player> players) {
        HCore.sendPacket(Validate.notNull(players, "players cannot be null!"),
                new PacketPlayOutEntityDestroy(this.armorStand.af()),
                new PacketPlayOutEntityDestroy(this.click.af()));
    }
}