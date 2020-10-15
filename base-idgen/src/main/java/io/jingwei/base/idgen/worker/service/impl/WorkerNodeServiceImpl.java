package io.jingwei.base.idgen.worker.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jingwei.base.idgen.worker.entity.WorkerNode;
import io.jingwei.base.idgen.worker.mapper.WorkerNodeMapper;
import io.jingwei.base.idgen.worker.service.IWorkerNodeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * DB WorkerID Assigner for UID Generator 服务实现类
 * </p>
 *
 * @author Andy
 * @since 2019-11-20
 */
@Service
public class WorkerNodeServiceImpl extends ServiceImpl<WorkerNodeMapper, WorkerNode> implements IWorkerNodeService {

}
