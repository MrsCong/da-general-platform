package com.dgp.elasticsearch.connection;

import com.dgp.common.utils.EnvUtil;
import com.dgp.common.utils.ReflectionUtils;
import com.dgp.elasticsearch.common.EsMapper;
import com.dgp.elasticsearch.common.EsMapperMethod;
import com.dgp.elasticsearch.enums.CommandType;
import com.dgp.elasticsearch.enums.EsActionEnum;
import com.dgp.elasticsearch.sql.SqlDesc;
import com.dgp.elasticsearch.support.DSLConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.reflection.TypeParameterResolver;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
public class EsRequest implements Serializable {

    /**
     * @Fields field:field:{todo}
     */

    private static final long serialVersionUID = 1L;
    private static final Map<String, SqlDesc> sqlDesMap = new HashMap<String, SqlDesc>();
    private Object[] parametr;
    private Method method;
    private Class<?> mapperInterface;
    private EsDataSource dataSource;
    private DSLConfiguration dslConfiguration;
    private SqlDesc sqlDesc = new SqlDesc();

    public EsRequest(Method method, Object[] parametr, Class<?> mapperInterface, EsDataSource dataSource) {
        this.method = method;
        this.parametr = parametr;
        this.mapperInterface = mapperInterface;
        this.dataSource = dataSource;
        this.dslConfiguration = dataSource.getDslConfiguration();
    }

    public Object invoke() {

        SqlDesc sqlDesc = createSqlDesc();
        return dataSource.getEsConnection().execute(sqlDesc);

    }

    private SqlDesc createSqlDesc() {

        String mapperStateMentId = method.getDeclaringClass().getName() + "." + method.getName();
        sqlDesc = sqlDesMap.get(mapperStateMentId);
        if (sqlDesc != null) {
            return sqlDesc;
        } else {
            sqlDesc = new SqlDesc();
        }

        // 将参数转换成Map对象
        ParamNameResolver paramNameResolver = new ParamNameResolver(dslConfiguration, method);
        Object param = paramNameResolver.getNamedParams(parametr);

        getIndexAndParam();
        if (param != null) {
            try {
                Object id = null;
                if (param instanceof Map) {
                    Map<String, Object> maptemp = (Map<String, Object>) param;
                    id = maptemp.get(sqlDesc.getKey());

                } else {
                    id = ReflectionUtils.getFieldValue(param, sqlDesc.getKey());
                }
                sqlDesc.setId(id);
            } catch (Exception e) {
            }

        }

        // 获取MappedStatement
        MappedStatement statement = dslConfiguration.getMappedStatement(mapperStateMentId);
        // 生成dsl语句
        String dsl = statement.getBoundSql(param).getSql();
        log.debug("dsl=:{}", dsl);
        sqlDesc.setDsl(dsl);
        // 获取操作类型
        String command = statement.getSqlCommandType().name();
        sqlDesc.setCommandType(CommandType.valueOf(command));

        Class<?> returnType;
        Class<?> resultType;
        // 获取方法返回结果对象类型
        Type resolvedReturnType = TypeParameterResolver.resolveReturnType(method, mapperInterface.getClass());
        if (resolvedReturnType instanceof Class<?>) {
            returnType = (Class<?>) resolvedReturnType;
            resultType = returnType;
        } else if (resolvedReturnType instanceof ParameterizedType) {
            returnType = (Class<?>) ((ParameterizedType) resolvedReturnType).getRawType();
            resultType = (Class<?>) ((ParameterizedType) resolvedReturnType).getActualTypeArguments()[0];
        } else {
            returnType = method.getReturnType();
            resultType = returnType;
        }
        sqlDesc.setResultType(resultType);
        boolean returnsMany = dslConfiguration.getObjectFactory().isCollection(returnType) || returnType.isArray();
        boolean returnsVoid = void.class.equals(returnType);

        sqlDesc.setReturnType(returnType);
        sqlDesc.setReturnsMany(returnsMany);
        sqlDesc.setReturnsVoid(returnsVoid);

        // sqlDesMap.put(mapperStateMentId, sqlDesc);
        return sqlDesc;

    }

    /**
     * LQL 2021年12月10日
     *
     * @return void
     * @throws
     * @Title: getIndexAndParam
     * @Description: 处理es 索引和url参数
     */
    public void getIndexAndParam() {

        EsMapper esMapperAnnotation = method.getDeclaringClass().getAnnotation(EsMapper.class);
        EsMapperMethod methodAnnotation = method.getAnnotation(EsMapperMethod.class);
        String mappperIndex = null;
        String methodIndex = null;
        EsActionEnum action = null;
        String key = null;
        String urlParam = null;
        if (esMapperAnnotation != null) {
            mappperIndex = esMapperAnnotation.index();
        }
        if (methodAnnotation != null) {
            methodIndex = methodAnnotation.index();
            action = methodAnnotation.action();
            urlParam = methodAnnotation.param();
            key = methodAnnotation.key();
        }
        sqlDesc.setIndex(EnvUtil.getEnvEsIndex(mappperIndex));
        sqlDesc.setActionEnum(action);
        sqlDesc.setUrlParam(urlParam);
        sqlDesc.setKey(key);

    }

}
