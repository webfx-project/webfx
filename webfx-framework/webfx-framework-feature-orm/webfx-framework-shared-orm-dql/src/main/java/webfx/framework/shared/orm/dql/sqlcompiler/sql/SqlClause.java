package webfx.framework.shared.orm.dql.sqlcompiler.sql;

/**
 * @author Bruno Salmon
 */
public enum SqlClause {
    SELECT, // select [...] from X where ...
    UPDATE, // update X set [...] where ...
    INSERT, // insert into X ([...]) values (...)
    DELETE,
    VALUES,
    WHERE,
    GROUP_BY,
    HAVING,
    ORDER_BY,
    LIMIT,
    RETURNING
}
