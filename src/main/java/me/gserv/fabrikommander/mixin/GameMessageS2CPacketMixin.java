package me.gserv.fabrikommander.mixin;

import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static me.gserv.fabrikommander.utils.TextKt.*;

@Mixin(GameMessageS2CPacket.class)
public class GameMessageS2CPacketMixin {
    @Shadow private Text message;
    @Shadow private MessageType location;
    @Shadow private UUID senderUuid;

    private Text originalMessage;
    private MessageType originalMessageLocation;
    private UUID originalSenderUuid;

    /**
     * Manipulates GameMessage packets before they
     * are sent. Currently only changes the messages
     * for join and leave messages.
     *
     * @param message
     * @param location
     * @param senderUuid
     * @param ci
     */
    @Inject(
            method = "<init>(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V",
            at = @At("TAIL")
    )
    private void editMessagePackets(Text message, MessageType location, UUID senderUuid, CallbackInfo ci) {
        this.originalMessage = message;
        this.originalMessageLocation = location;
        this.originalSenderUuid = senderUuid;

        TranslatableText translatableMessage = (message instanceof TranslatableText) ? (TranslatableText) message : null;

        // Catch join/leave messages and format them
        if (isJoinOrLeaveMessage(translatableMessage)) {

            // Format normal join message
            if (isSimpleJoinMessage(translatableMessage)) {
                message = formatSimpleJoinMessage(translatableMessage);

            // Format join messages with new name
            } else if (isRenamedJoinMessage(translatableMessage)) {
                message = formatRenamedJoinMessage(translatableMessage);

            // Format leave message
            } else if (isLeaveMessage(translatableMessage)) {
                message = formatLeaveMessage(translatableMessage);
            }

        }

        this.message = message;
        this.location = location;
        this.senderUuid = senderUuid;
    }

    /**
     * Check for join and leave messages
     *
     * @param message
     * @return
     */
    private boolean isJoinOrLeaveMessage(@Nullable TranslatableText message) {
        if (message == null) { return false; }
        return isSimpleJoinMessage(message) || isRenamedJoinMessage(message) || isLeaveMessage(message);
    }

    /**
     * Check for normal join message
     *
     * @param message
     * @return
     */
    private boolean isSimpleJoinMessage(TranslatableText message) {
        return message.getKey().equals("multiplayer.player.joined");
    }

    /**
     * Checks for renamed join message
     *
     * @param message
     * @return
     */
    private boolean isRenamedJoinMessage(TranslatableText message) {
        return message.getKey().equals("multiplayer.player.joined.renamed");
    }

    /**
     * Check for leave message
     *
     * @param message
     * @return
     */
    private boolean isLeaveMessage(TranslatableText message) {
        return message.getKey().equals("multiplayer.player.left");
    }

    /**
     * Format simple join messages to
     * [+] PlayerName
     *
     * @param message
     * @return
     */
    private MutableText formatSimpleJoinMessage(TranslatableText message) {
        return reset("")
                .append(
                        darkGray("[")
                )
                .append(
                        darkGreen("+")
                ).append(
                        darkGray("] ")
                ).append(
                        (MutableText) message.getArgs()[0]
                );
    }

    /**
     * Format renamed join messages to
     * [+] PlayerName (Formerly known as FormerPlayerName)
     *
     * @param message
     * @return
     */
    private MutableText formatRenamedJoinMessage(TranslatableText message) {
        return reset("")
                .append(
                        darkGray("[")
                )
                .append(
                        darkGreen("+")
                ).append(
                        darkGray("] ")
                ).append(
                        (MutableText) message.getArgs()[0]
                ).append(
                        gray(italic(" (formerly known as "))
                ).append(
                        yellow(italic((String) message.getArgs()[1]))
                ).append(
                        gray(italic(")"))
                );
    }

    /**
     * Format leave messages to
     * [-] PlayerName
     *
     * @param message
     * @return
     */
    private MutableText formatLeaveMessage(TranslatableText message) {
        return reset("")
                .append(
                        darkGray("[")
                )
                .append(
                        darkRed("-")
                ).append(
                darkGray("] ")
        ).append(
                (MutableText) message.getArgs()[0]
        );
    }
}
