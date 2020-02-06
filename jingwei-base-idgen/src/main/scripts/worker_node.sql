DROP TABLE IF EXISTS worker_node;
CREATE TABLE worker_node
(
id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'auto increment id',
host_name VARCHAR(64) NOT NULL COMMENT 'host name',
port VARCHAR(64) NOT NULL COMMENT 'port',
type INT NOT NULL COMMENT 'node type: CONTAINER(1), ACTUAL(2), FAKE(3)',
launch_date DATE NOT NULL COMMENT 'launch date',
modified TIMESTAMP NOT NULL COMMENT 'modified time',
created TIMESTAMP NOT NULL COMMENT 'created time',
PRIMARY KEY(ID)
)COMMENT='DB WorkerID Assigner for UID Generator',ENGINE = INNODB;
