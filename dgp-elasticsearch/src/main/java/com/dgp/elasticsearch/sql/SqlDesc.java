package com.dgp.elasticsearch.sql;
import com.dgp.elasticsearch.common.EsOperateConstant;
import com.dgp.elasticsearch.enums.CommandType;
import com.dgp.elasticsearch.enums.EsActionEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class SqlDesc implements Serializable {

	/**
	 * @Fields field:field:{todo}
	 */

	private static final long serialVersionUID = 1L;

	private String index;
	private EsActionEnum actionEnum;
	private String urlParam;
	private Object id;
	private String key;

	private String dsl;

	private CommandType commandType;

	private Class<?> resultType;

	private Class<?> returnType;

	private boolean returnsMany;

	private boolean returnsVoid;

	public String getRequestPath() {
		StringBuffer path = new StringBuffer();
		if (StringUtils.isNotEmpty(index)) {
			path.append("/");
			path.append(index);
		}

		if (actionEnum != null) {
			path.append("/");
			path.append(actionEnum.getValue());
		}
		if (StringUtils.isNotEmpty(urlParam)) {
			path.append("?");
			path.append(urlParam);
		}
		return path.toString();
	}

	public String getRestFulPath() {
		StringBuffer path = new StringBuffer();
		if (StringUtils.isNotEmpty(index)) {
			path.append("/");
			path.append(index);
		}
		if (actionEnum != null) {
			path.append("/");
			path.append(actionEnum.getValue());
		}
		if (id != null) {
			path.append("/");
			path.append(String.valueOf(id));
		}
		return path.toString();
	}

	public String getRestSqlPath() {
		StringBuffer path = new StringBuffer();

		if (actionEnum != null) {
			path.append("/");
			path.append(actionEnum.getValue());
		}

		path.append("?").append(EsOperateConstant.SQL_FORMAT);
		return path.toString();
	}

}
