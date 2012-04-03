package com.google.code.jee.utils.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;

import com.google.code.jee.utils.StringUtil;
import com.google.code.jee.utils.collection.ArrayUtil;
import com.google.code.jee.utils.collection.MapUtil;

/**
 * Class ClassUtil.
 */
public final class ClassUtil extends ClassUtils {
    private static final Logger LOGGER = Logger.getLogger(ClassUtil.class);

    /**
     * Constructor of the class ClassUtil.
     */
    private ClassUtil() {
    }

    /**
     * Gets the method name.
     * 
     * @return the method name
     */
    public static String getEnclosingMethodName() {
        return getEnclosingMethodName(1);
    }

    /**
     * Gets the enclosing method name.
     * 
     * @param depth the depth
     * @return the enclosing method name
     */
    public static String getEnclosingMethodName(int depth) {
        String methodName = null;
        final StackTraceElement[] ste = new Throwable().getStackTrace();
        if (!ArrayUtils.isEmpty(ste) && ste.length > depth) {
            methodName = ste[depth].getMethodName();
        }
        return methodName;
    }

    /**
     * Method instantiateClass.
     * 
     * @param <T> the generic type
     * @param className the className
     * @return Object
     */
    @SuppressWarnings ("unchecked")
    public static <T> T instantiateClass(String className) {
        T object = null;
        if (!StringUtil.isBlank(className)) {
            try {
                object = ClassUtil.instantiateClass((Class<T>) Class.forName(className));
            } catch (final ClassNotFoundException e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }
        }
        return object;
    }

    /**
     * Method instantiateClass.
     * 
     * @param <T> the generic type
     * @param clazz the clazz
     * @return T
     */
    @SuppressWarnings ("unchecked")
    public static <T> T instantiateClass(Class<T> clazz) {
        T object = null;
        if (clazz != null) {
            final Constructor<?>[] arrayConstructor = clazz.getDeclaredConstructors();
            for (final Constructor<?> constructor : arrayConstructor) {
                if (constructor.isAccessible()) {
                    try {
                        object = clazz.newInstance();
                    } catch (final InstantiationException e1) {
                        final String msg = "[The " + clazz.getName() + " class could not be instantiated]";
                        LOGGER.fatal(msg);
                    } catch (final IllegalAccessException e1) {
                        final String msg = "[The constructor of the " + clazz.getName() + " class is not accessible]";
                        LOGGER.fatal(msg);
                    }
                } else {
                    constructor.setAccessible(true);
                    final Object[] emptyArguments = {};
                    try {
                        object = (T) constructor.newInstance(emptyArguments);
                    } catch (final IllegalArgumentException e1) {
                        final String msg = "[The constructor of the " + clazz.getName()
                                + "class has at least one argument ]";
                        LOGGER.fatal(msg);
                    } catch (final InstantiationException e1) {
                        final String msg = "[The " + clazz.getName() + " class could not be instantiated]";
                        LOGGER.fatal(msg);
                    } catch (final IllegalAccessException e1) {
                        final String msg = "[The constructor of the " + clazz.getName() + " class is not accessible]";
                        LOGGER.fatal(msg);
                    } catch (final InvocationTargetException e1) {
                        final String msg = "[The " + clazz.getName() + " class could not be invoked dynamically]";
                        LOGGER.fatal(msg);
                    }
                }
            }
        }
        return object;
    }

    /**
     * Method invokeMethod.
     * 
     * @param className the className
     * @param methodName the methodName
     * @param arguments the arguments
     * @return Object
     */
    public static Object invokeMethod(String className, String methodName, Object[] arguments) {
        final Object invokeConstructor = ClassUtil.instantiateClass(className);
        return ClassUtil.invokeMethod(invokeConstructor, methodName, arguments);
    }

    /**
     * Method invokeMethod.
     * 
     * @param instanceObject the instanceObject
     * @param methodName the methodName
     * @param arguments the arguments
     * @return Object
     */
    public static Object invokeMethod(Object instanceObject, String methodName, Object[] arguments) {
        Object invokeMethod = null;
        try {
            invokeMethod = MethodUtils.invokeMethod(instanceObject, methodName, arguments);
        } catch (final NoSuchMethodException e1) {
            final String msg = "[The " + methodName + " method from the " + instanceObject.getClass()
                    + " class does not exist]";
            LOGGER.fatal(msg);
        } catch (final IllegalAccessException e1) {
            final String msg = "[The " + methodName + " method from the " + instanceObject.getClass()
                    + " class is not public]";
            LOGGER.fatal(msg);
        } catch (final InvocationTargetException e1) {
            final String msg = "[The " + methodName + " method from the " + instanceObject.getClass()
                    + " class could not be invoked dynamically]";
            LOGGER.fatal(msg);
        }
        return invokeMethod;
    }

