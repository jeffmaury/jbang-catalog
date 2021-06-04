///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.jetbrains.intellij:plugin-repository-rest-client:2.0.17
import org.jetbrains.intellij.pluginRepository.PluginRepository;
import org.jetbrains.intellij.pluginRepository.PluginRepositoryFactory;
import org.jetbrains.intellij.pluginRepository.model.IntellijUpdateMetadata;
import org.jetbrains.intellij.pluginRepository.model.PluginBean;
import org.jetbrains.intellij.pluginRepository.model.PluginUpdateBean;
import org.jetbrains.intellij.pluginRepository.model.ProductFamily;
import org.jetbrains.intellij.pluginRepository.model.UpdateBean;

import java.util.List;

public class jbstat {

    public static void main(String... args) {
        PluginRepository instance = PluginRepositoryFactory.create();
        PluginBean plugin = instance.getPluginManager().getPluginByXmlId(args[0],
        ProductFamily.INTELLIJ);
        List<UpdateBean> versions = instance.getPluginManager().getPluginVersions(plugin.getId());
        for(UpdateBean version : versions) {
            IntellijUpdateMetadata data = instance.getPluginUpdateManager().getIntellijUpdateMetadata(version.getPluginId(), version.getId());
            try {
                PluginUpdateBean update = instance.getPluginUpdateManager().getUpdateById(data.getId());
                System.out.println(args[0] + ";" + update.getVersion() + ";" + update.getDownloads());
            } catch (Exception e) {}
        }
    }
}
