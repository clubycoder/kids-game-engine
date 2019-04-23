package luby.kids.game.utils.gson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.*;

public class JsonDeserializerWithValidation<T> implements JsonDeserializer<T> {
    @Override
    public T deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        T pojo = new Gson().fromJson(jsonElement, type);
        checkRequiredFields(pojo.getClass().getDeclaredFields(), pojo);
        checkSuperClasses(pojo);
        return pojo;
    }

    private void checkRequiredFields(Field[] fields, Object pojo) throws JsonParseException {
        // Checking nested list items too.
        if (pojo instanceof List) {
            final List pojoList = (List) pojo;
            for (final Object pojoListPojo : pojoList) {
                checkRequiredFields(pojoListPojo.getClass().getDeclaredFields(), pojoListPojo);
                checkSuperClasses(pojoListPojo);
            }
        }

        for (Field f : fields) {
            // If some field has required annotation.
            if (f.getAnnotation(SerializedRequired.class) != null) {
                try {
                    // Trying to read this field's value and check that it truly has value.
                    f.setAccessible(true);
                    Object fieldObject = f.get(pojo);
                    if (fieldObject == null) {
                        // Required value is null - throwing error.
                        throw new JsonParseException(String.format("%1$s -> %2$s",
                                pojo.getClass().getSimpleName(),
                                f.getName()));
                    } else {
                        checkRequiredFields(fieldObject.getClass().getDeclaredFields(), fieldObject);
                        checkSuperClasses(fieldObject);
                    }
                }

                // Exceptions while reflection.
                catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new JsonParseException(e);
                }
            }
        }
    }

    private void checkSuperClasses(Object pojo) throws JsonParseException {
        Class<?> superclass = pojo.getClass();
        while ((superclass = superclass.getSuperclass()) != null) {
            checkRequiredFields(superclass.getDeclaredFields(), pojo);
        }
    }
}
