import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ui.GenForm;

import static utils.CacheUtils.*;

/**
 * @creed: Here be dragons !
 * @author: EzioQAQ
 * @Time: 2019-04-20 20:50
 */
public class GenConfigurable implements SearchableConfigurable {

    private GenForm genForm;

    @NotNull @Override
    public String getId() {
        return "EzioGenTurbo";
    }

    @Nls(capitalization = Nls.Capitalization.Title) @Override public String getDisplayName() {
        return this.getId();
    }

    @Nullable @Override
    public JComponent createComponent() {
        if (null == this.genForm) {
            this.genForm = new GenForm();
        }

        return this.genForm.mainPanel;
    }

    @Override
    public boolean isModified() {

        boolean dao = genForm.daoSuffix.getText().equals(getValue(KEY_DAO_SUFFIX, VALUE_DAO_SUFFIX_DEFAULT));
        boolean mapper = genForm.daoAnnotation.getText().equals(getValue(KEY_DAO_ANNOTATION, VALUE_DAO_ANNOTATION_DEFAULT));
        boolean service = genForm.serviceAnnotation.getText().equals(getValue(KEY_SERVICE_ANNOTATION, VALUE_SERVICE_ANNOTATION_DEFAULT));
        boolean xml = genForm.createXml.isSelected() == getValueWithBoolean(KEY_CREATE_DAO_XML);
        return dao || mapper || service || xml;
    }

    @Override public void apply() throws ConfigurationException {
        setValue(KEY_DAO_SUFFIX, genForm.daoSuffix.getText());
        setValue(KEY_DAO_ANNOTATION, genForm.daoAnnotation.getText());
        setValue(KEY_SERVICE_ANNOTATION, genForm.serviceAnnotation.getText());
        setValue(KEY_CREATE_DAO_XML, String.valueOf(genForm.createXml.isSelected()));
    }

    @Override public void reset() {
        genForm.daoSuffix.setText(getValue(KEY_DAO_SUFFIX, VALUE_DAO_SUFFIX_DEFAULT));
        genForm.daoAnnotation.setText(getValue(KEY_DAO_ANNOTATION, VALUE_DAO_ANNOTATION_DEFAULT));
        genForm.serviceAnnotation.setText(getValue(KEY_SERVICE_ANNOTATION, VALUE_SERVICE_ANNOTATION_DEFAULT));
        genForm.createXml.setSelected(getValueWithBoolean(KEY_CREATE_DAO_XML));
    }
}
