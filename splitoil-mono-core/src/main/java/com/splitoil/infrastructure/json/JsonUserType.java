package com.splitoil.infrastructure.json;

import com.fasterxml.jackson.databind.JavaType;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import static jline.internal.Preconditions.checkNotNull;

public class JsonUserType implements UserType, ParameterizedType {

    public static final String LIST = "List";

    public static final String MAP = "Map";

    public static final String OBJECT = "Object";

    public static final String KEY_TYPE = "key";

    public static final String ELEM_TYPE = "value";

    public static final JacksonAdapter jackson = JacksonAdapter.getInstance();

    protected JavaType handlerType;

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.JAVA_OBJECT };
    }

    @Override
    public Class returnedClass() {
        return String.class;
    }

    @Override
    public boolean equals(final Object x, final Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(final Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(final ResultSet rs, final String[] names, final SharedSessionContractImplementor session, final Object owner)
        throws HibernateException, SQLException {

        final String value = rs.getString(names[0]);
        return fromJsonString(value);
    }

    @Override
    public void nullSafeSet(final PreparedStatement st, final Object value, final int index, final SharedSessionContractImplementor session)
        throws HibernateException, SQLException {


        final Object input = firstNonNull(value, JacksonAdapter.NULL_STR);

        final String jsonString = toJsonString(input);
        st.setObject(index, jackson.jsonDecodeTree(jsonString), Types.OTHER);
    }

    private Object firstNonNull(Object first, Object second) {
        return first != null ? first : checkNotNull(second);
    }


    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        final String json = toJsonString(value);
        return fromJsonString(json);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        return (String) deepCopy(value);
    }

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return deepCopy(original);
    }

    @Override public void setParameterValues(final Properties parameters) {

        final Optional<String> objType = Optional.ofNullable(parameters.getProperty(OBJECT));
        if (objType.isPresent()) {
            handlerType = resolveHandlerTypeAsObject(objType.get());
            return;
        }

        final Optional<String> mapObjType = Optional.ofNullable(parameters.getProperty(MAP));
        if (mapObjType.isPresent()) {
            handlerType = resoveHandlerTypeAsMap(parameters);
            return;
        }

        final Optional<String> listObjType = Optional.ofNullable(parameters.getProperty(LIST));
        if (listObjType.isPresent()) {
            handlerType = resoveHandlerTypeAsList(parameters);
            return;
        }

        throw new HibernateException("Illegal type of handler class!");

    }

    private JavaType resolveHandlerTypeAsObject(final String objectCanonicalName) {
        try {
            return jackson.createFromCanonical(objectCanonicalName);
        } catch (final Exception e) {
            throw new HibernateException("Unable to resolve object type", e);
        }
    }

    private JavaType resoveHandlerTypeAsMap(final Properties params) {
        try {
            final String type = params.getProperty(MAP);
            final Class<? extends Map> mapType = getaClass(type).asSubclass(Map.class);

            final String keyType = params.getProperty(KEY_TYPE, Object.class.getName());
            final String valueType = params.getProperty(ELEM_TYPE, Object.class.getName());

            return jackson.createMapLikeType(mapType, getaClass(keyType), getaClass(valueType));
        } catch (final ClassNotFoundException | ClassCastException e) {
            throw new HibernateException("Unable to resolve map type", e);
        }
    }

    private JavaType resoveHandlerTypeAsList(final Properties params) {
        try {
            final String type = params.getProperty(LIST);
            final Class<? extends Collection> listType = getaClass(type).asSubclass(Collection.class);
            final String elemType = params.getProperty(ELEM_TYPE, Object.class.getName());
            final Class<?> subType = getaClass(elemType);
            return jackson.createCollectionLikeType(listType, subType);
        } catch (final ClassNotFoundException | ClassCastException e) {
            throw new HibernateException("Unable to resolve collection type", e);
        }
    }

    protected Class<?> getaClass(final String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    private String toJsonString(final Object value) {
        try {
            if (jackson.isValidJSONString(value)) {
                return value.toString() + "::json";
            }
            return jackson.toJson(value) + "::json";

        } catch (final JsonEncodingException e) {
            throw new HibernateException("Converting object to sql string failed", e);
        }
    }

    private Object fromJsonString(final String value) {
        final Optional<String> rawValue = Optional.ofNullable(value);
        try {
            final String jsonStr = rawValue.orElse(JacksonAdapter.NULL_STR);
            if (handlerType.isTypeOrSubTypeOf(String.class) && jackson.isValidJSONString(jsonStr)) {
                return JacksonAdapter.NULL_STR.equals(jsonStr) ? null : jsonStr;
            }
            return jackson.jsonDecodeByJavaType(jsonStr, handlerType);
        } catch (final JsonDecodingException ex) {
            throw new HibernateException("JSON decoding failed", ex);
        }
    }

}

