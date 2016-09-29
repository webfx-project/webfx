package mongoose.entities.impl;

import mongoose.entities.MetricsEntity;
import naga.framework.orm.entity.EntityId;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.entity.impl.DynamicEntity;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public class MetricsEntityImpl extends DynamicEntity implements MetricsEntity {

    public MetricsEntityImpl(EntityId id, EntityStore store) {
        super(id, store);
    }

    @Override
    public void setDate(Instant date) {
        setFieldValue("date", date);
    }

    @Override
    public Instant getDate() {
        return getInstantFieldValue("date");
    }

    @Override
    public void setMemoryTotal(Long memoryTotal) {
        setFieldValue("memoryTotal", memoryTotal);
    }

    @Override
    public Long getMemoryTotal() {
        return getLongFieldValue("memoryTotal");
    }

    @Override
    public void setMemoryFree(Long memoryFree) {
        setFieldValue("memoryFree", memoryFree);
    }

    @Override
    public Long getMemoryFree() {
        return getLongFieldValue("memoryFree");
    }

    @Override
    public void setMemoryMax(Long memoryMax) {
        setFieldValue("memoryMax", memoryMax);
    }

    @Override
    public Long getMemoryMax() {
        return getLongFieldValue("memoryMax");
    }

    @Override
    public void setSystemLoadAverage(Double systemLoadAverage) {
        setFieldValue("systemLoadAverage", systemLoadAverage);
    }

    @Override
    public Double getSystemLoadAverage() {
        return getDoubleFieldValue("systemLoadAverage");
    }

    @Override
    public void setProcessCpuLoad(Double processCpuLoad) {
        setFieldValue("processCpuLoad", processCpuLoad);
    }

    @Override
    public Double getProcessCpuLoad() {
        return getDoubleFieldValue("processCpuLoad");
    }

    @Override
    public Long getMemoryUsed() {
        return getLongFieldValue("memoryUsed");
    }
}
