package org.ReDiego0.simpleChat.models;

public class ChatGroup {
    
    private final String name;
    private final int priority;
    private final String format;
    private final String permission;

    public ChatGroup(String name, int priority, String format, String permission) {
        this.name = name;
        this.priority = priority;
        this.format = format;
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public String getFormat() {
        return format;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public String toString() {
        return "ChatGroup{" +
                "name='" + name + '\'' +
                ", priority=" + priority +
                ", format='" + format + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
