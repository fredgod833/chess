package com.fgodard;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class BeanAccessor {

    private static final Map<String, List<BeanAccessor>> ACCESSORS = new HashMap<>();
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Object[] VOID_PARAMS = new Object[0];
    
    private String fieldName;
    private final Method accessor;
    private Method modifier = null;
    
    private final Field field;
    

    public String getFieldName() {
        return (field == null) ? fieldName : field.getName();
    }

    private BeanAccessor(Method method) {
        if (method.getName().startsWith("get") || method.getName().startsWith("has")) {
            fieldName = method.getName().substring(3);
        }

        if (method.getName().startsWith("is")) {
            fieldName = method.getName().substring(2);
        }
        accessor = method;
        field = getField(method);

    }

    public Class getType() {
        return accessor.getReturnType();
    }

    public <T extends Serializable> void setValue(Serializable bean, T value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (field != null) {
            setValueByField(bean, value);
            return;
        }
        if (modifier == null) {
            modifier = bean.getClass().getMethod("set".concat(fieldName), accessor.getReturnType());
        }
        modifier.invoke(bean, value);
    }

    public Serializable getValue(Serializable bean) throws ReflectiveOperationException {
        if (field != null) {
            return getValueByField(bean);
        }
        try {
            return (Serializable) accessor.invoke(bean, VOID_PARAMS);
        } catch (IllegalArgumentException e) {
            // debug (e,"Erreur de lecture du bean %1$s.",String.valueOf(bean));
            return "?";
        }
    }

    /**
     * Détermines si une méthode correspond à un accesseur d'un bean
     * 
     * @param method
     *            la méthode à vérifier
     * 
     * @return true si la méthode est un accesseur
     */
    private static boolean isAccessor(Method method) {

        // premiere condition : l'accesseur est public
        boolean result = Modifier.isPublic(method.getModifiers());
        // seconde condition : accesseur n'a pas de paramètre
        result = result && (method.getParameterTypes().length == 0);
        // troisieme condition : la méthode retourne un resultat
        Class returntype = method.getReturnType();
        result = result && (returntype != null) && (Serializable.class.isAssignableFrom(returntype) || Iterable.class.isAssignableFrom(returntype));
        // quatrieme condition : le nom de l'accesseur commence par "get" ou "is"
        result = result && (method.getName().startsWith("get") || method.getName().startsWith("has")
                || (method.getName().startsWith("is")));
        // cinquieme condition : ce n'est pas la méthode getClass ou getDeclaringClass()
        result = result && !method.getName().equals("getClass") && !method.getName().equals("getDeclaringClass")
                && !method.getName().equals("hashCode");
        // septieme condition : possède un champ correspondant.
        // result = result && ( getField(method) != null );
        return result;
    }

    /**
     * Retroune la liste des accesseurs (méthodes getxxx) qui permettent d'accéder aux propriétées des Beans.
     */
    public static final List<BeanAccessor> getAccessors(Object object) {
        Class clazz = object.getClass();
        String mapKey = clazz.getName().concat(String.valueOf(clazz.hashCode()));
        List<BeanAccessor> result;

        try {
            LOCK.readLock().lock();
            result = ACCESSORS.get(mapKey);
            if (result != null) {
                return result;
            }
        } finally {
            LOCK.readLock().unlock();
        }
        
        // debug("recherche des accesseurs pour la classe %1$s", clazz.getName());
        result = new ArrayList<>();
        java.lang.reflect.Method[] objMethods = clazz.getMethods();
        for (Method objMethod : objMethods) {
            if (BeanAccessor.isAccessor(objMethod)) {
                try {
                    objMethod.invoke(object, VOID_PARAMS);
                    result.add(new BeanAccessor(objMethod));
                    // debug("méthode %1$s valide pour la classe %2$s.", objMethod.getName(), clazz.getName());
                } catch (ReflectiveOperationException e) {
                    // pas de trace exceptionellement car classe utilisé par le logger.
                    // accesseur inaccessible
                    // debug(e, "Methode %1$s inaccessible pour la classe %2$s", objMethod.getName(), clazz.getName());
                }
            }
        }  
            
        try {
            LOCK.writeLock().lock();
            ACCESSORS.put(mapKey, result);
            return result;
            
        } finally {
            LOCK.writeLock().unlock();
        }
    }

    private static Field getField(Method method) {
        Field field;
        String name = "";
        if (method.getName().startsWith("get") || method.getName().startsWith("has")) {
            name = method.getName().substring(3);
        }

        if (method.getName().startsWith("is")) {
            name = method.getName().substring(2);
        }

        String f = "";

        if (name.length() > 0) {
            f = name.substring(0, 1).toUpperCase();
        }
        if (name.length() > 1) {
            f += name.substring(1);
        }

        try {
            field = method.getDeclaringClass().getField(f);
            field.setAccessible(true);
        } catch (NoSuchFieldException e1) {
            try {
                field = method.getDeclaringClass().getField(name);
                field.setAccessible(true);
            } catch (NoSuchFieldException e2) {
                // pas de trace ici pour éviter le bouclage avec le logger.
                return null;
            }
        }
        if (field.getType().isAssignableFrom(method.getReturnType())) {
            return field;
        }
        return null;
    }

    public <T extends Serializable> void setValueByField(Object bean, T value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        field.set(bean, value);
    }

    public <B extends Serializable> Serializable getValueByField(B bean) throws ReflectiveOperationException {
        return (Serializable) field.get(bean);
    }
    
}
