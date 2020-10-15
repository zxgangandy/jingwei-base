package io.jingwei.base.idgen.worker.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * DB WorkerID Assigner for UID Generator
 * </p>
 *
 * @author Andy
 * @since 2019-11-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class WorkerNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * auto increment id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * host name
     */
    private String hostName;

    /**
     * port
     */
    private String port;

    /**
     * node type: CONTAINER(1), ACTUAL(2), FAKE(3)
     */
    private Integer type;

    /**
     * launch date
     */
    private LocalDate launchDate;

    /**
     * modified time
     */
    private LocalDateTime modified;

    /**
     * created time
     */
    private LocalDateTime created;


}
