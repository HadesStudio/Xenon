package net.ascend.core.bukkit.util.command.utils;

import lombok.NonNull;

import java.lang.reflect.Field;

public class FieldWrapper {

    @NonNull private final Field field;
    @NonNull
    private final ClassWrapper wrapper;

    public FieldWrapper(@NonNull ClassWrapper wrapper, @NonNull String name) throws NoSuchFieldException {
        this.wrapper = wrapper;
        this.field = wrapper.getInstanceClass().getDeclaredField(name);
    }

    public Object get() {
        field.setAccessible(true);

        try {
            return field.get(wrapper.getInstance());
        } catch (IllegalAccessException exception) {
            exception.printStackTrace();
        }

        return null;
    }
}
