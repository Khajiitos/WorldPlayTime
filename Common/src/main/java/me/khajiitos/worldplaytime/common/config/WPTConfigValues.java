package me.khajiitos.worldplaytime.common.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.khajiitos.worldplaytime.common.util.Color;

public class WPTConfigValues {

    public static abstract class Value<T> {
        private T value;
        private final T defaultValue;

        private Value(T defaultValue) {
            this.defaultValue = defaultValue;
            this.value = defaultValue;
        }

        public T get() {
            return this.value;
        }

        public void set(T value) {
            this.value = value;
        }

        public void setUnchecked(Object obj) {
            this.value = (T)obj;
        }

        public T getDefault() {
            return this.defaultValue;
        }

        public abstract T read(JsonElement jsonElement);
        public abstract JsonElement write();
    }

    public static class BooleanValue extends Value<Boolean> {
        public BooleanValue(Boolean defaultValue) {
            super(defaultValue);
        }

        @Override
        public Boolean read(JsonElement jsonElement) {
            return jsonElement.isJsonPrimitive() ? jsonElement.getAsJsonPrimitive().getAsBoolean() : this.getDefault();
        }

        @Override
        public JsonElement write() {
            return new JsonPrimitive(this.get());
        }
    }

    public static class EnumValue<T extends Enum<?>> extends Value<T> {
        public EnumValue(T defaultValue) {
            super(defaultValue);
        }

        @Override
        public T read(JsonElement jsonElement) {
            String name = jsonElement.getAsString();

            for (Enum<?> enumConstant : this.getDefault().getClass().getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(name)) {
                    return (T) enumConstant;
                }
            }

            return this.getDefault();
        }

        @Override
        public JsonElement write() {
            return new JsonPrimitive(this.get().name().toLowerCase());
        }
    }

    public static class ColorValue extends Value<Color> {
        public ColorValue(Color defaultValue) {
            super(defaultValue);
        }

        @Override
        public Color read(JsonElement jsonElement) {
            Color color = Color.fromString(jsonElement.getAsString());
            return color != null ? color : this.getDefault();
        }

        @Override
        public JsonElement write() {
            return new JsonPrimitive(this.get().toString());
        }
    }
}
