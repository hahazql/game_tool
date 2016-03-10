/**
 * Created by zql on 15/11/24.
 */

import com.muyangren.config.SysConfig;
import com.muyangren.dubbox.controller.config.DubboConfig;
import com.muyangren.dubbox.controller.process.ServiceExport;

/**
 * Created by zql on 15/11/24.
 *
 * @className ProvideService
 * @classUse
 */
public class ProvideService {
	public static final String SHUTDOWN_HOOK_KEY = "dubbo.shutdown.hook";
	private static volatile boolean running = true;

	public static void main(String[] args) {

		Start();

	}

	public static void Start() {
		SysConfig.getInstnce().initConfig();
		DubboConfig config = getDubboConfig();
		ServiceExport serviceExport = new ServiceExport();
		serviceExport.setDubboConfig(config);
		addPackage(serviceExport, SysConfig.getInstnce().getDubbopackage());
		serviceExport.start();

	}

	private static void addPackage(ServiceExport serviceExport,
			String dubbopackages) {

		String[] packages = dubbopackages.split(";");
		for (String dubbopackage : packages) {

			serviceExport.addPackage(dubbopackage);

		}

	}

	private static DubboConfig getDubboConfig() {

		String Zookeeperaddress = SysConfig.getInstnce().getZookeeperaddress();
		DubboConfig config = new DubboConfig("test", Zookeeperaddress);
		return config;

	}

}
