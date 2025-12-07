package fr.fgodard;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

import static fr.fgodard.StringHelper.nvl;
import static fr.fgodard.StringHelper.trim;
import static fr.fgodard.logs.LogManager.debugFwk;

/**
 *
 */
public class BeanHelper {

    private static final String NULL_OBJECT_STRING = "null";
    private static final String VOID_OBJECT_STRING = "empty";
    private static final String SUB_OBJECT_SEPARATOR = "; ";
    private static final String COLLECTION_SEPARATOR = ", ";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss:SSS";

    private static final int MAX_DEPTH = 3;

    private static void writeObject(final StringBuilder builder, final int depth, final String propName,
            final Object propValue, final LinkedList written, final String separator, final List<String> excluded) {

        Serializable value = (Serializable) propValue;
        String name = nvl(propName, "");

        if (value == null) {
            writePropName(name, builder);
            builder.append(NULL_OBJECT_STRING);
            return;
        }

        if (propValue instanceof Supplier) {
            value = (Serializable) ((Supplier) propValue).get();
        }

        if (writeSimpleValue(builder, name, value)) {
            return;
        }

        if (value instanceof Object[]) {
            writeObjectArray(builder, name, depth, (Object[]) value, written, separator, excluded);
            return;
        }

        if (value instanceof Collection) {
            writeObjectList(builder, name, depth, (Collection) value, written, separator, excluded);
            return;
        }

        if (value instanceof Map) {
            writeObjectMap(builder, name, depth, (Map) value, written, excluded);
            return;
        }

        // c'est un sous objet
        if (depth > MAX_DEPTH) {
            writePropName(name, builder);
            builder.append(".....");
            return;
        }

        List<BeanAccessor> beanAccessors = BeanAccessor.getAccessors(value);
        if (beanAccessors.isEmpty()) {
            // objet sans propriétés
            writePropName(name, builder);
            builder.append(value.toString());
            return;
        }

        // Sous Objet non null;
        Object fieldValue;

        // �viter les references circulaires entre objets;
        if (written.contains(value)) {
            writePropName(name, builder);
            builder.append("!loop:");
            builder.append(value.toString());
            builder.append("!");
            return;
        }

        written.push(value);
        boolean hasOne = false;
        for (BeanAccessor accessor : beanAccessors) {
            try {
                String fieldName;
                if (trim(name).isEmpty()) {
                    fieldName = accessor.getFieldName();
                } else {
                    fieldName = name.concat(".").concat(accessor.getFieldName());
                }
                if (excluded == null || !excluded.contains(fieldName)) {
                    if (hasOne) {
                        builder.append(separator);
                    }
                    fieldValue = accessor.getValue(value);
                    writeObject(builder, depth + 1, fieldName, fieldValue, written, separator, excluded);
                    hasOne = true;
                }

            } catch (ReflectiveOperationException e) {
                debugFwk(e, "impossible d'acceder à la propriété %1$s du Bean, classe = %2$s", accessor.getFieldName(),
                        value == null ? "inconnue" : value.getClass().getName());
            }
        }
        written.pop();
    }

    private static boolean writeSimpleValue(StringBuilder builder, String propName, Object value) {
        if ((value instanceof CharSequence) || (value instanceof Character)) {
            writePropName(propName, builder);
            builder.append(value);
            return true;
        }

        if ((value instanceof Number) || (value instanceof Boolean)) {
            writePropName(propName, builder);
            builder.append(value.toString());
            return true;
        }

        if (value instanceof Date) {
            writePropName(propName, builder);
            builder.append(DateHelper.format((Date) value, DATE_FORMAT));
            return true;
        }

        if (value instanceof Calendar) {
            writePropName(propName, builder);
            builder.append(DateHelper.format((Calendar) value, DATE_FORMAT));
            return true;
        }

        if (value instanceof Class) {
            writePropName(propName + ".class", builder);
            builder.append(String.valueOf(value));
            return true;
        }
        return false;
    }

    /**
     * Permet de joindre tous les elements d'une liste
     *
     * @param objectList
     *            liste � joindre
     * 
     * @return
     */
    private static boolean writeObjectList(StringBuilder builder, String propName, int depth, Collection objectList,
            LinkedList written, String separator, List<String> excluded) {

        if (objectList.isEmpty()) {
            writePropName(propName, builder);
            builder.append(VOID_OBJECT_STRING);
            return true;
        }

        if (depth > MAX_DEPTH) {
            writePropName(propName, builder);
            builder.append(".....");
            return true;
        }

        final String propertyStr = propName.concat("(%s)");

        int i = 0;
        Iterator listIter = objectList.iterator();
        while (listIter.hasNext()) {
            if (i > 0) {
                builder.append(separator);
            }
            writePropName(String.format(propertyStr, String.valueOf(i)), builder);
            builder.append("[");
            writeObject(builder, depth, null, listIter.next(), written, COLLECTION_SEPARATOR, excluded);
            builder.append("]");
            i++;
        }
        return i > 0;
    }

