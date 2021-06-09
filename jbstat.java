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
	private static final String[] DEFAULT_ARGS = {
		"org.jboss.tools.intellij.openshift",
		"org.jboss.tools.intellij.analytics",
		"com.redhat.devtools.intellij.quarkus",
		"com.redhat.devtools.intellij.tekton",
		"com.redhat.devtools.intellij.knative",
		"com.redhat.devtools.intellij.kubernetes",
		"com.redhat.devtools.intellij.telemetry",
		"com.redhat.devtools.intellij.rsp"
	};

    public static void main(String... args) {
        PluginRepository instance = PluginRepositoryFactory.create();
		if (args.length == 0) {
			args = DEFAULT_ARGS;
		}
		for(String id : args) {
			dump(id, instance);
		}
    }
	
    public static void dump(String id, PluginRepository instance) {
        PluginBean plugin = instance.getPluginManager().getPluginByXmlId(id, ProductFamily.INTELLIJ);
        List<UpdateBean> versions = instance.getPluginManager().getPluginVersions(plugin.getId());
        for(UpdateBean version : versions) {
            IntellijUpdateMetadata data = instance.getPluginUpdateManager().getIntellijUpdateMetadata(version.getPluginId(), version.getId());
            try {
                PluginUpdateBean update = instance.getPluginUpdateManager().getUpdateById(data.getId());
				if (update.getChannel() == null || update.getChannel().length() == 0) {
					System.out.println(id + ";" + update.getVersion() + ";" + update.getDownloads());
				}
            } catch (Exception e) {
		    e.printStackTrace();
	    }
        }
    }	
}
