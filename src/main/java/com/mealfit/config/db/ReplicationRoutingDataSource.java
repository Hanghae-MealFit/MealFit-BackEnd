package com.mealfit.config.db;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private SlaveNames slaveNames;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        List<String> replicas = targetDataSources.keySet().stream()
              .map(Object::toString)
              .filter(string -> string.contains("slave"))
              .collect(toList());

        this.slaveNames = new SlaveNames(replicas);
    }

    @Override
    protected String determineCurrentLookupKey() {
        // 조회 쿼리인 경우 Slave
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            //다음 slave 선택
            return slaveNames.getNextName();
        }

        // 그 외인 경우 Master
        return "master";
    }

    public class SlaveNames {

        private final String[] value;
        private int counter = 0;

        public SlaveNames(List<String> slaveDataSourceProperties) {
            this(slaveDataSourceProperties.toArray(String[]::new));
        }

        public SlaveNames(String[] value) {
            this.value = value;
        }

        // 아주 간단하게 번갈아가며 사용하는 로직
        public String getNextName() {
            int index = counter;
            counter = (counter + 1) % value.length;
            return value[index];
        }
    }
}

