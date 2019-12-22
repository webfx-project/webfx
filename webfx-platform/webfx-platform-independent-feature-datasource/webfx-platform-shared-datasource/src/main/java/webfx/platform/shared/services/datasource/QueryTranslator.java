package webfx.platform.shared.services.datasource;

/**
 * @author Bruno Salmon
 */
public interface QueryTranslator {

    String translateQueryIntoDataSourceDefaultLanguage(String queryLanguage, String query);

}