    private static boolean writeObjectArray(StringBuilder builder, String propName, int depth, Object[] array,
            LinkedList written, String separator, List<String> excluded) {

        if (array.length == 0) {
            writePropName(propName, builder);
            builder.append(VOID_OBJECT_STRING);
            return true;
        }

        if (depth > MAX_DEPTH) {
            writePropName(propName, builder);
            builder.append(".....");
            return true;
        }

        final String propertyStr = propName.concat("[%s]");
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                builder.append(separator);
            }
            writePropName(String.format(propertyStr, String.valueOf(i)), builder);
            builder.append("[");
            writeObject(builder, depth, null, array[i], written, COLLECTION_SEPARATOR, excluded);
            builder.append("]");
        }
        return true;
    }

    private static boolean writeObjectMap(StringBuilder builder, String propName, int depth,
            Map<Object, Object> objectMap, LinkedList written, List<String> excluded) {

        if (objectMap.isEmpty()) {
            writePropName(propName, builder);
            builder.append(VOID_OBJECT_STRING);
            return true;
        }

        if (depth > MAX_DEPTH) {
            writePropName(propName, builder);
            builder.append(".....");
            return true;
        }

        boolean hasOneField = false;
        writePropName(propName, builder);
        builder.append("[");
        for (Map.Entry entry : objectMap.entrySet()) {
            if (hasOneField) {
                builder.append(COLLECTION_SEPARATOR);
            }
            builder.append("(");
            writeObject(builder, depth, null, entry.getKey(), written, COLLECTION_SEPARATOR, excluded);
            builder.append(")=(");
            writeObject(builder, depth, null, entry.getValue(), written, COLLECTION_SEPARATOR, excluded);
            builder.append(")");
            hasOneField = true;
        }
        builder.append("]");
        return hasOneField;

    }

    private static void writePropName(final String propName, final StringBuilder builder) {
        if (!trim(propName).isEmpty()) {
            builder.append(propName);
            builder.append("=");
        }
    }

    /**
     * Parcours les champs d'un bean pour déterminer si ils sont nuls ou vide.
     *
     * @param bean
     *            l'objet à tester
     * 
     * @return true si l'objet est nul ou vide.
     */
    private static boolean areFieldsNullOrEmpty(Serializable bean) {
        boolean result = true;
        Object[] nullParams = new Object[0];
        // on inspectes chaque valeur en parcourant les accesseurs
        Serializable fieldValue;
        List<BeanAccessor> accessors = BeanAccessor.getAccessors(bean);
        for (BeanAccessor accessor : accessors) {
            try {
                fieldValue = accessor.getValue(bean);
                result = isNullOrEmptyBean(fieldValue);
                if (!result) {
                    // au moins un champ n'est pas vide -> on sors.
                    break;
                }
            } catch (Exception e) {
                // si on ne peut acceder � la valeur de ce champ, on considère qu'il vaut nul.
                // => on ignore l'exception
                debugFwk(e, "impossible d'acceder à la propriété %1$s du Bean de type %2$s", accessor.getFieldName(),
                        bean.getClass().getName());
            }
        }
        return result;
    }

    /**
     * Indique si une liste est nulle ou vide Testes récursivement si les éléments de la liste sont nuls ou vide
     *
     * @param beanList
     *            ! la liste à vérifier.
     * 
     * @return true si la liste est nulle, vide, ou ne contient que des éléments vides.
     */
    public static boolean isNullOrEmptyList(Iterable<Serializable> beanList) {
        if (beanList == null) {
            return true;
        }
        Iterator<Serializable> it = beanList.iterator();
        if (!it.hasNext()) {
            return true;
        }

        Serializable bean;
        while (it.hasNext()) {
            bean = it.next();
            if (!isNullOrEmptyBean(bean)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Testes si un objet est null ou vide vérifie récusivement les valeur des propriétés de l'objet si toutes les
     * propriétés sont nulles ou vide, l'objet est vide
     *
     * @param bean
     *            l'objet
     * 
     * @return true si l'objet est nul ou vide.
     */
    public static boolean isNullOrEmptyBean(Serializable bean) {
        if (bean == null) {
            return true;
        }

        if (bean instanceof Number) {
            return false;
        }

        if (bean instanceof CharSequence) {
            return (((CharSequence) bean).length() == 0);
        }

        if (bean instanceof Iterable) {
            return isNullOrEmptyList((Iterable) bean);
        }

        return areFieldsNullOrEmpty(bean);
    }

    public static StringBuilder writeBean(StringBuilder builder, Serializable bean, String... excluded) {
        if (builder == null) {
            builder = new StringBuilder();
        }

        List<String> excludedList = null;
        if (excluded != null) {
            excludedList = Arrays.asList(excluded);
        }

        writeObject(builder, 0, null, bean, new LinkedList(), SUB_OBJECT_SEPARATOR, excludedList);

        return builder;
    }

    public static String beanToString(Serializable bean, String... excluded) {
        return writeBean(null, bean, excluded).toString();
    }

}
