import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;

import static utils.CacheUtils.KEY_CREATE_DAO_XML;
import static utils.CacheUtils.KEY_DAO_ANNOTATION;
import static utils.CacheUtils.KEY_DAO_SUFFIX;
import static utils.CacheUtils.KEY_SERVICE_ANNOTATION;
import static utils.CacheUtils.VALUE_DAO_ANNOTATION_DEFAULT;
import static utils.CacheUtils.VALUE_DAO_SUFFIX_DEFAULT;
import static utils.CacheUtils.VALUE_SERVICEIMPL_SUFFIX_DEFAULT;
import static utils.CacheUtils.VALUE_SERVICE_ANNOTATION_DEFAULT;
import static utils.CacheUtils.VALUE_SERVICE_SUFFIX_DEFAULT;
import static utils.CacheUtils.getValue;
import static utils.CacheUtils.getValueWithBoolean;
import static utils.GenerateUtils.genClass;
import static utils.GenerateUtils.genFolder;
import static utils.GenerateUtils.genInterface;
import static utils.GenerateUtils.genXML;
import static utils.LogUtils.showInfo;

/**
 * @creed: Here be dragons !
 * @author: EzioQAQ
 * @Time: 2019-04-17 14:53
 */
public class GenTurboAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // TODO: insert action logic here
        gen(event);
        showInfo("创建成功");
    }

    private void gen(AnActionEvent event) {
        PsiFile psiFile = event.getData(LangDataKeys.PSI_FILE);

        assert psiFile != null;
        assert psiFile.getParent() != null;
        PsiDirectory directory = psiFile.getParent().getParentDirectory();
        assert directory != null;
        String name = psiFile.getName().replace("." + psiFile.getFileType().getName().toLowerCase(), "");

        WriteCommandAction.runWriteCommandAction(directory.getProject(), () -> {
            // TODO: Ezio 2019-04-21  文件夹也可以做成可配置的
            PsiDirectory daoDirectory = genFolder(directory, "dao");
            PsiDirectory serviceDirectory = genFolder(directory, "service");
            PsiDirectory implDirectory = genFolder(serviceDirectory, "impl");

            String daoName = name + getValue(KEY_DAO_SUFFIX, VALUE_DAO_SUFFIX_DEFAULT);
            String daoAnnotation = getValue(KEY_DAO_ANNOTATION, VALUE_DAO_ANNOTATION_DEFAULT);
            PsiClass daoInterface = genInterface(daoDirectory, daoName, daoAnnotation);

            String serviceName = name + VALUE_SERVICE_SUFFIX_DEFAULT;
            String serviceImplName = name + VALUE_SERVICEIMPL_SUFFIX_DEFAULT;
            String serviceAnnotation = getValue(KEY_SERVICE_ANNOTATION, VALUE_SERVICE_ANNOTATION_DEFAULT);
            PsiClass serviceInterface = genInterface(serviceDirectory, serviceName, null);
            genClass(implDirectory, serviceImplName, serviceAnnotation, serviceInterface);

            // xml
            if (getValueWithBoolean(KEY_CREATE_DAO_XML)) {
                genXML(daoDirectory, daoName, daoInterface);
            }
        });
    }
}
