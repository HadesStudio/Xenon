package dev.hely.core.utils.material;

//============================================================
// This file was created by DevDipin!
// Hely Development Developer, and Management Team Member.
//
// If any questions, please contact on discord.
//
// File Information:
//   Path: dev.hely.core.utils.material
//   Date: Wednesday, July 05, 2023 - 2:09 PM
//
// Contact Information:
//   Discord: Andrew!!#4468
//   Hely Development Discord: https://discord.gg/J9XMt8FCxN
//============================================================

import org.bukkit.Bukkit;

public final class MinecraftVersion {
    private static String serverVersion;
    private static V current;

    public static boolean equals(V var0) {
        return compareWith(var0) == 0;
    }

    public static boolean olderThan(V var0) {
        return compareWith(var0) < 0;
    }

    public static boolean newerThan(V var0) {
        return compareWith(var0) > 0;
    }

    public static boolean atLeast(V var0) {
        return equals(var0) || newerThan(var0);
    }

    private static int compareWith(V var0) {
        try {
            return getCurrent().minorVersionNumber - var0.minorVersionNumber;
        } catch (Throwable var2) {
            var2.printStackTrace();
            return 0;
        }
    }

    public static String getServerVersion() {
        return serverVersion.equals("craftbukkit") ? "" : serverVersion;
    }

    public static V getCurrent() {
        return current;
    }

    static {
        try {
            String var0 = Bukkit.getServer() == null ? "" : Bukkit.getServer().getClass().getPackage().getName();
            String var1 = var0.substring(var0.lastIndexOf(46) + 1);
            boolean var2 = !"craftbukkit".equals(var1) && !"".equals(var0);
            serverVersion = var1;
            if (var2) {
                int var3 = 0;
                char[] var4 = var1.toCharArray();
                int var5 = var4.length;

                int var7;
                for(int var6 = 0; var6 < var5; ++var6) {
                    var7 = var4[var6];
                    ++var3;
                    if (var3 > 2 && var7 == 82) {
                        break;
                    }
                }

                String var11 = var1.substring(1, var3 - 2).replace("_", ".");
                var5 = 0;
                char[] var12 = var11.toCharArray();
                var7 = var12.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    char var9 = var12[var8];
                    if (var9 == '.') {
                        ++var5;
                    }
                }

                current = MinecraftVersion.V.parse(Integer.parseInt(var11.split("\\.")[1]));
            } else {
                current = MinecraftVersion.V.v1_3_AND_BELOW;
            }
        } catch (Throwable var10) {
            var10.printStackTrace();
        }

    }

    public static enum V {
        v1_20(20, false),
        v1_19(19),
        v1_18(18),
        v1_17(17),
        v1_16(16),
        v1_15(15),
        v1_14(14),
        v1_13(13),
        v1_12(12),
        v1_11(11),
        v1_10(10),
        v1_9(9),
        v1_8(8),
        v1_7(7),
        v1_6(6),
        v1_5(5),
        v1_4(4),
        v1_3_AND_BELOW(3);

        private final int minorVersionNumber;
        private final boolean tested;

        private V(int var3) {
            this(var3, true);
        }

        private V(int var3, boolean var4) {
            this.minorVersionNumber = var3;
            this.tested = var4;
        }

        private static V parse(int var0) {
            V[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                V var4 = var1[var3];
                if (var4.minorVersionNumber == var0) {
                    return var4;
                }
            }

            return null;
        }

        public String toString() {
            return "1." + this.minorVersionNumber;
        }

        public boolean isTested() {
            return this.tested;
        }
    }
}

