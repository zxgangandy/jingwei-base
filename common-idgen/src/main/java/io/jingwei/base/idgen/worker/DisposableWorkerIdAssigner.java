/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jingwei.base.idgen.worker;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jingwei.base.idgen.utils.DockerUtils;
import io.jingwei.base.idgen.utils.NetUtils;
import io.jingwei.base.idgen.worker.entity.WorkerNode;
import io.jingwei.base.idgen.worker.service.IWorkerNodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * Represents an implementation of {@link WorkerIdAssigner},
 * the worker id will be discarded after assigned to the UidGenerator
 *
 * @author yutianbao
 */
@Slf4j
public class DisposableWorkerIdAssigner implements WorkerIdAssigner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisposableWorkerIdAssigner.class);

    @Resource
    private IWorkerNodeService workerNodeService;

    @Value("${server.port}")
    private String serverPort ;

    /**
     * Assign worker id base on database.<p>
     * If there is host name & port in the environment, we considered that the node runs in Docker container<br>
     * Otherwise, the node runs on an actual machine.
     *
     * @return assigned worker id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public long assignWorkerId() {
        // build worker node entity
        WorkerNode workerNode = buildWorkerNode();

        // add worker node for new (ignore the same IP + PORT)
        QueryWrapper<WorkerNode> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(WorkerNode::getHostName, workerNode.getHostName());
        WorkerNode node = workerNodeService.getOne(wrapper);
        if (node != null) {
            return node.getId();
        }

        workerNodeService.save(workerNode);
        LOGGER.info("Add worker node:" + workerNode);

        return workerNode.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public long assignFakeWorkerId() {
        return buildFakeWorkerNode().getId();
    }

    /**
     * Build worker node entity by IP and PORT
     */
    private WorkerNode buildWorkerNode() {
        WorkerNode workerNodeEntity = new WorkerNode();
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setType(WorkerNodeType.CONTAINER.value())
                    .setHostName(DockerUtils.getDockerHost())
                    .setPort(DockerUtils.getDockerPort())
                    .setLaunchDate(LocalDate.now());
        } else {
            workerNodeEntity.setType(WorkerNodeType.ACTUAL.value())
                    .setHostName(NetUtils.getHostName())
                    .setPort(serverPort)
                    .setLaunchDate(LocalDate.now());
        }

        return workerNodeEntity;
    }

    private WorkerNode buildFakeWorkerNode() {
        WorkerNode workerNodeEntity = new WorkerNode();
        workerNodeEntity.setType(WorkerNodeType.FAKE.value());
        if (DockerUtils.isDocker()) {
            workerNodeEntity.setHostName(DockerUtils.getDockerHost());
            workerNodeEntity.setPort(DockerUtils.getDockerPort() + "-" + RandomUtils.nextInt(100000));
        }else {
            workerNodeEntity.setHostName(NetUtils.getLocalAddress());
            workerNodeEntity.setPort(System.currentTimeMillis() + "-" + RandomUtils.nextInt(100000));
        }
        return workerNodeEntity;
    }


}
