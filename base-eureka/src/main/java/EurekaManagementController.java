import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({"/manage"})
public class EurekaManagementController {
    private ApplicationInfoManager applicationInfoManager;

    public EurekaManagementController() {
    }

    @RequestMapping({"online"})
    @ResponseBody
    public Object online() {
        return this.changeInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    @RequestMapping({"offline"})
    @ResponseBody
    public Object offline() {
        return this.changeInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
    }

    public void setApplicationInfoManager(ApplicationInfoManager applicationInfoManager) {
        this.applicationInfoManager = applicationInfoManager;
    }

    private boolean changeInstanceStatus(InstanceInfo.InstanceStatus status) {
        if (null != this.applicationInfoManager && null != status) {
            this.applicationInfoManager.setInstanceStatus(status);
            return true;
        } else {
            return false;
        }
    }

}
