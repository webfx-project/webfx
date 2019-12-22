package webfx.platform.shared.services.datasource;

/**
 * @author Bruno Salmon
 */
public interface UpdateTranslator {

    String translateUpdateIntoDataSourceDefaultLanguage(String updateLanguage, String update);

}
