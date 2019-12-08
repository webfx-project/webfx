package webfx.platform.shared.service.datasource;

/**
 * @author Bruno Salmon
 */
public interface QueryTranslator {

    String translateQueryIntoDataSourceDefaultLanguage(String queryLanguage, String query);

}
