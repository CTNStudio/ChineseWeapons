package net.mirrorloong.chineseweapons.content;

public record RepairEntry(String id, boolean tag) {
    public static RepairEntry item(String id) {
        return new RepairEntry(id, false);
    }

    public static RepairEntry tag(String id) {
        return new RepairEntry(id, true);
    }
}