    /**
     * Returns all the fields (public & private) declared in the whole class
     * hierachy of a given class.
     * 
     * @param clazz any class
     * @return List
     */
    public static List<Field> getFields(Class<?> clazz) {
        List<Field> fields = null;
        if (clazz != null) {
            fields = new ArrayList<Field>();
            Class<?> currentClass = clazz;
            do {
                fields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
                currentClass = currentClass.getSuperclass();
            } while (currentClass != null);
        }
        return fields;
    }

    /**
     * Gets the real fields.
     * 
     * @param clazz the clazz
     * @return the real fields
     */
    public static Map<String, Type> getRealFields(Class<?> clazz) {
        final Map<String, Type> realFieldsType = new LinkedHashMap<String, Type>();
        if (clazz != null) {
            final List<Field> fields = getFields(clazz);
            if (!CollectionUtils.isEmpty(fields)) {
                final Map<String, Type> typeParameters = getTypeParameters(clazz);

                for (final Field field : fields) {
                    final Type realType = getRealType(field, typeParameters);
                    realFieldsType.put(field.getName(), realType);
                }
            }
        }
        return realFieldsType;
    }

    /**
     * Gets the real type.
     * 
     * @param clazz the clazz
     * @param field the field
     * @return the real type
     */
    public static Type getRealType(Class<?> clazz, Field field) {
        Type realType = null;
        if (clazz != null && field != null) {
            final Map<String, Type> typeParameters = getTypeParameters(clazz);
            realType = getRealType(field, typeParameters);
        }
        return realType;
    }

    /**
     * Gets the real type.
     * 
     * @param clazz the clazz
     * @param method the method
     * @return the real type
     */
    public static Type getRealType(Class<?> clazz, Method method) {
        Type realType = null;
        if (clazz != null && method != null) {
            final Map<String, Type> typeParameters = getTypeParameters(clazz);
            realType = getRealType(method, typeParameters);
        }
        return realType;
    }

