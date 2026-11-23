package org.fentanylsolutions.eyesintheshadows.packet.packets;

import net.minecraft.client.Minecraft;

import org.fentanylsolutions.eyesintheshadows.EyesInTheShadows;
import org.fentanylsolutions.eyesintheshadows.client.JumpscareOverlay;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class InitiateJumpscarePacket implements IMessageHandler<InitiateJumpscarePacket.SimpleMessage, IMessage> {

    @Override
    public IMessage onMessage(SimpleMessage message, MessageContext ctx) {
        if (ctx.side.isClient()) {
            EyesInTheShadows.debug("Received InitiateJumpscarePacket packet");
            Minecraft.getMinecraft()
                .func_152344_a(
                    () -> JumpscareOverlay.INSTANCE.show(message.px, message.py, message.pz, message.glitched));
        }
        return null;
    }

    public static class SimpleMessage implements IMessage {

        public double px;
        public double py;
        public double pz;
        /** Whether the eyes that scare are glitched (Limbo's, or in a glitched dimension). */
        public boolean glitched;

        // this constructor is required otherwise you'll get errors (used somewhere in fml through reflection)
        public SimpleMessage() {}

        public SimpleMessage(double px, double py, double pz, boolean glitched) {
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.glitched = glitched;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            px = buf.readDouble();
            py = buf.readDouble();
            pz = buf.readDouble();
            glitched = buf.readBoolean();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeDouble(px);
            buf.writeDouble(py);
            buf.writeDouble(pz);
            buf.writeBoolean(glitched);
        }
    }
}
