package utils;

import com.intellij.ide.util.PropertiesComponent;
import java.util.Optional;

/**
 * @creed: Here be dragons !
 * @author: EzioQAQ
 * @Time: 2019-04-20 20:58
 */
public class CacheUtils {

    public static final String KEY_DAO_SUFFIX = "ezio.dao.suffix";

    public static final String VALUE_DAO_SUFFIX_DEFAULT = "Dao";

    public static final String KEY_DAO_ANNOTATION = "ezio.dao.annotation";

    public static final String VALUE_DAO_ANNOTATION_DEFAULT = "Mapper";

    public static final String VALUE_SERVICE_SUFFIX_DEFAULT = "Service";

    public static final String VALUE_SERVICEIMPL_SUFFIX_DEFAULT = "ServiceImpl";

    public static final String KEY_SERVICE_ANNOTATION = "ezio.service.annotation";

    public static final String VALUE_SERVICE_ANNOTATION_DEFAULT = "Service";

    public static final String KEY_CREATE_DAO_XML = "ezio.dao.xml.create";

    public static final Boolean VALUE_CREATE_DAO_XML_DEFAULT = true;

    public static String getValue(String name, String defaultValue) {

        return Optional.ofNullable(getValue(name)).orElse(defaultValue);
    }

    public static Boolean getValueWithBoolean(String name) {

        return Optional.ofNullable(getValue(name)).map(Boolean::valueOf).orElse(false);
    }

    public static String getValue(String name) {

        return PropertiesComponent.getInstance().getValue(name);
    }

    public static void setValue(String name, String value) {
        PropertiesComponent.getInstance().setValue(name, value);
    }
}