    /**
     * Gets the real type.
     * 
     * @param field the field
     * @param typeParameters the type parameters
     * @return the real type
     */
    private static Type getRealType(Field field, Map<String, Type> typeParameters) {
        Type realType = null;
        if (field != null && !MapUtil.isEmpty(typeParameters)) {
            if (field.getGenericType() instanceof TypeVariable<?>) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>) field.getGenericType();
                realType = typeParameters.get(typeVariable.getName());
            } else if (field.getGenericType() instanceof ParameterizedType) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>) ((ParameterizedType) field.getGenericType())
                        .getActualTypeArguments()[0];
                realType = typeParameters.get(typeVariable.getName());
            } else {
                realType = field.getType();
            }
        } else {
            realType = field.getType();
        }
        return realType;
    }

    /**
     * Gets the real type.
     * 
     * @param method the method
     * @param typeParameters the type parameters
     * @return the real type
     */
    private static Type getRealType(Method method, Map<String, Type> typeParameters) {
        Type realType = null;
        if (method != null && !MapUtil.isEmpty(typeParameters)) {
            if (method.getGenericReturnType() instanceof TypeVariable<?>) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>) method.getGenericReturnType();
                realType = typeParameters.get(typeVariable.getName());
            } else if (method.getGenericReturnType() instanceof ParameterizedType) {
                final TypeVariable<?> typeVariable = (TypeVariable<?>) ((ParameterizedType) method
                        .getGenericReturnType()).getActualTypeArguments()[0];
                realType = typeParameters.get(typeVariable.getName());
            } else {
                realType = method.getReturnType();
            }
        } else {
            realType = method.getReturnType();
        }
        return realType;
    }

    /**
     * Gets the type parameters.
     * 
     * @param clazz the clazz
     * @return the type parameters
     */
    public static Map<String, Type> getTypeParameters(Class<?> clazz) {
        Map<String, Type> typeParameters = null;

        if (clazz != null) {
            typeParameters = new HashMap<String, Type>();

            Class<?> currentClass = clazz;
            do {
                if (currentClass.getGenericSuperclass() instanceof ParameterizedType) {
                    final ParameterizedType superClass = (ParameterizedType) currentClass.getGenericSuperclass();
                    final Type[] actualTypeArguments = superClass.getActualTypeArguments();
                    final TypeVariable<?>[] typeVariables = ((Class<?>) superClass.getRawType()).getTypeParameters();
                    if (!ArrayUtils.isEmpty(actualTypeArguments) && !ArrayUtils.isEmpty(typeVariables)) {
                        for (int i = 0; i < typeVariables.length; i++) {
                            final TypeVariable<?> typeVariable = typeVariables[i];
                            final Type actualType = actualTypeArguments[i];
                            if (!typeParameters.containsKey(typeVariable.getName())) {
                                typeParameters.put(typeVariable.getName(), actualType);
                            }
                        }
                    }
                }

                currentClass = currentClass.getSuperclass();
            } while (currentClass != null);
        }
        return typeParameters;
    }

    /**
     * Returns all the methods (public & private) declared in the whole class
     * hierachy of a given class.
     * 
     * @param clazz any class
     * @return Collection
     */
    public static Collection<Method> getMethods(Class<?> clazz) {
        Collection<Method> methods = null;
        if (clazz != null) {
            methods = new ArrayList<Method>();
            Class<?> currentClass = clazz;
            do {
                methods.addAll(Arrays.asList(currentClass.getDeclaredMethods()));
                currentClass = currentClass.getSuperclass();
            } while (currentClass != null);
        }
        return methods;
    }

    /**
     * Gets the field.
     * 
     * @param clazz the clazz
     * @param name the name
     * @return the field
     */
    public static Field getField(Class<?> clazz, String name) {
        Field field = null;
        if (clazz != null && !StringUtil.isBlank(name)) {
            Class<?> currentClass = clazz;

            while (field == null && currentClass != null) {
                try {
                    field = currentClass.getDeclaredField(name);
                } catch (final SecurityException e) {
                    field = null;
                } catch (final NoSuchFieldException e) {
                    field = null;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return field;
    }

    /**
     * Method getGenericType.
     * 
     * @param field the field
     * @return Class<?>
     */
    public Class<?> getGenericType(Field field) {
        Class<?> clazz = null;
        if (field != null) {
            final ParameterizedType type = (ParameterizedType) field.getGenericType();
            if (type != null && type.getActualTypeArguments() != null && type.getActualTypeArguments().length > 0) {
                clazz = (Class<?>) type.getActualTypeArguments()[0];
            }
        }
        return clazz;
    }

    /**
     * Check if the field is a constant.
     * 
     * @param field the field
     * @return boolean
     */
    public static boolean isConstantField(Field field) {
        boolean constantField = false;
        final int searchModifiers = Modifier.STATIC | Modifier.FINAL;
        if (field != null) {
            final Integer modifiers = field.getModifiers();
            constantField = (modifiers & searchModifiers) == searchModifiers;
        }
        return constantField;
    }

    /**
     * Gets the string type.
     * 
     * @param type the type
     * @return the string type
     */
    public static String getStringType(Type type) {
        String stringType = null;
        if (type instanceof TypeVariable<?>) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            stringType = typeVariable.getName();
        } else if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            stringType = ((Class<?>) parameterizedType.getRawType()).getSimpleName();
            if (!ArrayUtil.isEmpty(parameterizedType.getActualTypeArguments())) {
                final Type[] typeArguments = parameterizedType.getActualTypeArguments();
                stringType += "<";
                for (int i = 0; i < typeArguments.length; i++) {
                    if (i != 0) {
                        stringType += ", ";
                    }
                    stringType += getStringType(typeArguments[i]);
                }
                stringType += ">";
            }
        } else {
            stringType = ((Class<?>) type).getSimpleName();
        }
        return stringType;
    }

    /**
     * Gets the raw type.
     * 
     * @param type the type
     * @return the raw type
     */
    public static Class<?> getRawType(Type type) {
        Class<?> rawType = null;
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            rawType = (Class<?>) parameterizedType.getRawType();
        } else {
            rawType = (Class<?>) type;
        }
        return rawType;
    }

    /**
     * Gets the type parameters.
     * 
     * @param type the type
     * @return the type parameters
     */
    public static Class<?>[] getTypeParameters(Type type) {
        Class<?>[] typeParameters = null;
        if (type != null) {
            if (type instanceof ParameterizedType) {
                final ParameterizedType parameterizedType = (ParameterizedType) type;
                final Type[] params = parameterizedType.getActualTypeArguments();
                if (!ArrayUtil.isEmpty(params)) {
                    typeParameters = new Class<?>[params.length];
                    for (int i = 0; i < params.length; i++) {
                        typeParameters[i] = (Class<?>) params[i];
                    }
                }
            } else {
                final Map<String, Type> types = getTypeParameters((Class<?>) type);
                if (!MapUtil.isEmpty(types)) {
                    typeParameters = types.values().toArray(new Class[0]);
                }
            }
        }
        return typeParameters;
    }

    public static Class<?>[] getReturnType(Type type) {
        Class<?>[] returnType = null;
        if (type instanceof TypeVariable<?>) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            final Type[] bounds = typeVariable.getBounds();
            if (!ArrayUtil.isEmpty(bounds)) {
                returnType = new Class<?>[bounds.length];
                for (int i = 0; i < bounds.length; i++) {
                    returnType[i] = (Class<?>) bounds[i];
                }
            }
        }
        return returnType;
    }

    /**
     * Checks if is override.
     * 
     * @param method the method
     * @return true, if is override
     */
    public static boolean isOverride(Method method) {
        boolean override = false;
        if (method != null) {
            final Class<?> declaringClass = method.getDeclaringClass();
            Class<?> superClass = declaringClass.getSuperclass();
            if (superClass != null) {
                do {
                    override = isOverrideFromClass(method, superClass);
                    superClass = superClass.getSuperclass();
                } while (!override && superClass != null);
            }
            if (!override) {
                final Class<?>[] interfaces = declaringClass.getInterfaces();
                if (!ArrayUtil.isEmpty(interfaces)) {
                    for (final Class<?> interfaceClass : interfaces) {
                        override = isOverrideFromInterface(method, interfaceClass);
                        if (override) {
                            break;
                        }
                    }
                }
            }
        }
        return override;
    }

    /**
     * Checks if is override from class.
     * 
     * @param method the method
     * @param superClass the super class
     * @return true, if is override from class
     */
    private static boolean isOverrideFromClass(Method method, Class<?> superClass) {
        boolean override = false;
        if (method != null && superClass != null) {
            override = isOverride(method, superClass.getMethods());
            if (!override) {
                final Class<?>[] interfaces = superClass.getInterfaces();
                if (!ArrayUtil.isEmpty(interfaces)) {
                    for (final Class<?> interfaceClass : interfaces) {
                        override = isOverrideFromInterface(method, interfaceClass);
                        if (override) {
                            break;
                        }
                    }
                }
            }
        }
        return override;
    }

    /**
     * Checks if is override from interface.
     * 
     * @param method the method
     * @param interfaceClass the interface class
     * @return true, if is override from interface
     */
    private static boolean isOverrideFromInterface(Method method, Class<?> interfaceClass) {
        boolean override = false;
        if (method != null && interfaceClass != null) {
            override = isOverride(method, interfaceClass.getMethods());
            if (!override && !ArrayUtil.isEmpty(interfaceClass.getInterfaces())) {
                for (final Class<?> currentInterface : interfaceClass.getInterfaces()) {
                    override = isOverrideFromInterface(method, currentInterface);
                    if (override) {
                        break;
                    }
                }
            }
        }
        return override;
    }

    /**
     * Checks if is override.
     * 
     * @param method the method
     * @param superMethods the super methods
     * @return true, if is override
     */
    private static boolean isOverride(Method method, final Method[] superMethods) {
        boolean override = false;
        if (!ArrayUtil.isEmpty(superMethods)) {
            for (final Method superMethod : superMethods) {
                override = isOverride(method, superMethod);
                if (override) {
                    break;
                }
            }
        }
        return override;
    }

    /**
     * Checks if is override.
     * 
     * @param method the method
     * @param superMethod the super method
     * @return true, if is override
     */
    private static boolean isOverride(Method method, final Method superMethod) {
        boolean override = false;
        if (method != null && superMethod != null) {
            override = superMethod.getName().equals(method.getName());
            override = override && !method.getDeclaringClass().equals(superMethod.getDeclaringClass());

            if (override && superMethod.getGenericReturnType() instanceof TypeVariable<?>) {
                final Type type = getRealType(superMethod.getDeclaringClass(), superMethod);
                override = false;
                if (type != null) {
                    override = ((Class<?>) type).isAssignableFrom(method.getReturnType());
                }
            } else {
                override = override && superMethod.getReturnType().equals(method.getReturnType());
            }
            override = override && ArrayUtil.isSameLength(superMethod.getParameterTypes(), method.getParameterTypes());
            if (override && !ArrayUtil.isEmpty(method.getParameterTypes())) {
                override = false;
                final Class<?>[] parameterTypes = method.getParameterTypes();
                final Class<?>[] superParameterTypes = superMethod.getParameterTypes();
                for (int i = 0; i < parameterTypes.length; i++) {
                    override = superParameterTypes[i].isAssignableFrom(parameterTypes[i]);
                    if (!override) {
                        break;
                    }
                }
            }

        }
        return override;
    }

    /**
     * Checks if is getter.
     * 
     * @param method the method
     * @return true, if is getter
     */
    public static boolean isGetter(Method method) {
        boolean getter = false;
        if (method != null) {
            getter = getFieldFromGetter(method) != null;
        }
        return getter;
    }

    /**
     * Gets the field from getter.
     * 
     * @param method the method
     * @return the field from getter
     */
    public static Field getFieldFromGetter(Method method) {
        Field fieldFromGetter = null;
        if (method != null) {
            if (isGetterType(method)) {
                final Class<?> declaringClass = method.getDeclaringClass();
                fieldFromGetter = getFieldFromGetter(method, declaringClass.getDeclaredFields());
                if (fieldFromGetter == null) {
                    Class<?> superClass = declaringClass.getSuperclass();
                    if (superClass != null) {
                        do {
                            fieldFromGetter = getFieldFromGetter(method, superClass.getDeclaredFields());
                            superClass = superClass.getSuperclass();
                        } while (fieldFromGetter == null && superClass != null);
                    }
                }
            }
        }
        return fieldFromGetter;
    }

    /**
     * Gets the field from getter.
     * 
     * @param method the method
     * @param fields the fields
     * @return the field from getter
     */
    private static Field getFieldFromGetter(Method method, Field[] fields) {
        Field fieldFromGetter = null;
        if (method != null && !ArrayUtil.isEmpty(fields)) {
            for (final Field field : fields) {
                final boolean getterForField = isGetterForField(method, field);
                if (getterForField) {
                    fieldFromGetter = field;
                    break;
                }
            }
        }
        return fieldFromGetter;
    }

    /**
     * Checks if is getter for field.
     * 
     * @param method the method
     * @param field the field
     * @return true, if is getter for field
     */
    public static boolean isGetterForField(Method method, Field field) {
        boolean getterForField = false;
        if (method != null && field != null) {
            getterForField = isGetterType(method) && method.getReturnType().equals(field.getType());
            if (getterForField) {
                String fieldName = null;
                if (StringUtil.startsWith(method.getName(), "get")) {
                    fieldName = StringUtil.removeStart(method.getName(), "get");
                } else if (StringUtil.startsWith(method.getName(), "is")) {
                    fieldName = StringUtil.removeStart(method.getName(), "is");
                } else if (StringUtil.startsWith(method.getName(), "has")) {
                    fieldName = StringUtil.removeStart(method.getName(), "has");
                }
                if (!StringUtil.isBlank(fieldName)) {
                    fieldName = StringUtil.toLowercaseFirstCharacter(fieldName);
                    getterForField = fieldName.equals(field.getName());
                } else {
                    getterForField = false;
                }
            }
        }
        return getterForField;
    }

    /**
     * Checks if is getter type.
     * 
     * @param method the method
     * @return true, if is getter type
     */
    public static boolean isGetterType(Method method) {
        boolean getterType = false;
        if (method != null) {
            getterType = !method.getReturnType().equals(void.class);
            getterType = getterType && Modifier.isPublic(method.getModifiers());
            getterType = getterType
                    && (StringUtil.startsWith(method.getName(), "get") || (StringUtil
                            .startsWith(method.getName(), "is") || StringUtil.startsWith(method.getName(), "has"))
                            && (method.getReturnType().equals(Boolean.class) || method.getReturnType().equals(
                                    boolean.class)));
            getterType = getterType && ArrayUtil.isEmpty(method.getParameterTypes());
        }
        return getterType;
    }

    /**
     * Checks if is setter.
     * 
     * @param method the method
     * @return true, if is setter
     */
    public static boolean isSetter(Method method) {
        boolean setter = false;
        if (method != null) {
            setter = getFieldFromSetter(method) != null;
        }
        return setter;
    }

    /**
     * Gets the field from setter.
     * 
     * @param method the method
     * @return the field from setter
     */
    public static Field getFieldFromSetter(Method method) {
        Field fieldFromSetter = null;
        if (method != null) {
            if (isSetterType(method)) {
                final Class<?> declaringClass = method.getDeclaringClass();
                fieldFromSetter = getFieldFromSetter(method, declaringClass.getDeclaredFields());
                if (fieldFromSetter == null) {
                    Class<?> superClass = declaringClass.getSuperclass();
                    if (superClass != null) {
                        do {
                            fieldFromSetter = getFieldFromSetter(method, superClass.getDeclaredFields());
                            superClass = superClass.getSuperclass();
                        } while (fieldFromSetter == null && superClass != null);
                    }
                }
            }
        }
        return fieldFromSetter;
    }

    /**
     * Gets the field from setter.
     * 
     * @param method the method
     * @param fields the fields
     * @return the field from setter
     */
    private static Field getFieldFromSetter(Method method, Field[] fields) {
        Field fieldFromSetter = null;
        if (method != null && !ArrayUtil.isEmpty(fields)) {
            for (final Field field : fields) {
                final boolean setterForField = isSetterForField(method, field);
                if (setterForField) {
                    fieldFromSetter = field;
                    break;
                }
            }
        }
        return fieldFromSetter;
    }

    /**
     * Checks if is setter for field.
     * 
     * @param method the method
     * @param field the field
     * @return true, if is setter for field
     */
    public static boolean isSetterForField(Method method, Field field) {
        boolean setterForField = false;
        if (method != null && field != null) {
            setterForField = isSetterType(method) && method.getParameterTypes()[0].equals(field.getType());
            if (setterForField) {
                setterForField = method.getParameterTypes()[0].equals(field.getType());
                String fieldName = StringUtil.removeStart(method.getName(), "set");
                fieldName = StringUtil.toLowercaseFirstCharacter(fieldName);
                setterForField = fieldName.equals(field.getName());
            }
        }
        return setterForField;
    }

    /**
     * Checks if is setter type.
     * 
     * @param method the method
     * @return true, if is setter type
     */
    public static boolean isSetterType(Method method) {
        boolean setterType = false;
        if (method != null) {
            setterType = method.getReturnType().equals(void.class);
            setterType = setterType && Modifier.isPublic(method.getModifiers());
            setterType = setterType && StringUtil.startsWith(method.getName(), "set");
            setterType = setterType && !ArrayUtil.isEmpty(method.getParameterTypes())
                    && method.getParameterTypes().length == 1;
        }
        return setterType;
    }

    /**
     * Finds the field.
     * 
     * @param clazz the clazz
     * @param name the name
     * @return the field
     */
    public static Field findField(Class<?> clazz, String name) {
        Field field = null;
        if (clazz != null && !StringUtil.isBlank(name)) {
            if (StringUtil.contains(name, ".")) {
                Class<?> currentClass = clazz;
                String currentName = name;

                while (!StringUtil.isBlank(currentName) && StringUtil.contains(currentName, ".")) {
                    final String fieldName = StringUtil.substringBefore(currentName, ".");
                    if (!StringUtil.isBlank(fieldName)) {
                        field = getField(currentClass, fieldName);
                        if (field == null) {
                            break;
                        }
                    }

                    currentName = StringUtil.substringAfter(currentName, ".");
                    currentClass = field.getType();
                }

                if (currentClass != null && !StringUtil.isBlank(currentName)) {
                    field = getField(currentClass, currentName);
                }
            } else {
                field = getField(clazz, name);
            }
        }
        return field;
    }

}
